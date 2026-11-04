CREATE TABLE IF NOT EXISTS estudiante (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(160) NOT NULL UNIQUE,
    nivel VARCHAR(20) NOT NULL CHECK (nivel IN ('Principiante', 'Intermedio', 'Avanzado')),
    area_interes VARCHAR(120),
    creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS curso (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(160) NOT NULL,
    descripcion TEXT NOT NULL,
    categoria VARCHAR(80) NOT NULL,
    nivel VARCHAR(20) NOT NULL CHECK (nivel IN (U&'B\00e1sico', 'Intermedio', 'Avanzado')),
    duracion_horas INTEGER NOT NULL CHECK (duracion_horas > 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS consulta (
    id BIGSERIAL PRIMARY KEY,
    estudiante_id BIGINT NOT NULL REFERENCES estudiante(id) ON DELETE CASCADE,
    pregunta TEXT NOT NULL CHECK (length(trim(pregunta)) > 0),
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente' CHECK (estado IN ('Pendiente', 'Respondida', 'Sin resultados', 'Error')),
    creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS recomendacion (
    id BIGSERIAL PRIMARY KEY,
    consulta_id BIGINT NOT NULL UNIQUE REFERENCES consulta(id) ON DELETE CASCADE,
    respuesta TEXT NOT NULL,
    creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS fuente (
    id BIGSERIAL PRIMARY KEY,
    recomendacion_id BIGINT NOT NULL REFERENCES recomendacion(id) ON DELETE CASCADE,
    curso_id BIGINT NOT NULL REFERENCES curso(id),
    similitud NUMERIC(6,5) NOT NULL CHECK (similitud BETWEEN 0 AND 1),
    UNIQUE (recomendacion_id, curso_id)
);

CREATE TABLE IF NOT EXISTS calificacion (
    id BIGSERIAL PRIMARY KEY,
    recomendacion_id BIGINT NOT NULL UNIQUE REFERENCES recomendacion(id) ON DELETE CASCADE,
    puntuacion INTEGER NOT NULL CHECK (puntuacion BETWEEN 1 AND 5),
    comentario TEXT,
    creado_en TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_consulta_estudiante ON consulta (estudiante_id);
CREATE INDEX IF NOT EXISTS idx_consulta_estado ON consulta (estado);
CREATE INDEX IF NOT EXISTS idx_curso_activo ON curso (activo);
CREATE INDEX IF NOT EXISTS idx_fuente_curso ON fuente (curso_id);