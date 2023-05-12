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
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.messaging.FirebaseMessaging
import java.io.File

class SolicitudesProvider {


    val authProvider = AuthProvider()
    val db = Firebase.firestore.collection("Solicitudes")
    var storage = FirebaseStorage.getInstance().getReference().child("name")

    fun create(solicitudes: Solicitudes): Task<Void> {
        return db.document(solicitudes.id!!).set(solicitudes)
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
    fun remove(IdSolicitud:String): Task<Void> {
        return db.document(IdSolicitud).delete().addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

    fun getImageUrl(): Task<Uri> {
        return storage.downloadUrl
    }

   //traer todo los datos de driver**************

//    fun getConductorById(id: String): Task<DocumentSnapshot> {
//        return db.document(id).get()
//    }
    fun getSolicitudes(): Query {
        return db.orderBy("time",Query.Direction.DESCENDING)
    }






    fun getSolicitudes(idSolicitudes: String): Task<DocumentSnapshot> {
        return db.document(idSolicitudes).get()
    }

    fun update(solicitudes: Solicitudes): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["name"] = solicitudes?.name!!
        map["lastname"] = solicitudes?.lastname!!
        map["phone"] = solicitudes?.phone!!
        map["idClient"] = solicitudes?.idClient!!
        map["idDriver"] = solicitudes?.idDriver!!
        map["origin"] = solicitudes?.origin!!
        map["fecha"] = solicitudes?.fecha!!
        map["time"] = solicitudes?.time!!
        return db.document(solicitudes?.id!!).update(map)
    }

}