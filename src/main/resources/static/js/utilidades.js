import { api } from "./api.js";

let cursosCache = null;

export function mostrar(id, texto, tipo = "info") {
  const elemento = document.getElementById(id);
  elemento.textContent = texto;
  elemento.className = `mensaje ${tipo}`;
}

export function escapar(texto) {
  const div = document.createElement("div");
  div.textContent = texto ?? "";
  return div.innerHTML;
}

export function guardarEstudianteId(id) {
  localStorage.setItem("rutaia.estudianteId", String(id));
}

export function obtenerEstudianteId() {
  return localStorage.getItem("rutaia.estudianteId");
}

export async function mapaCursos() {
  if (!cursosCache) {
    const cursos = await api.cursos();
    cursosCache = new Map(cursos.map((c) => [c.id, c]));
  }
  return cursosCache;
}