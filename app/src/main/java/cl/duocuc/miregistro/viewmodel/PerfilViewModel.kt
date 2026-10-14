package cl.duocuc.miregistro.viewmodel

import androidx.lifecycle.ViewModel
import cl.duocuc.miregistro.model.Perfil
import cl.duocuc.miregistro.model.PerfilUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// =====================================================================
//  PerfilViewModel.kt  —  LÓGICA DE LA PANTALLA 2
//  ---------------------------------------------------------------------
//  Por ahora los datos están escritos aquí ("datos de ejemplo").
//  Más adelante vendrán de una base de datos o una API, y la pantalla
//  NO tendrá que cambiar: esa es una ventaja de separar con MVVM.
//  Esta pantalla aún NO recibe datos del formulario (no hay navegación).
// =====================================================================

class PerfilViewModel : ViewModel() {

    private val perfilEjemplo = Perfil(
        nombre = "Camila Rojas",
        profesion = "Desarrolladora móvil",
        ciudad = "Santiago, Chile",
        descripcion = "Me gusta crear apps simples y bonitas. Actualmente aprendo " +
            "Jetpack Compose y arquitectura MVVM.",
        seguidores = 128,
        siguiendo = 96,
        publicaciones = 24,
        intereses = listOf("Kotlin", "Compose", "Diseño UI", "Material 3", "Git")
    )

    private val _uiState = MutableStateFlow(PerfilUiState(perfil = perfilEjemplo))
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    /** Botón flotante "Seguir / Siguiendo": suma o resta un seguidor. */
    fun onAlternarSeguir() {
        _uiState.update { estado ->
            val cambio = if (estado.loSigo) -1 else 1
            estado.copy(
                loSigo = !estado.loSigo,
                perfil = estado.perfil.copy(seguidores = estado.perfil.seguidores + cambio)
            )
        }
    }
}
