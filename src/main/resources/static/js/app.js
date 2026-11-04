import { iniciarRegistro } from "./registro.js";
import { cargarCatalogo, iniciarCatalogo } from "./catalogo.js";
import { cargarConsulta, iniciarConsulta } from "./consulta.js";
import { cargarHistorial } from "./historial.js";
import { cargarEstadisticas } from "./estadisticas.js";

const alMostrar = {
  catalogo: cargarCatalogo,
  consulta: cargarConsulta,
  historial: cargarHistorial,
  estadisticas: cargarEstadisticas
};

function mostrarVista(nombre) {
  document.querySelectorAll(".vista").forEach((v) => v.classList.toggle("activa", v.id === nombre));
  document.querySelectorAll("nav button").forEach((b) => b.classList.toggle("activo", b.dataset.vista === nombre));
  if (alMostrar[nombre]) alMostrar[nombre]();
}

document.querySelectorAll("nav button").forEach((boton) => {
  boton.addEventListener("click", () => mostrarVista(boton.dataset.vista));
});

iniciarRegistro();
iniciarCatalogo();
iniciarConsulta();