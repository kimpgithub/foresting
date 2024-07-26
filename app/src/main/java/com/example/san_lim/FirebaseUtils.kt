package com.example.san_lim

import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun loginUser(email: String, password: String, navController: NavHostController) {
    try {
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 로그인 성공
                    Toast.makeText(navController.context, "Login Success", Toast.LENGTH_SHORT).show()
                    navController.navigate("home")
                } else {
                    // 로그인 실패
                    Toast.makeText(navController.context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    } catch (e: FirebaseAuthException) {
        Toast.makeText(navController.context, "Authentication error: ${e.message}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(navController.context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}

fun signUpUser(email: String, password: String, navController: NavHostController) {
    try {
        val auth = Firebase.auth
        val firestore = Firebase.firestore

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 회원가입 성공
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "email" to email
                    )
                    firestore.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(navController.context, "Sign Up Success", Toast.LENGTH_SHORT).show()
                            navController.navigate("login")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(navController.context, "Error adding document: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // 회원가입 실패
                    Toast.makeText(navController.context, "Sign Up Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    } catch (e: FirebaseAuthException) {
        Toast.makeText(navController.context, "Authentication error: ${e.message}", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(navController.context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
    }
}
