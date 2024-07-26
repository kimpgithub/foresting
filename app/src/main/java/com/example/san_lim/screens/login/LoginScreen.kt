package com.example.san_lim.screens.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.san_lim.loginUser
import com.example.san_lim.signUpUser
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("test@intel.com") }
    var password by remember { mutableStateOf("123456") }
    var isLogin by remember { mutableStateOf(true) } // true for login, false for sign up

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLogin) {
            Button(
                onClick = { loginUser(email, password, navController) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Don't have an account? Sign up",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { isLogin = false }
            )
        } else {
            Button(
                onClick = { signUpUser(email, password, navController) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Already have an account? Login",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { isLogin = true }
            )
        }
    }
}