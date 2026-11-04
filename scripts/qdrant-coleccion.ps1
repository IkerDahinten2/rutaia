$ErrorActionPreference = "Stop"
Invoke-RestMethod -Method Put -Uri "http://localhost:6333/collections/cursos_rutaia" -ContentType "application/json" -Body '{"vectors":{"size":1536,"distance":"Cosine"}}'
Invoke-RestMethod -Uri "http://localhost:6333/collections/cursos_rutaia"