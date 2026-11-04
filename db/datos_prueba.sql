INSERT INTO estudiante (nombre, correo, nivel, area_interes) VALUES
('Ana García', 'ana.garcia@correo.com', 'Principiante', 'Desarrollo web'),
('Luis Pérez', 'luis.perez@correo.com', 'Intermedio', 'Bases de datos'),
('María López', 'maria.lopez@correo.com', 'Avanzado', 'Inteligencia artificial'),
('Carlos Ruiz', 'carlos.ruiz@correo.com', 'Principiante', 'Automatización'),
('Sofía Morales', 'sofia.morales@correo.com', 'Intermedio', 'Programación'),
('Diego Castillo', 'diego.castillo@correo.com', 'Avanzado', 'Desarrollo web'),
('Laura Méndez', 'laura.mendez@correo.com', 'Principiante', 'Bases de datos'),
('Jorge Ramírez', 'jorge.ramirez@correo.com', 'Intermedio', 'Programación'),
('Paula Soto', 'paula.soto@correo.com', 'Avanzado', 'Inteligencia artificial'),
('Andrés Vega', 'andres.vega@correo.com', 'Principiante', 'Automatización')
ON CONFLICT (correo) DO NOTHING;

INSERT INTO curso (nombre, descripcion, categoria, nivel, duracion_horas) VALUES
('Fundamentos de programación', 'Aprende lógica, variables, condicionales y ciclos usando Python. Ideal si nunca has programado.', 'Programación', 'Básico', 30),
('Programación orientada a objetos con Java', 'Clases, herencia, polimorfismo y buenas prácticas para construir aplicaciones organizadas.', 'Programación', 'Intermedio', 40),
('Estructuras de datos y algoritmos', 'Listas, pilas, colas, árboles y análisis de complejidad para resolver problemas de forma eficiente.', 'Programación', 'Avanzado', 50),
('Control de versiones con Git', 'Crea repositorios, ramas y commits, y resuelve conflictos en proyectos reales.', 'Programación', 'Básico', 12),
('Diseño de páginas con HTML y CSS', 'Estructura, estilos, diseño responsivo y maquetación de sitios web desde cero.', 'Desarrollo web', 'Básico', 25),
('JavaScript para la web', 'Manipulación del DOM, eventos y consumo de APIs con fetch.', 'Desarrollo web', 'Intermedio', 35),
('Desarrollo de APIs REST con Spring Boot', 'Diseño de endpoints, validaciones, persistencia y documentación con Swagger.', 'Desarrollo web', 'Avanzado', 45),
('Frameworks de frontend modernos', 'Componentes, estado y enrutamiento para construir interfaces interactivas.', 'Desarrollo web', 'Intermedio', 40),
('Introducción a bases de datos relacionales', 'Tablas, llaves, relaciones y consultas básicas con SQL.', 'Bases de datos', 'Básico', 20),
('Modelado de datos y normalización', 'Diagramas entidad-relación, normalización y diseño de esquemas.', 'Bases de datos', 'Intermedio', 25),
('PostgreSQL avanzado', 'Índices, transacciones, vistas, funciones y optimización de consultas.', 'Bases de datos', 'Avanzado', 35),
('Bases de datos vectoriales', 'Búsqueda por similitud, embeddings y uso de Qdrant en aplicaciones.', 'Bases de datos', 'Avanzado', 20),
('Introducción a la inteligencia artificial', 'Conceptos básicos, tipos de aprendizaje y casos de uso en la vida real.', 'Inteligencia artificial', 'Básico', 15),
('Procesamiento de lenguaje natural', 'Tokenización, embeddings y clasificación de texto con modelos modernos.', 'Inteligencia artificial', 'Intermedio', 30),
('Sistemas de recomendación', 'Filtrado colaborativo, enfoque basado en contenido y generación aumentada por recuperación.', 'Inteligencia artificial', 'Avanzado', 40),
('Herramientas de IA generativa para productividad', 'Uso ético y efectivo de asistentes de IA para redactar, analizar y automatizar tareas.', 'Inteligencia artificial', 'Básico', 10),
('Automatización de procesos con n8n', 'Flujos, nodos, webhooks y conexión entre aplicaciones sin programar.', 'Automatización', 'Básico', 16),
('Integración de APIs y webhooks', 'Consumo de servicios externos, autenticación y manejo de errores.', 'Automatización', 'Intermedio', 22),
('Scripting con PowerShell', 'Automatiza tareas de Windows con scripts, variables y ciclos.', 'Automatización', 'Intermedio', 18),
('Arquitectura de sistemas distribuidos', 'Comunicación entre servicios, colas, caché y tolerancia a fallos.', 'Automatización', 'Avanzado', 40);

INSERT INTO consulta (estudiante_id, pregunta, estado)
SELECT e.id, v.pregunta, v.estado
FROM (VALUES
('ana.garcia@correo.com', 'quiero aprender a crear páginas web sin saber programar', 'Respondida'),
('luis.perez@correo.com', 'necesito consultar y ordenar datos en tablas de una base de datos', 'Respondida'),
('maria.lopez@correo.com', 'quiero entender cómo funciona la inteligencia artificial sin matemáticas complicadas', 'Respondida'),
('carlos.ruiz@correo.com', 'quiero automatizar tareas repetitivas entre aplicaciones', 'Respondida'),
('sofia.morales@correo.com', 'quiero aprender a programar en Java con clases y objetos', 'Respondida'),
('diego.castillo@correo.com', 'busco un curso sobre APIs REST con Spring Boot', 'Respondida'),
('laura.mendez@correo.com', 'quiero aprender a usar Git para trabajar en equipo', 'Respondida'),
('jorge.ramirez@correo.com', 'quiero preparar recetas italianas para una cena', 'Sin resultados'),
('paula.soto@correo.com', 'quiero crear sistemas de recomendación con IA', 'Respondida'),
('andres.vega@correo.com', 'quiero dar estilo a una página web con CSS', 'Respondida')
) AS v(correo, pregunta, estado)
JOIN estudiante e ON e.correo = v.correo;

