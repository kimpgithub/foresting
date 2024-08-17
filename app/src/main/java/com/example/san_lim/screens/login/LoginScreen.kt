package com.example.san_lim.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.san_lim.loginUser
import com.example.san_lim.signUpUser
import kotlinx.coroutines.delay
import com.example.san_lim.R
import com.example.san_lim.ui.theme.ColorPalette

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("test@intel.com") }
    var password by remember { mutableStateOf("123456") }
    var isLogin by remember { mutableStateOf(true) }

    // val backgroundPainter: Painter = painterResource(id = R.drawable.background) // 배경 이미지 로드
    val treePainter: Painter = painterResource(id = R.drawable.tree) // tree.jpg 이미지를 로드
    val logoPainter: Painter = painterResource(id = R.drawable.korea_forest_logo) // 로고 이미지 로드

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorPalette.softWhite) // 배경색 변경
    ) {
        /*
        Image(
            painter = backgroundPainter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        */

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // 로고 이미지 추가
            Image(
                painter = logoPainter,
                contentDescription = null,
                modifier = Modifier.size(120.dp) // 로고 이미지 크기 조절
            )

            Spacer(modifier = Modifier.height(20.dp))
            // Row를 사용하여 "Foresting" 텍스트와 양쪽에 이미지를 배치
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = treePainter,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Foresting",
                    fontSize = 32.sp,
                    color = ColorPalette.earthyDarkMoss,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = treePainter,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = "우리 산림의 재미를 더하다",
                fontSize = 14.sp,
                color = ColorPalette.lightCharcoal
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(160.dp))

            Text(
                text = "로그인",
                color = ColorPalette.darkCharcoal,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("이메일") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("비밀번호") },
                visualTransformation = PasswordVisualTransformation(), // 비밀번호 필드에 변환기 추가
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (isLogin) loginUser(email, password, navController) else signUpUser(email, password, navController) },
                modifier = Modifier
                    .width(280.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(ColorPalette.earthyDarkMoss)
            ) {
                Text(text = if (isLogin) "로그인" else "가입하기", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (isLogin) "아이디가 없으신가요? 회원가입" else "이미 계정이 있으신가요? 로그인",
                color = ColorPalette.earthyDarkMoss,
                modifier = Modifier.clickable { isLogin = !isLogin }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}
