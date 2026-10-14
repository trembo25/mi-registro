package cl.duocuc.miregistro.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duocuc.miregistro.R
import cl.duocuc.miregistro.model.FormularioUiState
import cl.duocuc.miregistro.model.NivelExperiencia
import cl.duocuc.miregistro.ui.components.BotonPrincipal
import cl.duocuc.miregistro.ui.components.BotonSecundario
import cl.duocuc.miregistro.ui.components.CampoContrasena
import cl.duocuc.miregistro.ui.components.CampoTexto
import cl.duocuc.miregistro.ui.components.CasillaConTexto
import cl.duocuc.miregistro.ui.components.GrupoRadio
import cl.duocuc.miregistro.ui.components.ImagenEncabezado
import cl.duocuc.miregistro.ui.components.InterruptorConTexto
import cl.duocuc.miregistro.ui.components.TarjetaSeccion
import cl.duocuc.miregistro.ui.theme.Dimens
import cl.duocuc.miregistro.ui.theme.Exito
import cl.duocuc.miregistro.ui.theme.ExitoFondo
import cl.duocuc.miregistro.ui.theme.MiRegistroTheme
import cl.duocuc.miregistro.viewmodel.FormularioViewModel

// =====================================================================
//  FormularioScreen.kt  —  PANTALLA 1: FORMULARIO DE REGISTRO  (la "V")
//  ---------------------------------------------------------------------
//  Árbol de CONTENEDORES de esta pantalla:
//
//  Scaffold                      <- esqueleto: barra superior + contenido
//   └─ Column (scroll)           <- todo en vertical, con desplazamiento
//       ├─ ImagenEncabezado      <- Box: imagen + degradado + textos apilados
//       └─ Column (padding)      <- zona del formulario
//           ├─ TarjetaSeccion    <- Card "Datos personales" (3 campos)
//           ├─ TarjetaSeccion    <- Card "Preferencias" (radio + switch)
//           ├─ CasillaConTexto   <- Row: checkbox + texto
//           ├─ Row               <- dos botones lado a lado (weight)
//           └─ Card (si éxito)   <- mensaje verde
//
//  La pantalla se divide en dos funciones:
//   1) FormularioScreen  -> CON estado: conoce el ViewModel.
//   2) FormularioContent -> SIN estado: recibe datos y lambdas.
//      Se puede ver en @Preview sin ViewModel.
// =====================================================================

@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel = viewModel()   // se crea una vez y sobrevive a la rotación
) {
    // Cada vez que el StateFlow emite un estado nuevo, la pantalla se redibuja
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FormularioContent(
        uiState = uiState,
        onNombreCambia = viewModel::onNombreCambia,
        onCorreoCambia = viewModel::onCorreoCambia,
        onContrasenaCambia = viewModel::onContrasenaCambia,
        onAlternarVisibilidad = viewModel::onAlternarVisibilidad,
        onNivelSeleccionado = viewModel::onNivelSeleccionado,
        onNoticiasCambia = viewModel::onNoticiasCambia,
        onTerminosCambia = viewModel::onTerminosCambia,
        onRegistrar = viewModel::onRegistrar,
        onLimpiar = viewModel::onLimpiar
    )
}

