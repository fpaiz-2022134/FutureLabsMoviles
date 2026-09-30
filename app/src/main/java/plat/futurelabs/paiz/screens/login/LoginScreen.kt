package plat.futurelabs.paiz.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import plat.futurelabs.paiz.R
import plat.futurelabs.paiz.ui.theme.FutureLabsMovilesTheme

@Composable
fun LoginScreen(
    onStartClick: ()-> Unit,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier.fillMaxSize()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)

        ) {
            Image(
                painter = painterResource(R.drawable.rickmortylogo),
                contentDescription = "Logo de Rick and Morty",
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = onStartClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Empezar")
            }
        }

        Text(
            text = "Franco Paiz - #25780",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview(){
    FutureLabsMovilesTheme{
        Surface {
            LoginScreen(
                onStartClick = {}
            )
        }
    }
}