# Documentación del proceso

Esta carpeta reúne los artefactos de **ingeniería de requisitos y proceso** del
Sistema de Gestión de Trabajos de Grado. Complementan la documentación técnica y
la arquitectura, y sirven como evidencia del trabajo de análisis, diseño y
validación.

## Contenido

| Documento | Qué contiene |
|---|---|
| [`guia-historias-de-usuario.md`](guia-historias-de-usuario.md) | Estándar de redacción de HU y CA: formato canónico, roles, BDD, 2P+1N, estimación y checklist. |
| [`../user-stories.md`](../user-stories.md) | Backlog completo: 6 épicas, 21 HU, 63 CA y 68 SP. |
| [`trazabilidad.md`](trazabilidad.md) | Matriz HU → CA → endpoint → prueba, con estado de cobertura. |
| [`usabilidad.md`](usabilidad.md) | Prueba de usabilidad con dos participantes, resultados y hallazgos. |
| [`demo.md`](demo.md) | Vídeo del primer corte (versión Swing) y evolución del producto. |
| [`prototipo.md`](prototipo.md) | Prototipo navegable en Figma y su relación con la implementación. |
| [`historias-de-usuario.xlsx`](historias-de-usuario.xlsx) | Versión legible del backlog en hojas de cálculo (épicas, HU, criterios, trazabilidad y resumen). |
| [`../adr/0003-arquitectura-inicial.md`](../adr/0003-arquitectura-inicial.md) | Decisión arquitectónica del primer corte: monolito modular + Swing. |
| [`fuentes/`](fuentes/) | Documentos originales del curso (DOCX, XLSX y enlaces). |

## Cómo se relacionan

```text
fuentes/ (DOCX, XLSX originales)
        │
        ├── guia-historias-de-usuario.md  ── estándar de redacción
        │
        ├── ../user-stories.md            ── backlog canónico
        │        │
        │        └── trazabilidad.md      ── HU → CA → endpoint → prueba
        │
        ├── usabilidad.md                 ── validación con usuarios
        ├── demo.md                       ── evidencia del primer corte
        ├── prototipo.md                  ── referencia de interfaz
        └── ../adr/0003-...               ── decisión arquitectónica inicial
```

## Regenerar el XLSX

El archivo `historias-de-usuario.xlsx` se genera a partir de las tablas
Markdown de `docs/user-stories.md` y `docs/proceso/trazabilidad.md`. Para
regenerarlo:

```bash
python docs/proceso/generar-historias-xlsx.py
```

El script requiere `openpyxl` (`pip install openpyxl`). No se versiona ningún
dato sensible: solo contiene el backlog y la matriz de trazabilidad.

## Convenciones

- **IDs de épica:** `EP-01` … `EP-06`.
- **IDs de historia:** `HU-01` … `HU-21`, continuos.
- **IDs de criterio:** `CA-XX.y (P|N)`, donde `y` es 1..3 y `P`/`N` indica
  positivo o negativo.
- **Estimación:** Fibonacci modificada (1, 2, 3, 5, 8, 13). La épica suma sus HU.
- **Estados de cobertura:** Cubierto, Parcial, Manual y `#55`.

## Origen de los archivos

Los documentos originales del curso se conservan sin modificaciones en
[`fuentes/`](fuentes/):

- `Arquitectura y diseño.docx`
- `INGENIERÍA DE SOFTWARE HU - Vrs 0.xlsx`
- `Usabilidad.docx`
- `link youtube.txt`
- `llink prototipo.txt`

Las versiones Markdown y XLSX de esta carpeta son las **canónicas y legibles**;
los originales se mantienen como evidencia.
