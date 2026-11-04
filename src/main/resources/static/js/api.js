const BASE = "/api";

async function peticion(ruta, opciones = {}) {
  const respuesta = await fetch(BASE + ruta, {
    headers: { "Content-Type": "application/json" },
    ...opciones
  });
  const datos = await respuesta.json().catch(() => ({}));
  if (!respuesta.ok) {
    throw new Error(datos.error || "No se pudo completar la solicitud");
  }
  return datos;
}

function query(parametros) {
  const p = new URLSearchParams();
  Object.entries(parametros).forEach(([clave, valor]) => {
    if (valor) p.set(clave, valor);
  });
  const texto = p.toString();
  return texto ? `?${texto}` : "";
}

export const api = {
  registrarEstudiante: (datos) => peticion("/estudiantes", { method: "POST", body: JSON.stringify(datos) }),
  obtenerEstudiante: (id) => peticion(`/estudiantes/${id}`),
  historial: (id) => peticion(`/estudiantes/${id}/historial`),
  catalogo: (categoria, nivel) => peticion(`/catalogo${query({ categoria, nivel })}`),
  cursos: () => peticion("/cursos"),
  crearConsulta: (datos) => peticion("/consultas", { method: "POST", body: JSON.stringify(datos) }),
  obtenerConsulta: (id) => peticion(`/consultas/${id}`),
  calificar: (recomendacionId, datos) => peticion(`/recomendaciones/${recomendacionId}/calificacion`, { method: "POST", body: JSON.stringify(datos) }),
  estadisticas: () => peticion("/estadisticas")
};