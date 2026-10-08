package plat.futurelabs.paiz.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ErrorLayout(
    onRetryClick: ()-> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.CenterVertically
        )
    ){
        Icon(
            imageVector = Icons.Outlined.ErrorOutline,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error
        )

        Text(
            text = "Error al obtener la información.\nIntenta de nuevo",
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )

        OutlinedButton(
            onClick = onRetryClick
        ) {
            Text(
                text = "Reintentar"
            )
        }
    }
}
