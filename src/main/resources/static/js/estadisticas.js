import { api } from "./api.js";
import { escapar, mapaCursos } from "./utilidades.js";

export async function cargarEstadisticas() {
  const contenedor = document.getElementById("tarjetasEstadisticas");
  try {
    const e = await api.estadisticas();
    const cursos = await mapaCursos();
    const top = e.cursoMasRecomendado
      ? (cursos.get(e.cursoMasRecomendado)?.nombre ?? `Curso #${e.cursoMasRecomendado}`)
      : "Sin datos";
    const datos = [
      ["Consultas totales", e.totalConsultas],
      ["Respondidas", e.respondidas],
      ["Sin resultados", e.sinResultados],
      ["Promedio de calificaciÃ³n", Number(e.promedioCalificacion).toFixed(2)],
      ["Curso mÃ¡s recomendado", top]
    ];
    contenedor.innerHTML = datos.map(([titulo, valor]) => `
      <article class="tarjeta">
        <h3>${escapar(titulo)}</h3>
        <p class="cifra">${escapar(String(valor))}</p>
      </article>`).join("");
  } catch (error) {
    contenedor.innerHTML = `<p class="mensaje error">${escapar(error.message)}</p>`;
  }
}