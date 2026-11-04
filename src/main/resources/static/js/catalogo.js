import { api } from "./api.js";
import { escapar } from "./utilidades.js";

export function iniciarCatalogo() {
  document.getElementById("filtroCategoria").addEventListener("change", cargarCatalogo);
  document.getElementById("filtroNivel").addEventListener("change", cargarCatalogo);
}

async function llenarCategorias() {
  const select = document.getElementById("filtroCategoria");
  if (select.options.length > 1) return;
  const cursos = await api.catalogo("", "");
  [...new Set(cursos.map((c) => c.categoria))].sort().forEach((categoria) => {
    select.add(new Option(categoria, categoria));
  });
}

function pintar(cursos) {
  const lista = document.getElementById("listaCursos");
  if (cursos.length === 0) {
    lista.innerHTML = "<p>No hay cursos con esos filtros.</p>";
    return;
  }
  lista.innerHTML = cursos.map((c) => `
    <article class="tarjeta">
      <h3>${escapar(c.nombre)}</h3>
      <p>${escapar(c.descripcion)}</p>
      <p><strong>CategorÃ­a:</strong> ${escapar(c.categoria)}</p>
      <p><strong>Nivel:</strong> ${escapar(c.nivel)} Â· ${c.duracionHoras} h</p>
    </article>`).join("");
}

export async function cargarCatalogo() {
  const categoria = document.getElementById("filtroCategoria").value;
  const nivel = document.getElementById("filtroNivel").value;
  try {
    await llenarCategorias();
    pintar(await api.catalogo(categoria, nivel));
  } catch (error) {
    document.getElementById("listaCursos").innerHTML = `<p class="mensaje error">${escapar(error.message)}</p>`;
  }
}