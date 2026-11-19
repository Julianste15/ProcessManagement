#!/usr/bin/env python3
"""Siembra datos de demo en ProcessManagement a traves del API Gateway.

El script habla solo con el gateway (por defecto http://localhost:8080), usa el
mismo contrato HTTP que la aplicacion y es idempotente: Si vuelve a ejecutarse
sobre un stack ya sembrado no duplica usuarios, formatos ni anteproyectos.

No se ejecuta si detecta un perfil de produccion.

Uso::

    python scripts/seed-demo.py
    python scripts/seed-demo.py --base-url http://localhost:8080 --wait-seconds 180

Tambien puede invocarse desde los envoltorios ``seed-demo.ps1`` / ``seed-demo.sh``
o desde Docker Compose con ``docker compose --profile seed run --rm seed-demo``.
"""

from __future__ import annotations

import argparse
import base64
import json
import os
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
from datetime import datetime, timedelta
from typing import Any, Callable

DEMO_PASSWORD = "Demo123!"
CAREER = "SYSTEMS_ENGINEERING"
MODALITY = "INVESTIGACION"

STATE_IN_EVALUATION = "FORMATO_A_EN_EVALUACION"
STATE_ACCEPTED = "FORMATO_A_ACEPTADO"
STATE_REJECTED = "FORMATO_A_RECHAZADO"
TARGET_REJECTED_WITH_RETRIES = "REJECTED_WITH_RETRIES"

COORDINATOR_EMAIL = "coordinador.demo@unicauca.edu.co"
DIRECTOR_EMAIL = "director.demo@unicauca.edu.co"
EVALUATOR_1_EMAIL = "evaluador1.demo@unicauca.edu.co"
EVALUATOR_2_EMAIL = "evaluador2.demo@unicauca.edu.co"
ADMIN_EMAIL = "administrador.demo@unicauca.edu.co"

USERS: tuple[dict[str, Any], ...] = (
    {
        "names": "Maria",
        "surnames": "Torres",
        "email": COORDINATOR_EMAIL,
        "role": "COORDINATOR",
        "telephone": 3000000001,
    },
    {
        "names": "Ana Maria",
        "surnames": "Perez Lopez",
        "email": DIRECTOR_EMAIL,
        "role": "TEACHER",
        "telephone": 3000000002,
    },
    {
        "names": "Carlos",
        "surnames": "Ruiz",
        "email": EVALUATOR_1_EMAIL,
        "role": "TEACHER",
        "telephone": 3000000003,
    },
    {
        "names": "Lucia",
        "surnames": "Mora",
        "email": EVALUATOR_2_EMAIL,
        "role": "TEACHER",
        "telephone": 3000000004,
    },
    {
        "names": "Jorge",
        "surnames": "Salazar",
        "email": ADMIN_EMAIL,
        "role": "ADMINISTRATOR",
        "telephone": 3000000005,
    },
    {
        "names": "Juan",
        "surnames": "Diaz",
        "email": "estudiante1.demo@unicauca.edu.co",
        "role": "STUDENT",
        "telephone": 3000000006,
    },
    {
        "names": "Sofia",
        "surnames": "Gomez",
        "email": "estudiante2.demo@unicauca.edu.co",
        "role": "STUDENT",
        "telephone": 3000000007,
    },
    {
        "names": "Mateo",
        "surnames": "Vargas",
        "email": "estudiante3.demo@unicauca.edu.co",
        "role": "STUDENT",
        "telephone": 3000000008,
    },
)

