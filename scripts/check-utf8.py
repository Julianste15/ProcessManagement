#!/usr/bin/env python3
"""Verifica que los archivos de texto versionados estén en UTF-8 sin BOM.

Falla (exit code 1) si encuentra:

- bytes que no decodifican como UTF-8;
- BOM UTF-8 (``EF BB BF``);
- caracteres de reemplazo ``U+FFFD``;
- secuencias típicas de mojibake (``Ã``, ``Â``, ``â€``, ``ï¿½``).

Solo revisa archivos rastreados por Git con extensiones de texto, de modo que
binarios como PNG, DOCX o XLSX quedan fuera del análisis.

Uso::

    python scripts/check-utf8.py

El script es multiplataforma y no necesita dependencias externas.
"""

from __future__ import annotations

import subprocess
import sys
from pathlib import Path

TEXT_EXTENSIONS = {
    ".css",
    ".fxml",
    ".html",
    ".java",
    ".json",
    ".md",
    ".properties",
    ".ps1",
    ".py",
    ".sh",
    ".sql",
    ".txt",
    ".xml",
    ".yaml",
    ".yml",
}

BOM = b"\xef\xbb\xbf"
# Se escriben con escapes para que este mismo archivo no se marque como
# mojibake al analizarse.
REPLACEMENT_CHARACTER = "\ufffd"
MOJIBAKE_MARKERS = (
    "\u00c3",          # Ã
    "\u00c2",          # Â
    "\u00e2\u20ac",    # â€
    "\u00ef\u00bf\u00bd",  # ï¿½
)


def tracked_files(root: Path) -> list[Path]:
    """Devuelve los archivos rastreados por Git bajo ``root``."""
    try:
        output = subprocess.check_output(
            ["git", "ls-files"],
            cwd=root,
            text=True,
            encoding="utf-8",
        )
    except (subprocess.CalledProcessError, FileNotFoundError) as error:
        raise SystemExit(
            "No se pudo ejecutar 'git ls-files'. Ejecuta el script dentro del "
            f"repositorio. Detalle: {error}"
        )

    return [root / line.strip() for line in output.splitlines() if line.strip()]


def inspect(path: Path) -> list[str]:
    """Devuelve la lista de problemas encontrados en ``path``."""
    problems: list[str] = []
    data = path.read_bytes()

    if data.startswith(BOM):
        problems.append("contiene BOM UTF-8")
        data = data[len(BOM):]

    try:
        text = data.decode("utf-8")
    except UnicodeDecodeError as error:
        problems.append(
            f"no es UTF-8 válido (byte {error.start}): {error.reason}"
        )
        return problems

    if REPLACEMENT_CHARACTER in text:
        count = text.count(REPLACEMENT_CHARACTER)
        problems.append(f"contiene {count} caracter(es) de reemplazo U+FFFD")

    for marker in MOJIBAKE_MARKERS:
        if marker in text:
            problems.append(f"contiene mojibake ({marker!r})")

    return problems


def main() -> int:
    root = Path(__file__).resolve().parents[1]
    failures: list[tuple[Path, list[str]]] = []
    checked = 0

    for path in tracked_files(root):
        if not path.is_file() or path.suffix.lower() not in TEXT_EXTENSIONS:
            continue
        checked += 1
        problems = inspect(path)
        if problems:
            failures.append((path, problems))

    if failures:
        print(f"Codificación inválida en {len(failures)} archivo(s):")
        for path, problems in failures:
            relative = path.relative_to(root).as_posix()
            for problem in problems:
                print(f"  {relative}: {problem}")
        return 1

    print(f"OK: {checked} archivos de texto en UTF-8 sin BOM.")
    return 0


if __name__ == "__main__":
    sys.exit(main())