// TopAppBar todavía está marcada como "experimental" en Material 3:
// @OptIn indica que la usamos sabiendo que su API podría cambiar.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioContent(
    uiState: FormularioUiState,
    onNombreCambia: (String) -> Unit,
    onCorreoCambia: (String) -> Unit,
    onContrasenaCambia: (String) -> Unit,
    onAlternarVisibilidad: () -> Unit,
    onNivelSeleccionado: (NivelExperiencia) -> Unit,
    onNoticiasCambia: (Boolean) -> Unit,
    onTerminosCambia: (Boolean) -> Unit,
    onRegistrar: () -> Unit,
    onLimpiar: () -> Unit
) {
    // SCAFFOLD: "andamio" de la pantalla. Tiene espacios (slots) con nombre:
    //   topBar, bottomBar, floatingActionButton, snackbarHost y el contenido.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_formulario)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        // innerPadding = espacio que ocupan las barras. Si no se aplica,
        // el contenido quedaría DEBAJO de la TopAppBar.

        // COLUMN: apila hijos en VERTICAL (display:flex; flex-direction:column)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // overflow-y: auto
        ) {
            ImagenEncabezado(
                imagen = R.drawable.banner_registro,   // imagen de res/drawable
                descripcion = stringResource(R.string.cd_banner),
                titulo = stringResource(R.string.banner_titulo),
                subtitulo = stringResource(R.string.banner_subtitulo)
            )

            Column(
                modifier = Modifier.padding(Dimens.espacioPantalla),
                verticalArrangement = Arrangement.spacedBy(Dimens.espacioMedio) // gap: 16dp
            ) {
                // ---------- Card 1: datos personales ----------
                TarjetaSeccion(titulo = stringResource(R.string.seccion_datos)) {
                    CampoTexto(
                        valor = uiState.nombre,
                        onValorCambia = onNombreCambia,
                        etiqueta = stringResource(R.string.campo_nombre),
                        icono = R.drawable.ic_persona,
                        error = uiState.errorNombre
                    )
                    CampoTexto(
                        valor = uiState.correo,
                        onValorCambia = onCorreoCambia,
                        etiqueta = stringResource(R.string.campo_correo),
                        icono = R.drawable.ic_correo,
                        error = uiState.errorCorreo,
                        tipoTeclado = KeyboardType.Email   // teclado con @
                    )
                    CampoContrasena(
                        valor = uiState.contrasena,
                        onValorCambia = onContrasenaCambia,
                        etiqueta = stringResource(R.string.campo_contrasena),
                        visible = uiState.contrasenaVisible,
                        onAlternarVisible = onAlternarVisibilidad,
                        error = uiState.errorContrasena
                    )
                }

                // ---------- Card 2: preferencias ----------
                TarjetaSeccion(titulo = stringResource(R.string.seccion_preferencias)) {
                    Text(
                        text = stringResource(R.string.etiqueta_nivel),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    GrupoRadio(
                        opciones = NivelExperiencia.entries,   // todas las opciones del enum
                        seleccionada = uiState.nivel,
                        onSeleccionar = onNivelSeleccionado,
                        textoDe = { it.etiqueta }
                    )
                    InterruptorConTexto(
                        texto = stringResource(R.string.opcion_noticias),
                        activado = uiState.recibirNoticias,
                        onCambio = onNoticiasCambia
                    )
                }

                // ---------- Términos (checkbox) + error ----------
                Column {
                    CasillaConTexto(
                        texto = stringResource(R.string.opcion_terminos),
                        marcado = uiState.aceptaTerminos,
                        onCambio = onTerminosCambia
                    )
                    // ?.let { } = "si no es null, ejecuta el bloque"
                    uiState.errorTerminos?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = Dimens.espacioChico)
                        )
                    }
                }

                // ---------- ROW: botones lado a lado ----------
                // weight(1f) reparte el ancho en partes iguales (flex: 1)
                Row(horizontalArrangement = Arrangement.spacedBy(Dimens.espacioMedio)) {
                    BotonSecundario(
                        texto = stringResource(R.string.btn_limpiar),
                        onClick = onLimpiar,
                        modifier = Modifier.weight(1f)
                    )
                    BotonPrincipal(
                        texto = stringResource(R.string.btn_registrar),
                        onClick = onRegistrar,
                        icono = R.drawable.ic_check,
                        modifier = Modifier.weight(1f)
                    )
                }

                // ---------- Mensaje de éxito (solo si registroExitoso) ----------
                // En Compose se muestra/oculta con un simple "if".
                if (uiState.registroExitoso) {
                    MensajeExito(nombre = uiState.nombre)
                }

                Spacer(Modifier.height(Dimens.espacioGrande))
            }
        }
    }
}

/** Card verde con ícono y texto. Privada: solo se usa en esta pantalla. */
@Composable
private fun MensajeExito(nombre: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ExitoFondo, contentColor = Exito)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.espacioMedio),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Exito,
                modifier = Modifier.size(Dimens.tamanoIcono)
            )
            Text(
                // %1$s en strings.xml se reemplaza por "nombre"
                text = stringResource(R.string.registro_exitoso, nombre),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = Dimens.espacioChico)
            )
        }
    }
}

// ----------------------------- PREVIEWS -----------------------------

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FormularioVacioPreview() {
    MiRegistroTheme {
        FormularioContent(
            uiState = FormularioUiState(),
            onNombreCambia = {}, onCorreoCambia = {}, onContrasenaCambia = {},
            onAlternarVisibilidad = {}, onNivelSeleccionado = {}, onNoticiasCambia = {},
            onTerminosCambia = {}, onRegistrar = {}, onLimpiar = {}
        )
    }
}

@Preview(showBackground = true, heightDp = 1400, name = "Con errores")
@Composable
private fun FormularioConErroresPreview() {
    MiRegistroTheme {
        FormularioContent(
            uiState = FormularioUiState(
                correo = "ana@",
                errorNombre = "El nombre es obligatorio",
                errorCorreo = "Ingresa un correo válido",
                errorContrasena = "Mínimo 6 caracteres",
                errorTerminos = "Debes aceptar los términos"
            ),
            onNombreCambia = {}, onCorreoCambia = {}, onContrasenaCambia = {},
            onAlternarVisibilidad = {}, onNivelSeleccionado = {}, onNoticiasCambia = {},
            onTerminosCambia = {}, onRegistrar = {}, onLimpiar = {}
        )
    }
}
