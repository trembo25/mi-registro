package cl.duocuc.miregistro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.miregistro.model.FormularioUiState
import cl.duocuc.miregistro.model.NivelExperiencia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// =====================================================================
//  FormularioViewModel.kt  —  LÓGICA DE LA PANTALLA 1  (la "VM" de MVVM)
//  ---------------------------------------------------------------------
//  - Guarda el estado y sobrevive a la rotación (lo escrito no se pierde).
//  - Expone el estado en SOLO LECTURA (StateFlow).
//  - Recibe los eventos de la vista con funciones "on...".
//  - Valida el formulario (la vista NO decide si un correo es válido).
//
//  Regla: el ViewModel NUNCA importa nada de Compose (ni Text ni Color).
// =====================================================================

class FormularioViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioUiState())       // privado y mutable
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()  // público, solo lectura

    // ----- Eventos de los campos: cada letra escrita llega aquí -----
    // Al escribir, se borra el error de ese campo y el mensaje de éxito.

    fun onNombreCambia(valor: String) {
        _uiState.update { it.copy(nombre = valor, errorNombre = null, registroExitoso = false) }
    }

    fun onCorreoCambia(valor: String) {
        _uiState.update { it.copy(correo = valor.trim(), errorCorreo = null, registroExitoso = false) }
    }

    fun onContrasenaCambia(valor: String) {
        _uiState.update { it.copy(contrasena = valor, errorContrasena = null, registroExitoso = false) }
    }

    fun onAlternarVisibilidad() {
        _uiState.update { it.copy(contrasenaVisible = !it.contrasenaVisible) }
    }

    // ----- Eventos de los controles de selección -----

    fun onNivelSeleccionado(nivel: NivelExperiencia) {
        _uiState.update { it.copy(nivel = nivel) }
    }

    fun onNoticiasCambia(valor: Boolean) {
        _uiState.update { it.copy(recibirNoticias = valor) }
    }

    fun onTerminosCambia(valor: Boolean) {
        _uiState.update { it.copy(aceptaTerminos = valor, errorTerminos = null) }
    }

    // ----- Botones -----

    /** Botón "Registrarme": valida y, si todo está bien, marca éxito. */
    fun onRegistrar() {
        val estado = _uiState.value

        val errorNombre = if (estado.nombre.isBlank()) "El nombre es obligatorio" else null
        val errorCorreo = if (!estado.correo.contains("@") || !estado.correo.contains("."))
            "Ingresa un correo válido" else null
        val errorContrasena = if (estado.contrasena.length < 6)
            "Mínimo 6 caracteres" else null
        val errorTerminos = if (!estado.aceptaTerminos)
            "Debes aceptar los términos" else null

        val hayErrores = listOf(errorNombre, errorCorreo, errorContrasena, errorTerminos)
            .any { it != null }

        _uiState.update {
            it.copy(
                errorNombre = errorNombre,
                errorCorreo = errorCorreo,
                errorContrasena = errorContrasena,
                errorTerminos = errorTerminos,
                registroExitoso = !hayErrores
            )
        }
    }

    /** Botón "Limpiar": vuelve al estado inicial. */
    fun onLimpiar() {
        _uiState.value = FormularioUiState()
    }
}