FORMATS: tuple[dict[str, Any], ...] = (
    {
        "title": "Gestion de proyectos de grado",
        "student": "estudiante1.demo@unicauca.edu.co",
        "target": STATE_ACCEPTED,
        "objective": "Disenar e implementar un sistema de gestion de proyectos de grado.",
        "specific": ["Levantar requisitos", "Implementar la API", "Validar con usuarios"],
    },
    {
        "title": "Sistema de tutorias academicas",
        "student": "estudiante2.demo@unicauca.edu.co",
        "target": STATE_IN_EVALUATION,
        "objective": "Construir una plataforma de seguimiento de tutorias academicas.",
        "specific": ["Modelar el dominio", "Implementar notificaciones"],
    },
    {
        "title": "Analisis de retencion estudiantil",
        "student": "estudiante3.demo@unicauca.edu.co",
        "target": TARGET_REJECTED_WITH_RETRIES,
        "objective": "Analizar los factores que influyen en la retencion estudiantil.",
        "specific": ["Recopilar datos", "Proponer recomendaciones"],
    },
)

DISCOVERY_ERRORS = (
    "service instance cannot be null",
    "no servers available",
    "loadbalancer",
    "connection refused",
)

PRODUCTION_PROFILES = {"prod", "production", "produccion", "release", "live"}
PROFILE_ENV_VARS = (
    "SPRING_PROFILES_ACTIVE",
    "SPRING_PROFILES_DEFAULT",
    "APP_ENV",
    "ENVIRONMENT",
    "NODE_ENV",
)

BASE_URL = "http://localhost:8080"
TIMEOUT = 5.0


class SeedError(RuntimeError):
    """Error de la semilla con mensaje accionable para quien la ejecuta."""


def log(message: str) -> None:
    print(message, flush=True)


def assert_not_production() -> None:
    """Bloquea la semilla si el entorno se parece a produccion."""
    detected: list[str] = []
    for variable in PROFILE_ENV_VARS:
        raw_value = os.environ.get(variable, "")
        for value in re.split(r"[,\s]+", raw_value.lower()):
            if value in PRODUCTION_PROFILES:
                detected.append(f"{variable}={value}")
    if detected:
        raise SeedError(
            "Seed de demo bloqueado: se detecto un entorno de produccion ("
            + ", ".join(detected)
            + "). Ejecuta la semilla solo en desarrollo o con el perfil 'docker'."
        )


def parse_body(raw: str) -> Any:
    if not raw:
        return None
    try:
        return json.loads(raw)
    except json.JSONDecodeError:
        return raw


def http(
    method: str,
    path: str,
    *,
    token: str | None = None,
    payload: Any = None,
    params: dict[str, Any] | None = None,
    allowed: tuple[int, ...] = (200,),
) -> Any:
    """Realiza una peticion HTTP al gateway y devuelve el cuerpo interpretado."""
    url = BASE_URL + path
    if params:
        url += "?" + urllib.parse.urlencode(params)

    headers = {"Accept": "application/json, text/plain, */*"}
    data: bytes | None = None
    if payload is not None:
        data = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        headers["Content-Type"] = "application/json; charset=utf-8"
    if token:
        headers["Authorization"] = "Bearer " + token

    request = urllib.request.Request(url, data=data, headers=headers, method=method)
    try:
        with urllib.request.urlopen(request, timeout=TIMEOUT) as response:
            status = response.status
            raw = response.read().decode("utf-8", errors="replace")
    except urllib.error.HTTPError as error:
        status = error.code
        raw = error.read().decode("utf-8", errors="replace")
        if status not in allowed:
            raise SeedError(f"{method} {path} -> HTTP {status}: {raw[:300]}") from error
        return None if status == 404 else parse_body(raw)
    except urllib.error.URLError as error:
        raise SeedError(
            f"No se pudo conectar con {BASE_URL} ({error.reason}). "
            "Levanta el stack con: docker compose up -d --build"
        ) from error

    if status not in allowed:
        raise SeedError(f"{method} {path} -> HTTP {status}: {raw[:300]}")
    return parse_body(raw)


