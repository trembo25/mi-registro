package cl.duocuc.miregistro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cl.duocuc.miregistro.ui.screens.FormularioScreen
import cl.duocuc.miregistro.ui.screens.PerfilScreen
import cl.duocuc.miregistro.ui.theme.MiRegistroTheme

// =====================================================================
//  MainActivity.kt  —  PUNTO DE ENTRADA DE LA APP
//  ---------------------------------------------------------------------
//  Con Compose NO hay layouts XML: setContent { } recibe directamente
//  las funciones @Composable.
//
//      setContent {
//          Tema {          <- colores / tipografía / formas ("CSS" global)
//              Pantalla()  <- la screen que se muestra
//          }
//      }
//
//  Las dos pantallas TODAVÍA NO SE COMUNICAN (no hay navegación).
//  Para ver una u otra, cambia PANTALLA_INICIAL y vuelve a ejecutar (Run ).
//  También puedes verlas sin ejecutar con los @Preview de cada Screen.
// =====================================================================

/** Pantallas disponibles en esta clase. */
enum class Pantalla { FORMULARIO, PERFIL }

/** <-- CAMBIA ESTE VALOR para elegir qué pantalla abre la app. */
private val PANTALLA_INICIAL = Pantalla.FORMULARIO

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // dibuja de borde a borde; el Scaffold respeta las barras del sistema
        setContent {
            MiRegistroTheme {
                when (PANTALLA_INICIAL) {
                    Pantalla.FORMULARIO -> FormularioScreen()
                    Pantalla.PERFIL -> PerfilScreen()
                }
            }
        }
    }
}