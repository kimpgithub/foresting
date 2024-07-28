package com.example.san_lim.screens.home

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.san_lim.R // 추가된 import 문

//HomeScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f) // Adjusted to fill available space
                .padding(bottom = 16.dp)
                .clickable { navController.navigate("select_screen") },
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFD0F0C0)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.home_hue_select),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(alpha = 0.3f) // Set transparency
                )
                Text(
                    text = "휴양림 추천",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black // Adjust text color for better visibility
                )
            }
        }

        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        QuickAccessGrid(navController, modifier = Modifier.weight(1f))
    }
}

@Composable
fun QuickAccessItem(
    title: String,
    backgroundColor: Color,
    navController: NavController,
    destination: String,
    isExternalLink: Boolean,
    imageResourceId: Int, // Add this parameter
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current // Get the context

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                if (isExternalLink) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(destination))
                    context.startActivity(intent)
                } else {
                    navController.navigate(destination)
                }
            },
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Add the background image with transparency
            Image(
                painter = painterResource(id = imageResourceId), // Use the image resource parameter
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.2f) // Set transparency
            )
            // Add the text on top of the image
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black // Adjust text color for better visibility
            )
        }
    }
}

@Composable
fun QuickAccessGrid(navController: NavController, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickAccessItem(
                title = "산림청",
                backgroundColor = Color(0xFFA7E2F7),
                navController = navController,
                destination = "https://www.forest.go.kr",
                isExternalLink = true,
                imageResourceId = R.drawable.home_sanlim, // Replace with your image resource
                modifier = Modifier.weight(1f)
            )
            QuickAccessItem(
                title = "트레킹 코스",
                backgroundColor = Color(0xFFF3B0C3),
                navController = navController,
                destination = "guide_screen",
                isExternalLink = false,
                imageResourceId = R.drawable.home_sanlim, // Replace with your image resource
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickAccessItem(
                title = "휴양림 정보",
                backgroundColor = Color(0xFFE1D5F0),
                navController = navController,
                destination = "trail_recommendation_screen",
                isExternalLink = false,
                imageResourceId = R.drawable.home_sanlim, // Replace with your image resource
                modifier = Modifier.weight(1f)
            )
            QuickAccessItem(
                title = "봉사 알림",
                backgroundColor = Color(0xFFFFF0C2),
                navController = navController,
                destination = "https://www.1365.go.kr/vols/1572247904127/partcptn/timeCptn.do",
                isExternalLink = true,
                imageResourceId = R.drawable.home_boongsa, // Replace with your image resource
                modifier = Modifier.weight(1f)
            )
        }
    }
}