def wait_for(operation: Callable[[], Any], description: str, attempts: int) -> Any:
    """Reintenta una operacion mientras los servicios terminan de arrancar."""
    last_error: SeedError | None = None
    for attempt in range(1, attempts + 1):
        try:
            return operation()
        except SeedError as error:
            last_error = error
            if attempt < attempts:
                time.sleep(2)
    raise SeedError(
        f"No se pudo {description} tras {attempts} intentos. Ultimo error: {last_error}"
    )


def is_discovery_error(error: SeedError) -> bool:
    """Detecta fallos transitorios de Eureka/Feign mientras arranca el stack."""
    message = str(error).lower()
    return any(fragment in message for fragment in DISCOVERY_ERRORS)


def retry_discovery(
    operation: Callable[[], Any], description: str, attempts: int
) -> Any:
    """Reintenta solo errores de descubrimiento, nunca errores de negocio."""
    last_error: SeedError | None = None
    for attempt in range(1, attempts + 1):
        try:
            return operation()
        except SeedError as error:
            if not is_discovery_error(error):
                raise
            last_error = error
            if attempt < attempts:
                time.sleep(2)
    raise SeedError(
        f"No se pudo {description} tras {attempts} intentos. Ultimo error: {last_error}"
    )


def wait_for_gateway(attempts: int) -> None:
    def probe() -> bool:
        http("GET", "/actuator/health", allowed=(200, 404, 503))
        return True

    wait_for(probe, "contactar el gateway", attempts)
    log("[OK] Gateway acceso y respondiendo.")


def demo_pdf_base64() -> str:
    """Devuelve un PDF minimo pero decodificable como base64."""
    pdf = (
        b"%PDF-1.4\n"
        b"1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n"
        b"2 0 obj\n<< /Type /Pages /Count 0 >>\nendobj\n"
        b"trailer\n<< /Root 1 0 R >>\n"
        b"%%EOF\n"
    )
    return base64.b64encode(pdf).decode("ascii")


def register_users(attempts: int) -> None:
    def register_all() -> None:
        for user in USERS:
            payload = {
                "names": user["names"],
                "surnames": user["surnames"],
                "email": user["email"],
                "password": DEMO_PASSWORD,
                "telephone": user["telephone"],
                "career": CAREER,
                "role": user["role"],
            }
            try:
                http("POST", "/api/users/register", payload=payload)
                log(f"  [creado]   {user['email']} ({user['role']})")
            except SeedError as error:
                message = str(error).lower()
                if "ya existe" in message or "already" in message:
                    log(f"  [existente] {user['email']} ({user['role']})")
                else:
                    raise

    log("Registrando usuarios de demo...")
    wait_for(register_all, "registrar los usuarios de demo", attempts)


def login(email: str) -> str:
    response = http(
        "POST",
        "/api/auth/login",
        payload={"email": email, "password": DEMO_PASSWORD},
    )
    if not isinstance(response, dict) or not response.get("token"):
        raise SeedError(f"El login de {email} no devolvio un token JWT.")
    return response["token"]


def get_user_id(email: str, token: str) -> int:
    user = http("GET", "/api/users/" + urllib.parse.quote(email, safe="@"), token=token)
    if not isinstance(user, dict) or not isinstance(user.get("id"), int):
        raise SeedError(f"No se pudo obtener el id del usuario {email}.")
    return user["id"]


def assign_department_head(coordinator_token: str) -> None:
    director_id = get_user_id(DIRECTOR_EMAIL, coordinator_token)
    current = http(
        "GET",
        "/api/department-head/current",
        token=coordinator_token,
        allowed=(200, 404),
    )
    if current == DIRECTOR_EMAIL:
        log("  [existente] Jefe de departamento ya asignado.")
        return

    now = datetime.now().replace(microsecond=0)
    http(
        "POST",
        "/api/department-head/assign",
        token=coordinator_token,
        params={
            "teacherId": director_id,
            "startDate": (now - timedelta(days=1)).strftime("%Y-%m-%dT%H:%M:%S"),
            "endDate": (now + timedelta(days=365)).strftime("%Y-%m-%dT%H:%M:%S"),
        },
    )
    log(f"  [creado]   Jefe de departamento: {DIRECTOR_EMAIL}")


