#!/usr/bin/env python3
"""Genera docs/proceso/historias-de-usuario.xlsx a partir del backlog Markdown.

Fuentes:
  - docs/user-stories.md
  - docs/proceso/trazabilidad.md

Uso:
  python docs/proceso/generar-historias-xlsx.py
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

try:
    from openpyxl import Workbook
    from openpyxl.styles import Alignment, Border, Font, PatternFill, Side
    from openpyxl.utils import get_column_letter
except ImportError:  # pragma: no cover
    sys.exit("Falta openpyxl. Instala con: pip install openpyxl")

ROOT = Path(__file__).resolve().parents[2]
STORIES = ROOT / "docs" / "user-stories.md"
TRACE = ROOT / "docs" / "proceso" / "trazabilidad.md"
OUTPUT = ROOT / "docs" / "proceso" / "historias-de-usuario.xlsx"

HU_RE = re.compile(r"^###\s+(HU-\d+)\s+—\s+(.+)$")
EPIC_RE = re.compile(r"^##\s+(EP-\d+)\s+—\s+(.+)$")
ROW_RE = re.compile(r"^\|\s*(.+?)\s*\|\s*$")


def clean(value: str) -> str:
    value = value.strip()
    value = value.replace("**", "")
    value = value.replace("`", "")
    return value.strip()


def cells(line: str) -> list[str]:
    match = ROW_RE.match(line.strip())
    if not match:
        return []
    # Divide por | fuera de backticks para no romper código con tuberías.
    raw = match.group(1)
    parts: list[str] = []
    current: list[str] = []
    in_code = False
    for char in raw:
        if char == "`":
            in_code = not in_code
            current.append(char)
        elif char == "|" and not in_code:
            parts.append("".join(current))
            current = []
        else:
            current.append(char)
    parts.append("".join(current))
    return [clean(p) for p in parts]


def parse_stories(path: Path) -> dict:
    lines = path.read_text(encoding="utf-8").splitlines()
    epics: list[dict] = []
    hus: list[dict] = []
    criteria: list[dict] = []
    correspondence: list[dict] = []

    current_epic: dict | None = None
    current_hu: dict | None = None
    section = ""
    in_correspondence = False

    for line in lines:
        if line.startswith("## "):
            in_correspondence = line.strip().startswith("## Correspondencia")

        epic_match = EPIC_RE.match(line.strip())
        if epic_match:
            current_epic = {"id": epic_match.group(1), "name": epic_match.group(2).strip()}
            epics.append(current_epic)
            current_hu = None
            continue

        hu_match = HU_RE.match(line.strip())
        if hu_match:
            current_hu = {
                "id": hu_match.group(1),
                "title": hu_match.group(2).strip(),
                "epic": current_epic["id"] if current_epic else "",
                "role": "",
                "feature": "",
                "reason": "",
                "priority": "",
                "estimate": "",
                "iteration": "",
                "endpoint": "",
            }
            hus.append(current_hu)
            current_hu["criteria"] = []
            continue

        if in_correspondence:
            row = cells(line)
            if len(row) >= 3 and re.match(r"^H\d+", row[0]):
                correspondence.append(
                    {"old": row[0], "new": row[1], "change": row[2]}
                )
            continue

        if current_hu is None:
            continue

        row = cells(line)
        if not row:
            continue

        if row[0].startswith("CA-"):
            if len(row) >= 6:
                raw_code = row[0]
                code = raw_code.split(" ")[0]
                kind = "Positivo" if "(P)" in raw_code else "Negativo" if "(N)" in raw_code else ""
                criteria.append(
                    {
                        "hu": current_hu["id"],
                        "code": code,
                        "kind": kind,
                        "scenario": row[1],
                        "given": row[2],
                        "when": row[3],
                        "then": row[4],
                        "guidelines": row[5],
                    }
                )
                current_hu["criteria"].append(code)
            continue

        key = row[0]
        value = row[1] if len(row) > 1 else ""
        mapping = {
            "Rol": "role",
            "Característica": "feature",
            "Razón": "reason",
            "Prioridad": "priority",
            "Estimación": "estimate",
            "Iteración": "iteration",
            "Endpoint": "endpoint",
        }
        if key in mapping:
            current_hu[mapping[key]] = value

    return {
        "epics": epics,
        "hus": hus,
        "criteria": criteria,
        "correspondence": correspondence,
    }


def parse_trace(path: Path) -> list[dict]:
    rows: list[dict] = []
    for line in path.read_text(encoding="utf-8").splitlines():
        row = cells(line)
        if len(row) >= 6 and re.match(r"^HU-\d+$", row[0]) and row[1].startswith("CA-"):
            rows.append(
                {
                    "hu": row[0],
                    "code": row[1],
                    "endpoint": row[2],
                    "implementation": row[3],
                    "test": row[4],
                    "status": row[5],
                }
            )
    return rows


HEADER_FILL = PatternFill("solid", fgColor="1F4E79")
HEADER_FONT = Font(bold=True, color="FFFFFF")
TITLE_FONT = Font(bold=True, size=14, color="1F4E79")
THIN = Side(style="thin", color="BFBFBF")
BORDER = Border(left=THIN, right=THIN, top=THIN, bottom=THIN)
WRAP = Alignment(wrap_text=True, vertical="top")
CENTER = Alignment(horizontal="center", vertical="center", wrap_text=True)
ALT_FILL = PatternFill("solid", fgColor="F2F7FB")


def style_sheet(ws, headers: list[str], widths: list[int], rows: list[list]) -> None:
    ws.freeze_panes = "A2"
    for col, (header, width) in enumerate(zip(headers, widths), start=1):
        cell = ws.cell(row=1, column=col, value=header)
        cell.fill = HEADER_FILL
        cell.font = HEADER_FONT
        cell.alignment = CENTER
        cell.border = BORDER
        ws.column_dimensions[get_column_letter(col)].width = width

    for index, values in enumerate(rows, start=2):
        for col, value in enumerate(values, start=1):
            cell = ws.cell(row=index, column=col, value=value)
            cell.alignment = WRAP
            cell.border = BORDER
            if index % 2 == 0:
                cell.fill = ALT_FILL

    ws.auto_filter.ref = f"A1:{get_column_letter(len(headers))}{max(len(rows) + 1, 2)}"
    ws.sheet_view.showGridLines = False


def add_title(ws, row: int, text: str) -> None:
    cell = ws.cell(row=row, column=1, value=text)
    cell.font = TITLE_FONT
    ws.merge_cells(start_row=row, start_column=1, end_row=row, end_column=6)


def main() -> int:
    data = parse_stories(STORIES)
    trace = parse_trace(TRACE)

    wb = Workbook()

    # ---------------------------------------------------------------- Épicas
    ws = wb.active
    ws.title = "Épicas"
    epic_rows = []
    for epic in data["epics"]:
        epic_hus = [h for h in data["hus"] if h["epic"] == epic["id"]]
        points = sum(int(re.sub(r"\D", "", h["estimate"]) or 0) for h in epic_hus)
        epic_rows.append(
            [epic["id"], epic["name"], len(epic_hus), ", ".join(h["id"] for h in epic_hus), points]
        )
    style_sheet(
        ws,
        ["Código", "Épica", "# HU", "Historias", "Story points"],
        [10, 42, 8, 30, 14],
        epic_rows,
    )
    total_row = len(epic_rows) + 2
    ws.cell(row=total_row, column=1, value="TOTAL").font = Font(bold=True)
    ws.cell(row=total_row, column=2, value="").font = Font(bold=True)
    ws.cell(row=total_row, column=3, value=len(data["hus"])).font = Font(bold=True)
    ws.cell(row=total_row, column=4, value="").font = Font(bold=True)
    ws.cell(
        row=total_row,
        column=5,
        value=sum(r[4] for r in epic_rows),
    ).font = Font(bold=True)

    # ------------------------------------------------------------------- HUs
    ws = wb.create_sheet("HUs")
    hu_rows = []
    for hu in data["hus"]:
        hu_rows.append(
            [
                hu["id"],
                hu["epic"],
                hu["role"],
                hu["feature"],
                hu["reason"],
                hu["priority"],
                hu["estimate"],
                hu["iteration"],
                hu["endpoint"],
                len(hu["criteria"]),
            ]
        )
    style_sheet(
        ws,
        [
            "ID",
            "Épica",
            "Rol",
            "Característica",
            "Razón",
            "Prioridad",
            "Estimación",
            "Iteración",
            "Endpoint",
            "# CA",
        ],
        [10, 10, 22, 30, 45, 10, 12, 12, 40, 6],
        hu_rows,
    )

    # ------------------------------------------------------------- Criterios
    ws = wb.create_sheet("Criterios")
    ca_rows = []
    for ca in data["criteria"]:
        ca_rows.append(
            [
                ca["hu"],
                ca["code"],
                ca["kind"],
                ca["scenario"],
                ca["given"],
                ca["when"],
                ca["then"],
                ca["guidelines"],
            ]
        )
    style_sheet(
        ws,
        [
            "HU",
            "Código",
            "Tipo",
            "Escenario",
            "Dado que",
            "Cuando",
            "Entonces",
            "Lineamientos técnicos",
        ],
        [10, 12, 10, 34, 42, 42, 48, 60],
        ca_rows,
    )

    # ----------------------------------------------------------- Trazabilidad
    ws = wb.create_sheet("Trazabilidad")
    tr_rows = [
        [t["hu"], t["code"], t["endpoint"], t["implementation"], t["test"], t["status"]]
        for t in trace
    ]
    style_sheet(
        ws,
        ["HU", "CA", "Endpoint / evento", "Implementación", "Prueba", "Estado"],
        [10, 12, 42, 48, 58, 14],
        tr_rows,
    )

    # ---------------------------------------------------------------- Resumen
    ws = wb.create_sheet("Resumen")
    ws.sheet_view.showGridLines = False
    add_title(ws, 1, "Backlog de historias de usuario — resumen")
    ws["A2"] = "Sistema de Gestión de Trabajos de Grado — FIET, Universidad del Cauca"
    ws["A2"].font = Font(italic=True, color="595959")

    total_points = sum(int(re.sub(r"\D", "", h["estimate"]) or 0) for h in data["hus"])
    positives = sum(1 for c in data["criteria"] if c["kind"] == "Positivo")
    negatives = sum(1 for c in data["criteria"] if c["kind"] == "Negativo")
    coverage = {"Cubierto": 0, "Parcial": 0, "Manual": 0, "#55": 0}
    for t in trace:
        if t["status"] in coverage:
            coverage[t["status"]] += 1

    metrics = [
        ("Épicas", len(data["epics"])),
        ("Historias de usuario", len(data["hus"])),
        ("Criterios de aceptación", len(data["criteria"])),
        ("Criterios positivos (P)", positives),
        ("Criterios negativos (N)", negatives),
        ("Story points totales", total_points),
        ("HU con exactamente 3 CA", sum(1 for h in data["hus"] if len(h["criteria"]) == 3)),
        ("Criterios cubiertos por prueba", coverage["Cubierto"]),
        ("Criterios parcialmente cubiertos", coverage["Parcial"]),
        ("Criterios verificados manualmente", coverage["Manual"]),
        ("Criterios que dependen de #55", coverage["#55"]),
    ]

    row = 4
    ws.cell(row=row, column=1, value="Métrica").font = HEADER_FONT
    ws.cell(row=row, column=1).fill = HEADER_FILL
    ws.cell(row=row, column=2, value="Valor").font = HEADER_FONT
    ws.cell(row=row, column=2).fill = HEADER_FILL
    row += 1
    for label, value in metrics:
        ws.cell(row=row, column=1, value=label).border = BORDER
        ws.cell(row=row, column=2, value=value).border = BORDER
        ws.cell(row=row, column=2).alignment = CENTER
        row += 1

    row += 1
    ws.cell(row=row, column=1, value="Correspondencia con el backlog anterior").font = TITLE_FONT
    row += 1
    for header, col in (("Anterior", 1), ("Actual", 2), ("Cambio", 3)):
        cell = ws.cell(row=row, column=col, value=header)
        cell.fill = HEADER_FILL
        cell.font = HEADER_FONT
        cell.alignment = CENTER
        cell.border = BORDER
    row += 1
    for item in data["correspondence"]:
        for col, value in enumerate([item["old"], item["new"], item["change"]], start=1):
            cell = ws.cell(row=row, column=col, value=value)
            cell.border = BORDER
            cell.alignment = WRAP
        row += 1

    ws.column_dimensions["A"].width = 38
    ws.column_dimensions["B"].width = 30
    ws.column_dimensions["C"].width = 60

    wb.save(OUTPUT)
    print(f"Generado: {OUTPUT}")
    print(
        f"  {len(data['epics'])} épicas, {len(data['hus'])} HU, "
        f"{len(data['criteria'])} CA, {total_points} SP"
    )
    print(
        f"  Cobertura: {coverage['Cubierto']} cubiertos, {coverage['Parcial']} parciales, "
        f"{coverage['Manual']} manuales, {coverage['#55']} #55"
    )
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
