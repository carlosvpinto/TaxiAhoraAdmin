package com.carlosvicente.gaugegrafico.providers

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.carlosvicente.gaugegrafico.models.Booking
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.models.HistoryDriverCancel

class HistoryCancelProvider {

    val db = Firebase.firestore.collection("HistoriesCancel")
    val authProvider = AuthProvider()
    val query = db.orderBy("timestamp", Query.Direction.DESCENDING)


    fun create(history: HistoryDriverCancel): Task<DocumentReference> {
        return db.add(history).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }

    fun getHistoryByIdCancel(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }
    fun getHistoryCancelId(idHistorycancel: String): Task<DocumentSnapshot> {
        return db.document(idHistorycancel).get()
    }

    fun getLastHistoryCancel(): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING).limit(1)

    }

    fun getHistoryCancel(): Query {
        return db.orderBy("timestamp",Query.Direction.DESCENDING)
    }
    fun remove(IdHistoryCancel:String): Task<Void> {
        return db.document(IdHistoryCancel).delete().addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

    fun getHistoriesCancel(): Query { // CONSULTA COMPUESTA - INDICE
        Log.d("HISTOCANCEL", "getHistoriesCancel que trae el authProvider: ${ authProvider.getId()}")
        return db.whereEqualTo("idDriver", authProvider.getId()).orderBy("timestamp", Query.Direction.ASCENDING)
        Log.d("HISTOCANCEL", " RESPUESTAS DEL GetHistoryCancel:= ${getHistoriesCancel()}")
    }

    fun getDriver(idDriver: String): Task<DocumentSnapshot> {
        return db.document(idDriver).get()
    }

    fun getBooking(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId())
    }

    fun updateCalificationToClient(id: String, calification: Float): Task<Void> {
        return db.document(id).update("calificationToClient", calification).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

}