def format_payload(spec: dict[str, Any], pdf: str) -> dict[str, Any]:
    slug = re.sub(r"[^a-zA-Z0-9]+", "-", spec["title"].lower()).strip("-")
    return {
        "titulo": spec["title"],
        "modalidad": MODALITY,
        "directorEmail": DIRECTOR_EMAIL,
        "codirectorEmail": None,
        "studentEmail": spec["student"],
        "objetivoGeneral": spec["objective"],
        "objetivosEspecificos": spec["specific"],
        "archivoPdfNombre": f"{slug}.pdf",
        "archivoPdfContenido": pdf,
    }


def wait_for_format_a_discovery(pdf: str, student_token: str, attempts: int) -> None:
    """Espera a que format-a-service pueda resolver user-service via Eureka.

    El envio de prueba usa un director inexistente: nunca crea datos, pero si
    fuerza la llamada Feign que falla mientras la cache de Eureka se llena.
    """
    probe = format_payload(
        {
            "title": "warmup-descubrimiento",
            "student": "warmup-student.invalid@unicauca.edu.co",
            "objective": "Sonda de disponibilidad del descubrimiento.",
            "specific": ["Sonda"],
        },
        pdf,
    )
    probe["directorEmail"] = "warmup-director.invalid@unicauca.edu.co"

    def try_probe() -> bool:
        try:
            http("POST", "/api/format-a/submit", token=student_token, payload=probe)
        except SeedError as error:
            if is_discovery_error(error):
                raise
            # Usuario inexistente, estado invalido, etc.: Eureka ya responde.
        return True

    wait_for(try_probe, "resolver user-service desde format-a-service", attempts)
    log("[OK] format-a-service ya resuelve user-service.")


def get_format(format_id: int, token: str) -> dict[str, Any]:
    result = http("GET", f"/api/format-a/{format_id}", token=token)
    if not isinstance(result, dict):
        raise SeedError(f"Respuesta inesperada al leer el Formato A {format_id}.")
    return result


def evaluate_format(format_id: int, state: str, observations: str, token: str) -> dict[str, Any]:
    result = http(
        "PUT",
        f"/api/format-a/{format_id}/evaluate",
        token=token,
        payload={"estado": state, "observaciones": observations},
    )
    if not isinstance(result, dict):
        raise SeedError(f"Respuesta inesperada al evaluar el Formato A {format_id}.")
    return result


def resubmit_format(format_id: int, payload: dict[str, Any], token: str) -> dict[str, Any]:
    result = http(
        "PUT",
        f"/api/format-a/{format_id}/resubmit",
        token=token,
        payload=payload,
    )
    if not isinstance(result, dict):
        raise SeedError(f"Respuesta inesperada al reenviar el Formato A {format_id}.")
    return result


