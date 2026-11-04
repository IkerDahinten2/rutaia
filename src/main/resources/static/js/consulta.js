import { api } from "./api.js";
import { mostrar, obtenerEstudianteId, escapar, mapaCursos } from "./utilidades.js";
import { mostrarCalificacion } from "./calificacion.js";

export function iniciarConsulta() {
  document.getElementById("formConsulta").addEventListener("submit", enviarConsulta);
}

export function cargarConsulta() {
  const id = obtenerEstudianteId();
  document.getElementById("avisoSesion").textContent = id
    ? `Consultando como estudiante #${id}`
    : "Primero reg\u00edstrate o entra con tu ID en la secci\u00f3n Registro.";
  document.querySelector("#formConsulta button").disabled = !id;
}

async function enviarConsulta(evento) {
  evento.preventDefault();
  const estudianteId = obtenerEstudianteId();
  const pregunta = new FormData(evento.target).get("pregunta").trim();
  const resultado = document.getElementById("resultadoConsulta");
  if (!estudianteId) {
    mostrar("mensajeConsulta", "Primero inicia sesi\u00f3n.", "error");
    return;
  }
  if (!pregunta) {
    mostrar("mensajeConsulta", "Escribe tu necesidad de aprendizaje.", "error");
    return;
  }
  resultado.innerHTML = "";
  mostrar("mensajeConsulta", "Buscando cursos...", "info");
  try {
    const consulta = await api.crearConsulta({ estudianteId: Number(estudianteId), pregunta });
    mostrar("mensajeConsulta", "", "info");
    await pintarResultado(consulta, resultado);
  } catch (error) {
    mostrar("mensajeConsulta", error.message, "error");
  }
}

export async function pintarResultado(consulta, contenedor) {
  if (consulta.estado !== "Respondida") {
    const explicacion = consulta.estado === "Sin resultados"
      ? "No encontramos cursos que se ajusten a tu consulta."
      : "Hubo un problema al procesar tu consulta. Intenta de nuevo.";
    contenedor.innerHTML = `<p class="mensaje info">Estado: ${escapar(consulta.estado)}. ${explicacion}</p>`;
    return;
  }
  const cursos = await mapaCursos();
  const fuentes = consulta.fuentes.map((f) => {
    const curso = cursos.get(f.cursoId);
    const nombre = curso ? curso.nombre : `Curso #${f.cursoId}`;
    return `<li><strong>${escapar(nombre)}</strong> \u00b7 similitud ${f.similitud}</li>`;
  }).join("");
  contenedor.innerHTML = `
    <div class="respuesta">
      <h3>Recomendaci\u00f3n</h3>
      <p>${escapar(consulta.respuesta)}</p>
      <h4>Cursos usados como fuente</h4>
      <ul>${fuentes}</ul>
    </div>
    <div class="bloque-calificacion"></div>`;
  mostrarCalificacion(contenedor.querySelector(".bloque-calificacion"), consulta.recomendacionId);
}