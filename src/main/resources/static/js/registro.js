import { api } from "./api.js";
import { mostrar, guardarEstudianteId } from "./utilidades.js";

export function iniciarRegistro() {
  const formRegistro = document.getElementById("formRegistro");
  const formEntrar = document.getElementById("formEntrar");

  formRegistro.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const datos = Object.fromEntries(new FormData(formRegistro));
    try {
      const estudiante = await api.registrarEstudiante(datos);
      guardarEstudianteId(estudiante.id);
      mostrar("mensajeRegistro", `Registro exitoso. Tu ID es ${estudiante.id}.`, "ok");
    } catch (error) {
      mostrar("mensajeRegistro", error.message, "error");
    }
  });

  formEntrar.addEventListener("submit", async (evento) => {
    evento.preventDefault();
    const id = new FormData(formEntrar).get("id");
    try {
      const estudiante = await api.obtenerEstudiante(id);
      guardarEstudianteId(estudiante.id);
      mostrar("mensajeEntrar", `SesiÃ³n iniciada como ${estudiante.nombre}.`, "ok");
    } catch (error) {
      mostrar("mensajeEntrar", error.message, "error");
    }
  });
}