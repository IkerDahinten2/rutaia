$ErrorActionPreference = "Stop"
[Console]::OutputEncoding = [Text.Encoding]::UTF8
Set-Location "C:\Users\mauri\OneDrive\Desktop\Mau\rutaia"
$utf8 = New-Object System.Text.UTF8Encoding($false)

function Acentos([string]$t) {
    $t.Replace('{a}',[string][char]0xe1).Replace('{e}',[string][char]0xe9).Replace('{i}',[string][char]0xed).Replace('{o}',[string][char]0xf3).Replace('{u}',[string][char]0xfa).Replace('{n}',[string][char]0xf1)
}

$key = $env:OPENROUTER_API_KEY
if (-not $key) { throw "Define OPENROUTER_API_KEY" }
$cabeceras = @{ Authorization = "Bearer $key" }

function Vector([string]$texto) {
    $cuerpo = [Text.Encoding]::UTF8.GetBytes((ConvertTo-Json -InputObject @{ model = "openai/text-embedding-3-small"; input = $texto } -Compress))
    $r = Invoke-RestMethod -Method Post -Uri "https://openrouter.ai/api/v1/embeddings" -Headers $cabeceras -ContentType "application/json; charset=utf-8" -Body $cuerpo
    return ,([double[]]$r.data[0].embedding)
}

function Similitud([double[]]$a, [double[]]$b) {
    $p = 0.0; $na = 0.0; $nb = 0.0
    for ($i = 0; $i -lt $a.Length; $i++) { $p += $a[$i] * $b[$i]; $na += $a[$i] * $a[$i]; $nb += $b[$i] * $b[$i] }
    return $p / [math]::Sqrt($na * $nb)
}

$filas = docker exec rutaia-postgres psql -U rutaia -d rutaia -At -F "|" -c "SELECT id, nombre, descripcion, categoria, nivel, duracion_horas FROM curso ORDER BY id"
$cursos = @()
foreach ($f in $filas) {
    $c = $f -split '\|', 6
    $cursos += [pscustomobject]@{ id = [int]$c[0]; nombre = $c[1]; categoria = $c[3]; nivel = $c[4]; duracion = [int]$c[5]; vector = (Vector ($c[1] + ". " + $c[2])) }
}

$consultas = @(
    @{ id = "C01"; texto = "quiero aprender a crear paginas web sin saber programar"; relevante = $true },
    @{ id = "C02"; texto = "necesito consultar y ordenar datos en tablas de una base de datos"; relevante = $true },
    @{ id = "C03"; texto = "quiero entender como funciona la inteligencia artificial sin matematicas complicadas"; relevante = $true },
    @{ id = "C04"; texto = "quiero automatizar tareas repetitivas entre aplicaciones"; relevante = $true },
    @{ id = "C05"; texto = "quiero aprender a programar en Java con clases y objetos"; relevante = $true },
    @{ id = "C06"; texto = "busco un curso sobre APIs REST con Spring Boot"; relevante = $true },
    @{ id = "C07"; texto = "quiero aprender a usar Git para trabajar en equipo"; relevante = $true },
    @{ id = "C08"; texto = "quiero preparar recetas italianas para una cena"; relevante = $false }
)

$resultados = @()
foreach ($q in $consultas) {
    $v = Vector $q.texto
    $mejor = $cursos | ForEach-Object { [pscustomobject]@{ nombre = $_.nombre; score = [math]::Round((Similitud $v $_.vector), 4) } } | Sort-Object score -Descending | Select-Object -First 1
    $resultados += [pscustomobject]@{ id = $q.id; texto = $q.texto; curso = $mejor.nombre; score = $mejor.score; relevante = $q.relevante }
}

$minRel = ($resultados | Where-Object { $_.relevante } | Measure-Object -Property score -Minimum).Minimum
$maxIrr = ($resultados | Where-Object { -not $_.relevante } | Measure-Object -Property score -Maximum).Maximum
$umbral = [math]::Round(($minRel + $maxIrr) / 2, 2)
$separa = $minRel -gt $maxIrr

$puntos = @()
foreach ($c in $cursos) {
    $puntos += @{ id = $c.id; vector = $c.vector; payload = @{ cursoId = $c.id; nombre = $c.nombre; categoria = $c.categoria; nivel = $c.nivel; duracionHoras = $c.duracion; activo = $true } }
}
$cuerpo = [Text.Encoding]::UTF8.GetBytes((ConvertTo-Json -InputObject @{ points = $puntos } -Depth 6 -Compress))
Invoke-RestMethod -Method Put -Uri "http://localhost:6333/collections/cursos_rutaia/points?wait=true" -ContentType "application/json; charset=utf-8" -Body $cuerpo | Out-Null

$tabla = ($resultados | ForEach-Object { "| $($_.id) | $($_.texto) | $($_.curso) | $($_.score) | $(if ($_.relevante) { 'Si' } else { 'No' }) |" }) -join [Environment]::NewLine
$conclusion = if ($separa) { "Las consultas relevantes quedan por encima de la consulta sin relaci{o}n, por lo que el umbral separa ambos grupos." } else { "Advertencia: los grupos se traslapan. Revisa las consultas antes de fijar el umbral." }

$md = @"
# Umbral de similitud

## M{e}todo

Se generaron embeddings con openai/text-embedding-3-small para los 20 cursos y para 8 consultas de prueba (7 relacionadas con el cat{a}logo y 1 sin relaci{o}n). La similitud se calcul{o} con coseno y se registra el curso con mayor similitud para cada consulta.

Los vectores de los cursos se cargaron en la colecci{o}n cursos_rutaia de Qdrant, reemplazando los vectores aleatorios de prueba.

## Resultados

| ID | Consulta | Curso m{a}s parecido | Similitud | Relevante |
|---|---|---|---|---|
$tabla

## Decisi{o}n

- Menor similitud entre consultas relevantes: $minRel
- Mayor similitud en la consulta sin relaci{o}n: $maxIrr
- Umbral elegido (punto medio): $umbral

$conclusion

Las consultas con similitud menor al umbral no se env{i}an al modelo y terminan en estado Sin resultados.
"@
[IO.File]::WriteAllText((Join-Path (Get-Location) "docs\umbral.md"), (Acentos $md), $utf8)
Write-Host "Umbral elegido: $umbral"