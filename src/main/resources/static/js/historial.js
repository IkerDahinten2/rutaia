import { api } from "./api.js";
import { obtenerEstudianteId, escapar } from "./utilidades.js";
import { pintarResultado } from "./consulta.js";

export async function cargarHistorial() {
  const id = obtenerEstudianteId();
  const lista = document.getElementById("listaHistorial");
  const detalle = document.getElementById("detalleHistorial");
  detalle.innerHTML = "";
  if (!id) {
    lista.innerHTML = `<p class="mensaje error">Primero inicia sesiÃ³n en la secciÃ³n Registro.</p>`;
    return;
  }
  try {
    const consultas = await api.historial(id);
    if (consultas.length === 0) {
      lista.innerHTML = "<p>TodavÃ­a no tienes consultas.</p>";
      return;
    }
    lista.innerHTML = consultas.map((c) => `
      <button class="item-historial" data-id="${c.id}">
        <span>${escapar(c.pregunta)}</span>
        <small>${escapar(c.estado)} Â· ${new Date(c.creadoEn).toLocaleString()}</small>
      </button>`).join("");
    lista.querySelectorAll(".item-historial").forEach((boton) => {
      boton.addEventListener("click", async () => {
        try {
          const consulta = await api.obtenerConsulta(boton.dataset.id);
          await pintarResultado(consulta, detalle);
        } catch (error) {
          detalle.innerHTML = `<p class="mensaje error">${escapar(error.message)}</p>`;
        }
      });
    });
  } catch (error) {
    lista.innerHTML = `<p class="mensaje error">${escapar(error.message)}</p>`;
  }
}