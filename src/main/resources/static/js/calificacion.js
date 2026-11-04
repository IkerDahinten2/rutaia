import { api } from "./api.js";

export function mostrarCalificacion(contenedor, recomendacionId) {
  if (!recomendacionId) return;
  contenedor.innerHTML = `
    <form class="calificacion">
      <h4>Califica esta recomendaci\u00f3n</h4>
      <select name="puntuacion" required>
        <option value="5">5 - Muy \u00fatil</option>
        <option value="4">4 - \u00datil</option>
        <option value="3">3 - Regular</option>
        <option value="2">2 - Poco \u00fatil</option>
        <option value="1">1 - No \u00fatil</option>
      </select>
      <input name="comentario" placeholder="Comentario (opcional)">
      <button type="submit">Enviar calificaci\u00f3n</button>
      <p class="mensaje"></p>
    </form>`;
  const formulario = contenedor.querySelector("form");
  formulario.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formulario));
    const aviso = formulario.querySelector(".mensaje");
    try {
      await api.calificar(recomendacionId, {
        puntuacion: Number(datos.puntuacion),
        comentario: datos.comentario || null
      });
      aviso.textContent = "Gracias por tu calificaci\u00f3n.";
      aviso.className = "mensaje ok";
      formulario.querySelector("button").disabled = true;
    } catch (error) {
      aviso.textContent = error.message;
      aviso.className = "mensaje error";
    }
  });
}