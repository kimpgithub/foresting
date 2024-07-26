package com.example.san_lim.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.net.Uri
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
//
//fun uploadDataToFirebase(
//    name: String,
//    latitude: Double,
//    longitude: Double,
//    date: String,
//    weedType: String,
//    comment: String,
//    imageUri: Uri,
//    onSuccess: () -> Unit,
//    onFailure: (Exception) -> Unit
//) {
//    val database: DatabaseReference = FirebaseDatabase.getInstance().reference
//    val storage: StorageReference = FirebaseStorage.getInstance().reference
//
//    val imageRef = storage.child("images/${imageUri.lastPathSegment}")
//    val uploadTask = imageRef.putFile(imageUri)
//
//    uploadTask.addOnSuccessListener { taskSnapshot ->
//        Log.d("FirebaseUtils", "Image upload successful")
//        imageRef.downloadUrl.addOnSuccessListener { uri ->
//            val weedData = WeedData(
//                name,
//                latitude,
//                longitude,
//                date,
//                weedType,
//                comment,
//                uri.toString()
//            )
//
//            database.child("weeds").push().setValue(weedData)
//                .addOnSuccessListener {
//                    Log.d("FirebaseUtils", "Data upload successful")
//                    onSuccess()
//                }
//                .addOnFailureListener { exception ->
//                    Log.e("FirebaseUtils", "Database upload failed", exception)
//                    onFailure(exception)
//                }
//        }.addOnFailureListener { exception ->
//            Log.e("FirebaseUtils", "Failed to get download URL", exception)
//            onFailure(exception)
//        }
//    }.addOnFailureListener { exception ->
//        Log.e("FirebaseUtils", "Image upload failed", exception)
//        onFailure(exception)
//    }
//}
//
//fun fetchDataFromFirebase(
//    onDataLoaded: (List<WeedData>) -> Unit,
//    onFailure: (Exception) -> Unit
//) {
//    val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("weeds")
//    database.addListenerForSingleValueEvent(object : ValueEventListener {
//        override fun onDataChange(dataSnapshot: DataSnapshot) {
//            val weedDataList = mutableListOf<WeedData>()
//            for (snapshot in dataSnapshot.children) {
//                val weedData = snapshot.getValue(WeedData::class.java)
//                weedData?.let { weedDataList.add(it) }
//            }
//            onDataLoaded(weedDataList)
//        }
//
//        override fun onCancelled(databaseError: DatabaseError) {
//            Log.e("FirebaseUtils", "Database fetch failed", databaseError.toException())
//            onFailure(databaseError.toException())
//        }
//    })
//}