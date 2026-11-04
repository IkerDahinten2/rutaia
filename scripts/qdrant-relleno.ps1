$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$info = Invoke-RestMethod -Uri "http://localhost:6333/collections/cursos_rutaia"
if ($info.result.points_count -gt 0) { Write-Host "Qdrant ya tiene puntos, se omite."; exit 0 }
$filas = docker exec rutaia-postgres psql -U rutaia -d rutaia -At -F "|" -c "SELECT id, nombre, categoria, nivel, duracion_horas FROM curso ORDER BY id"
$rnd = New-Object System.Random 42
$puntos = @()
foreach ($linea in $filas) {
    $c = $linea -split '\|'
    $vector = @(1..1536 | ForEach-Object { [math]::Round($rnd.NextDouble() * 2 - 1, 6) })
    $puntos += @{
        id = [int]$c[0]
        vector = $vector
        payload = @{ cursoId = [int]$c[0]; nombre = $c[1]; categoria = $c[2]; nivel = $c[3]; duracionHoras = [int]$c[4]; activo = $true }
    }
}
$cuerpo = ConvertTo-Json -InputObject @{ points = $puntos } -Depth 6 -Compress
Invoke-RestMethod -Method Put -Uri "http://localhost:6333/collections/cursos_rutaia/points?wait=true" -ContentType "application/json; charset=utf-8" -Body ([System.Text.Encoding]::UTF8.GetBytes($cuerpo)) | Out-Null
Write-Host "Puntos cargados: $($puntos.Count)"