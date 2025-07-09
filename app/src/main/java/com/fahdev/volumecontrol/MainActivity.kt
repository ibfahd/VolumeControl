package com.fahdev.volumecontrol

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.fahdev.volumecontrol.ui.theme.VolumeControlTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VolumeControlTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    VolumeControlScreen()
                }
            }
        }
    }
}

@Composable
fun VolumeControlScreen() {
    val context = LocalContext.current
    val audioManager = remember {
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    var currentVolume by remember {
        mutableIntStateOf(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC))
    }

    val maxVolume = remember {
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    val adjustVolume = { direction: Int ->
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            direction,
            AudioManager.FLAG_SHOW_UI
        )
        currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // App Title
        Text(
            text = "Volume Control",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // Volume Icon
        Text(
            text = "🔊",
            fontSize = 64.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Volume Display
        Text(
            text = "$currentVolume / $maxVolume",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Volume Progress Bar
        LinearProgressIndicator(
            progress = { currentVolume.toFloat() / maxVolume.toFloat() },
            modifier = Modifier
                .width(300.dp)
                .height(12.dp)
                .padding(bottom = 48.dp),
            color = Color(0xFF4CAF50),
            trackColor = Color(0xFF3d3d3d)
        )

        // Button Container
        Row(
            horizontalArrangement = Arrangement.spacedBy(48.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Volume Down Button
            VolumeButton(
                text = "−",
                enabled = currentVolume > 0,
                onClick = { adjustVolume(AudioManager.ADJUST_LOWER) }
            )

            // Volume Up Button
            VolumeButton(
                text = "+",
                enabled = currentVolume < maxVolume,
                onClick = { adjustVolume(AudioManager.ADJUST_RAISE) }
            )
        }
    }
}

@Composable
fun VolumeButton(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(120.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2d2d2d),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF1a1a1a),
            disabledContentColor = Color(0xFF666666)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp,
            disabledElevation = 0.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VolumeControlPreview() {
    VolumeControlTheme {
        VolumeControlScreen()
    }
}