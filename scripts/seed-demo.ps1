$ErrorActionPreference = 'Stop'

$script = Join-Path $PSScriptRoot 'seed-demo.py'

if (Get-Command python -ErrorAction SilentlyContinue) {
    & python $script @args
}
elseif (Get-Command py -ErrorAction SilentlyContinue) {
    & py -3 $script @args
}
else {
    throw 'Python 3 es necesario para ejecutar la semilla de demo.'
}

exit $LASTEXITCODE