def ensure_format(
    spec: dict[str, Any],
    pdf: str,
    student_token: str,
    coordinator_token: str,
    attempts: int,
) -> int:
    payload = format_payload(spec, pdf)
    existing_list = http(
        "GET",
        "/api/format-a/user/" + urllib.parse.quote(DIRECTOR_EMAIL, safe="@"),
        token=coordinator_token,
    )
    if not isinstance(existing_list, list):
        raise SeedError("Respuesta inesperada al listar los Formatos A del director.")

    existing = next((item for item in existing_list if item.get("titulo") == spec["title"]), None)
    if existing:
        current = get_format(existing["id"], coordinator_token)
        log(
            f"  [existente] {spec['title']} -> id={current['id']}, "
            f"estado={current.get('estado')}, intentos={current.get('intentos')}"
        )
    else:
        current = retry_discovery(
            lambda: http("POST", "/api/format-a/submit", token=student_token, payload=payload),
            f"crear el Formato A '{spec['title']}'",
            attempts,
        )
        if not isinstance(current, dict) or not isinstance(current.get("id"), int):
            raise SeedError(f"No se pudo crear el Formato A '{spec['title']}'.")
        log(f"  [creado]   {spec['title']} -> id={current['id']}")

    format_id = current["id"]
    state = current.get("estado")
    attempts = int(current.get("intentos") or 0)
    target = spec["target"]

    if target == STATE_IN_EVALUATION:
        if state == STATE_REJECTED:
            current = resubmit_format(format_id, payload, student_token)
            state = current.get("estado")
            attempts = int(current.get("intentos") or 0)
            log(f"  [reenviado] {spec['title']} -> intento {attempts}")
        elif state == STATE_ACCEPTED:
            log(f"  [aviso] {spec['title']} ya esta aceptado; no se revierte el estado.")

    elif target == STATE_ACCEPTED:
        if state == STATE_REJECTED:
            current = resubmit_format(format_id, payload, student_token)
            state = current.get("estado")
            attempts = int(current.get("intentos") or 0)
            log(f"  [reenviado] {spec['title']} -> intento {attempts}")
        if state == STATE_IN_EVALUATION:
            current = evaluate_format(
                format_id,
                STATE_ACCEPTED,
                "Aprobado para anteproyecto por la semilla de demo.",
                coordinator_token,
            )
            state = current.get("estado")
            log(f"  [aprobado] {spec['title']}")

    elif target == TARGET_REJECTED_WITH_RETRIES:
        if state == STATE_ACCEPTED:
            raise SeedError(
                f"No se puede convertir '{spec['title']}' en rechazado: ya esta aceptado. "
                "Usa un stack limpio para reconstruir la semilla."
            )
        if state == STATE_IN_EVALUATION:
            current = evaluate_format(
                format_id,
                STATE_REJECTED,
                "Rechazado por la semilla de demo (primer intento).",
                coordinator_token,
            )
            state = current.get("estado")
            log(f"  [rechazado] {spec['title']} -> intento {attempts}")
        if state == STATE_REJECTED and attempts < 2:
            current = resubmit_format(format_id, payload, student_token)
            attempts = int(current.get("intentos") or 0)
            log(f"  [reenviado] {spec['title']} -> intento {attempts}")
            current = evaluate_format(
                format_id,
                STATE_REJECTED,
                "Rechazado por la semilla de demo (segundo intento).",
                coordinator_token,
            )
            state = current.get("estado")
            log(f"  [rechazado] {spec['title']} -> intento {attempts}")

    final = get_format(format_id, coordinator_token)
    if final.get("estado") != state:
        log(f"  [aviso] Estado final de '{spec['title']}' es {final.get('estado')}.")
    return format_id


def ensure_anteproject(
    accepted_format_id: int,
    director_token: str,
    head_token: str,
    attempts: int,
) -> None:
    existing_list = http(
        "GET",
        "/api/anteprojects/director/" + urllib.parse.quote(DIRECTOR_EMAIL, safe="@"),
        token=director_token,
    )
    if not isinstance(existing_list, list):
        raise SeedError("Respuesta inesperada al listar los anteproyectos del director.")

    existing = next(
        (item for item in existing_list if item.get("formatoAId") == accepted_format_id),
        None,
    )
    if existing:
        anteproject_id = existing["id"]
        log(
            f"  [existente] Anteproyecto -> id={anteproject_id}, "
            f"estado={existing.get('status')}"
        )
    else:
        created = retry_discovery(
            lambda: http(
                "POST",
                "/api/anteprojects",
                token=director_token,
                payload={
                    "formatoAId": accepted_format_id,
                    "titulo": FORMATS[0]["title"],
                    "directorEmail": DIRECTOR_EMAIL,
                },
            ),
            "crear el anteproyecto de demo",
            attempts,
        )
        if not isinstance(created, dict) or not isinstance(created.get("id"), int):
            raise SeedError("No se pudo crear el anteproyecto de demo.")
        anteproject_id = created["id"]
        existing = created
        log(f"  [creado]   Anteproyecto -> id={anteproject_id}")

    if not existing.get("documentUrl"):
        http(
            "POST",
            f"/api/anteprojects/{anteproject_id}/submit-document",
            token=director_token,
            params={"documentUrl": "https://demo.invalid/anteproyecto-demo.pdf"},
        )
        log(f"  [enviado]  Documento del anteproyecto {anteproject_id}")

    if existing.get("status") != "UNDER_EVALUATION":
        retry_discovery(
            lambda: http(
                "POST",
                f"/api/anteprojects/{anteproject_id}/assign-evaluators",
                token=head_token,
                params={
                    "evaluator1Email": EVALUATOR_1_EMAIL,
                    "evaluator2Email": EVALUATOR_2_EMAIL,
                },
            ),
            "asignar los evaluadores del anteproyecto",
            attempts,
        )
        log(f"  [asignado] Evaluadores del anteproyecto {anteproject_id}")


