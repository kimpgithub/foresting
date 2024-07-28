package com.example.san_lim.screens.map

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.san_lim.utils.getLocationFromImageUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVisitScreen(navController: NavController, lodgeName: String) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var note by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            imageBitmap = bitmap?.asImageBitmap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "방문 기록 등록",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Box(
            modifier = Modifier
                .size(320.dp)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.3f))
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            imageBitmap?.let {
                Image(bitmap = it, contentDescription = null, modifier = Modifier.fillMaxSize())
            } ?: run {
                Text(
                    text = "이미지 등록",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("방문 메모") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, bottom = 24.dp)
        )

        Button(
            onClick = {
                imageUri?.let { uri ->
                    CoroutineScope(Dispatchers.IO).launch {
                        registerVisit(context, uri, lodgeName, note)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("방문 등록하기", fontSize = 18.sp)
        }
    }
}

suspend fun registerVisit(context: Context, imageUri: Uri, lodgeName: String, note: String) {
    val imageLocation = getLocationFromImageUri(context, imageUri)
    val lodgeLocation = getLodgeLocation(context, lodgeName)
    if (imageLocation != null && lodgeLocation != null) {
        val distance = FloatArray(1)
        android.location.Location.distanceBetween(
            imageLocation.latitude, imageLocation.longitude,
            lodgeLocation.latitude, lodgeLocation.longitude,
            distance
        )
        if (distance[0] < 500) { // 500 meters threshold
            withContext(Dispatchers.Main) {
                // Record visit
                // Save to Firestore or local database
                // For example:
                // saveVisitToFirestore(lodgeName, imageLocation, note)
                Toast.makeText(context, "Visit recorded successfully", Toast.LENGTH_SHORT).show()
            }
        } else {
            withContext(Dispatchers.Main) {
                // Show error message
                // For example:
                Toast.makeText(context, "Location does not match", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        withContext(Dispatchers.Main) {
            // Show error message
            // For example:
            Toast.makeText(context, "Unable to get location", Toast.LENGTH_SHORT).show()
        }
    }
}

data class Location(val latitude: Double, val longitude: Double)

fun getLodgeLocation(context: Context, lodgeName: String): Location? {
    val lodges = loadHueyanglimData(context)
    val lodge = lodges.find { it.name == lodgeName }
    return lodge?.let { Location(it.latitude, it.longitude) }
}
