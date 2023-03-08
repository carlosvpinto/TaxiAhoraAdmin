package com.carlosvicente.gaugegrafico.providers

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.carlosvicente.gaugegrafico.models.Driver
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

class DriverProvider {


    val authProvider = AuthProvider()
    val db = Firebase.firestore.collection("Drivers")
    var storage = FirebaseStorage.getInstance().getReference().child("profile")

    fun create(driver: Driver): Task<Void> {
        return db.document(driver.id!!).set(driver)
    }

    fun uploadImage(id: String, file: File): StorageTask<UploadTask.TaskSnapshot> {
        var fromFile = Uri.fromFile(file)
        val ref = storage.child("$id.jpg")
        storage = ref
        val uploadTask = ref.putFile(fromFile)

        return uploadTask.addOnFailureListener {
            Log.d("STORAGE", "ERROR: ${it.message}")
        }
    }

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

   //traer todo los daos de driver**************

    fun getConductorById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }
    fun getDriver(): Query {
        return db
    }





    fun getDriver(idDriver: String): Task<DocumentSnapshot> {
        return db.document(idDriver).get()
    }

    fun update(driver: Driver): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = driver?.name!!
        map["lastname"] = driver?.lastname!!
        map["phone"] = driver?.phone!!
        map["brandCar"] = driver?.brandCar!!
        map["colorCar"] = driver?.colorCar!!
        map["plateNumber"] = driver?.plateNumber!!
        map["image"] = driver?.image!!
        return db.document(driver?.id!!).update(map)
    }

}