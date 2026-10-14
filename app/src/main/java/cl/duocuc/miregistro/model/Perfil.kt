package cl.duocuc.miregistro.model

// =====================================================================
//  Perfil.kt  —  MODELO DE DATOS de un usuario (pantalla 2)
//  y PerfilUiState — estado de la pantalla de perfil.
//  ---------------------------------------------------------------------
//  Diferencia importante:
//   - Perfil        = los DATOS del negocio (vendrían de una BD o API).
//   - PerfilUiState = lo que la PANTALLA necesita (datos + estado visual,
//                     como si el usuario presionó "Seguir").
// =====================================================================

data class Perfil(
    val nombre: String,
    val profesion: String,
    val ciudad: String,
    val descripcion: String,
    val seguidores: Int,
    val siguiendo: Int,
    val publicaciones: Int,
    val intereses: List<String>
)

data class PerfilUiState(
    val perfil: Perfil,
    val loSigo: Boolean = false
)