INSERT INTO recomendacion (consulta_id, respuesta)
SELECT q.id, v.respuesta
FROM (VALUES
('quiero aprender a crear páginas web sin saber programar', 'Para empezar sin experiencia, te recomendamos el curso de Diseño de páginas con HTML y CSS. Después puedes avanzar a JavaScript para la web.'),
('necesito consultar y ordenar datos en tablas de una base de datos', 'El curso de Introducción a bases de datos relacionales cubre tablas, llaves y consultas SQL, que es lo que necesitas para empezar.'),
('quiero entender cómo funciona la inteligencia artificial sin matemáticas complicadas', 'La Introducción a la inteligencia artificial explica los conceptos básicos con ejemplos de la vida diaria, sin requisitos técnicos avanzados.'),
('quiero automatizar tareas repetitivas entre aplicaciones', 'Automatización de procesos con n8n te permite conectar aplicaciones sin programar. Si también trabajas en Windows, Scripting con PowerShell es un buen complemento.'),
('quiero aprender a programar en Java con clases y objetos', 'El curso de Programación orientada a objetos con Java cubre clases, herencia y polimorfismo, con buenas prácticas de diseño.'),
('busco un curso sobre APIs REST con Spring Boot', 'Desarrollo de APIs REST con Spring Boot cubre endpoints, validaciones, persistencia y documentación. Integración de APIs y webhooks complementa el tema.'),
('quiero aprender a usar Git para trabajar en equipo', 'El curso de Control de versiones con Git te enseña a crear ramas, hacer commits y resolver conflictos al trabajar en equipo.'),
('quiero crear sistemas de recomendación con IA', 'Sistemas de recomendación explica el filtrado colaborativo y la generación aumentada por recuperación, que es la base de un sistema como RutaIA.'),
('quiero dar estilo a una página web con CSS', 'Diseño de páginas con HTML y CSS cubre estilos, diseño responsivo y maquetación desde cero.')
) AS v(pregunta, respuesta)
JOIN consulta q ON q.pregunta = v.pregunta;

INSERT INTO fuente (recomendacion_id, curso_id, similitud)
SELECT r.id, c.id, v.similitud
FROM (VALUES
('quiero aprender a crear páginas web sin saber programar', 'Diseño de páginas con HTML y CSS', 0.92314),
('quiero aprender a crear páginas web sin saber programar', 'JavaScript para la web', 0.81047),
('necesito consultar y ordenar datos en tablas de una base de datos', 'Introducción a bases de datos relacionales', 0.89561),
('necesito consultar y ordenar datos en tablas de una base de datos', 'Modelado de datos y normalización', 0.71230),
('quiero entender cómo funciona la inteligencia artificial sin matemáticas complicadas', 'Introducción a la inteligencia artificial', 0.93120),
('quiero entender cómo funciona la inteligencia artificial sin matemáticas complicadas', 'Herramientas de IA generativa para productividad', 0.74505),
('quiero automatizar tareas repetitivas entre aplicaciones', 'Automatización de procesos con n8n', 0.90877),
('quiero automatizar tareas repetitivas entre aplicaciones', 'Scripting con PowerShell', 0.76642),
('quiero automatizar tareas repetitivas entre aplicaciones', 'Integración de APIs y webhooks', 0.68123),
('quiero aprender a programar en Java con clases y objetos', 'Programación orientada a objetos con Java', 0.94231),
('quiero aprender a programar en Java con clases y objetos', 'Fundamentos de programación', 0.69012),
('busco un curso sobre APIs REST con Spring Boot', 'Desarrollo de APIs REST con Spring Boot', 0.95678),
('busco un curso sobre APIs REST con Spring Boot', 'Integración de APIs y webhooks', 0.72341),
('quiero aprender a usar Git para trabajar en equipo', 'Control de versiones con Git', 0.93987),
('quiero crear sistemas de recomendación con IA', 'Sistemas de recomendación', 0.94876),
('quiero crear sistemas de recomendación con IA', 'Bases de datos vectoriales', 0.70125),
('quiero crear sistemas de recomendación con IA', 'Procesamiento de lenguaje natural', 0.66540),
('quiero dar estilo a una página web con CSS', 'Diseño de páginas con HTML y CSS', 0.96012)
) AS v(pregunta, curso, similitud)
JOIN consulta q ON q.pregunta = v.pregunta
JOIN recomendacion r ON r.consulta_id = q.id
JOIN curso c ON c.nombre = v.curso;

INSERT INTO calificacion (recomendacion_id, puntuacion, comentario)
SELECT r.id, v.puntuacion, v.comentario
FROM (VALUES
('quiero aprender a crear páginas web sin saber programar', 5, 'Muy claro para empezar'),
('necesito consultar y ordenar datos en tablas de una base de datos', 4, 'Me sirvió para repasar SQL'),
('quiero entender cómo funciona la inteligencia artificial sin matemáticas complicadas', 5, 'Explicación sencilla y útil'),
('quiero aprender a programar en Java con clases y objetos', 4, 'Buenos ejemplos de clases'),
('busco un curso sobre APIs REST con Spring Boot', 5, 'Justo lo que buscaba')
) AS v(pregunta, puntuacion, comentario)
JOIN consulta q ON q.pregunta = v.pregunta
JOIN recomendacion r ON r.consulta_id = q.id;