def run(base_url: str, wait_seconds: int, timeout: float) -> None:
    global BASE_URL, TIMEOUT
    BASE_URL = base_url.rstrip("/")
    TIMEOUT = timeout

    assert_not_production()
    attempts = max(1, wait_seconds // 2)
    log(f"Semilla de demo -> {BASE_URL}")
    wait_for_gateway(attempts)
    register_users(attempts)

    coordinator_token = wait_for(
        lambda: login(COORDINATOR_EMAIL), "autenticar el coordinador", attempts
    )
    log("[OK] Coordinador autenticado.")

    log("Asignando jefe de departamento...")
    assign_department_head(coordinator_token)

    director_token = login(DIRECTOR_EMAIL)
    # El token se emite despues de la asignacion, por lo que ya contiene
    # isDepartmentHead=true y sirve para asignar evaluadores.
    head_token = director_token
    student_tokens = {
        spec["student"]: login(spec["student"]) for spec in FORMATS
    }

    pdf = demo_pdf_base64()
    wait_for_format_a_discovery(pdf, student_tokens[FORMATS[0]["student"]], attempts)

    log("Sembrando Formatos A...")
    format_ids: dict[str, int] = {}
    for spec in FORMATS:
        format_ids[spec["target"]] = ensure_format(
            spec, pdf, student_tokens[spec["student"]], coordinator_token, attempts
        )

    log("Sembrando anteproyecto con evaluadores...")
    ensure_anteproject(
        format_ids[STATE_ACCEPTED], director_token, head_token, attempts
    )

    log("")
    log("Semilla completada. Credenciales de demo:")
    log(f"  Password para todos los usuarios: {DEMO_PASSWORD}")
    for user in USERS:
        log(f"  {user['role']:<14} {user['email']}")
    log("")
    log("La semilla es idempotente: ejecutarla de nuevo no duplica registros.")


def main() -> int:
    parser = argparse.ArgumentParser(description="Siembra datos de demo de ProcessManagement.")
    parser.add_argument(
        "--base-url",
        default=os.environ.get("DEMO_SEED_BASE_URL", "http://localhost:8080"),
        help="URL base del API Gateway (default: %(default)s)",
    )
    parser.add_argument(
        "--wait-seconds",
        type=int,
        default=int(os.environ.get("DEMO_SEED_WAIT_SECONDS", "120")),
        help="Tiempo maximo de espera a que arranquen los servicios (default: %(default)s)",
    )
    parser.add_argument(
        "--timeout",
        type=float,
        default=float(os.environ.get("DEMO_SEED_TIMEOUT", "5")),
        help="Timeout por peticion HTTP en segundos (default: %(default)s)",
    )
    args = parser.parse_args()

    try:
        run(args.base_url, args.wait_seconds, args.timeout)
    except SeedError as error:
        log(f"[ERROR] {error}")
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main())
