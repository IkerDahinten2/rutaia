$ErrorActionPreference = "Continue"
[Console]::OutputEncoding = [Text.Encoding]::UTF8
Set-Location "C:\Users\mauri\OneDrive\Desktop\Mau\rutaia"
$utf8 = New-Object System.Text.UTF8Encoding($false)

function Acentos([string]$t) {
    $t.Replace('{a}',[string][char]0xe1).Replace('{e}',[string][char]0xe9).Replace('{i}',[string][char]0xed).Replace('{o}',[string][char]0xf3).Replace('{u}',[string][char]0xfa).Replace('{n}',[string][char]0xf1)
}

$base = "http://localhost:8080/api"
try { Invoke-RestMethod -Uri "$base/health" | Out-Null } catch { throw "La aplicacion no responde en localhost:8080" }

function Llamar([string]$metodo, [string]$ruta, $cuerpo) {
    $p = @{ Method = $metodo; Uri = "$base$ruta"; ContentType = "application/json; charset=utf-8" }
    if ($null -ne $cuerpo) { $p.Body = [Text.Encoding]::UTF8.GetBytes((ConvertTo-Json -InputObject $cuerpo -Compress)) }
    try { return @{ codigo = "OK"; datos = (Invoke-RestMethod @p) } }
    catch { return @{ codigo = [string][int]$_.Exception.Response.StatusCode; datos = $null } }
}

$resultados = New-Object System.Collections.ArrayList

function Caso([string]$id, [string]$desc, [string]$entrada, [string]$esperado, [scriptblock]$regla, [string]$metodo, [string]$ruta, $cuerpo) {
    $r = Llamar $metodo $ruta $cuerpo
    $ok = [bool](& $regla $r)
    $obtenido = $r.codigo
    if ($r.codigo -eq "OK" -and $r.datos -and $r.datos.estado) { $obtenido = "OK / " + $r.datos.estado }
    $null = $resultados.Add([pscustomobject]@{ id = $id; desc = $desc; entrada = $entrada; esperado = $esperado; obtenido = $obtenido; resultado = $(if ($ok) { "Aprobado" } else { "Fallido" }) })
}

$ok200 = { param($r) $r.codigo -eq "OK" }
$respondida = { param($r) $r.codigo -eq "OK" -and $r.datos.estado -eq "Respondida" }
$sinRes = { param($r) $r.codigo -eq "OK" -and $r.datos.estado -eq "Sin resultados" }
$e400 = { param($r) $r.codigo -eq "400" }
$e404 = { param($r) $r.codigo -eq "404" }
$e409 = { param($r) $r.codigo -eq "409" }
$catalogo = { param($r) $r.codigo -eq "OK" -and @($r.datos).Count -gt 0 }

$estudiantes = (Llamar "GET" "/estudiantes" $null).datos
$estId = @($estudiantes)[0].id
$stamp = Get-Date -Format "yyyyMMddHHmmss"

Caso "E01" "Salud de la aplicacion" "GET /api/health" "OK" $ok200 "GET" "/health" $null
Caso "E02" "Registro valido" "estudiante nuevo" "OK" $ok200 "POST" "/estudiantes" @{ nombre = "Prueba QA"; correo = "qa.$stamp@correo.com"; nivel = "Principiante"; areaInteres = "Web" }
Caso "E03" "Correo duplicado" "ana.garcia@correo.com" "409" $e409 "POST" "/estudiantes" @{ nombre = "Duplicado"; correo = "ana.garcia@correo.com"; nivel = "Principiante"; areaInteres = "Web" }
Caso "E04" "Nivel no valido" "Experto" "400" $e400 "POST" "/estudiantes" @{ nombre = "Prueba"; correo = "nivel.$stamp@correo.com"; nivel = "Experto"; areaInteres = "Web" }
Caso "E05" "Duracion de curso en cero" "duracionHoras = 0" "400" $e400 "POST" "/cursos" @{ nombre = "Prueba"; descripcion = "Prueba"; categoria = "Prueba"; nivel = "Intermedio"; duracionHoras = 0 }
Caso "E06" "Catalogo filtrado por nivel" "nivel = Avanzado" "OK" $catalogo "GET" "/catalogo?nivel=Avanzado" $null
Caso "E07" "Calificacion fuera de rango" "puntuacion = 6" "400" $e400 "POST" "/recomendaciones/1/calificacion" @{ puntuacion = 6 }
Caso "E08" "Recomendacion inexistente" "id = 99999" "404" $e404 "POST" "/recomendaciones/99999/calificacion" @{ puntuacion = 3 }

$consultas = @(
    @("C01", "quiero aprender a crear paginas web sin saber programar"),
    @("C02", "necesito consultar y ordenar datos en tablas de una base de datos"),
    @("C03", "quiero entender como funciona la inteligencia artificial sin matematicas complicadas"),
    @("C04", "quiero automatizar tareas repetitivas entre aplicaciones"),
    @("C05", "quiero aprender a programar en Java con clases y objetos"),
    @("C06", "busco un curso sobre APIs REST con Spring Boot"),
    @("C07", "quiero aprender a usar Git para trabajar en equipo")
)
foreach ($q in $consultas) {
    Caso $q[0] "Consulta relacionada con el catalogo" $q[1] "Respondida" $respondida "POST" "/consultas" @{ estudianteId = $estId; pregunta = $q[1] }
}
Caso "C08" "Consulta sin relacion con el catalogo" "recetas italianas" "Sin resultados" $sinRes "POST" "/consultas" @{ estudianteId = $estId; pregunta = "quiero preparar recetas italianas para una cena" }
Caso "C09" "Pregunta vacia" "pregunta vacia" "400" $e400 "POST" "/consultas" @{ estudianteId = $estId; pregunta = "" }
Caso "C10" "Estudiante inexistente" "estudianteId = 99999" "404" $e404 "POST" "/consultas" @{ estudianteId = 99999; pregunta = "quiero aprender a crear paginas web" }

$total = $resultados.Count
$aprob = @($resultados | Where-Object { $_.resultado -eq "Aprobado" }).Count
$filas = ($resultados | ForEach-Object { "| $($_.id) | $($_.desc) | $($_.entrada) | $($_.esperado) | $($_.obtenido) | $($_.resultado) |" }) -join [Environment]::NewLine
$fecha = Get-Date -Format "dd/MM/yyyy HH:mm"

$md = @"
# Documento de pruebas

Fecha de ejecuci{o}n: $fecha

## Entorno

- Java 21.0.13, Spring Boot 4.1.2, PostgreSQL 16, Qdrant 1.20.0
- Aplicaci{o}n en localhost:8080 y base de datos en localhost:5434

## Casos

| ID | Caso | Entrada | Esperado | Obtenido | Resultado |
|---|---|---|---|---|---|
$filas

## Resumen

- Total de casos: $total
- Aprobados: $aprob
- Fallidos: $($total - $aprob)

Los casos C01 a C08 requieren el flujo consulta-rag activo en n8n. Sin ese flujo, las consultas terminan en estado Error y se reportan como fallidas.
"@
[IO.File]::WriteAllText((Join-Path (Get-Location) "docs\documento-pruebas.md"), (Acentos $md), $utf8)
Write-Host "Aprobados: $aprob de $total"