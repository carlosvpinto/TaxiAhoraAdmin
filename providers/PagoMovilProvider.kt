package com.carlosvicente.gaugegrafico.providers

import android.util.Log
import com.carlosvicente.gaugegrafico.models.PagoMovil
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot

class PagoMovilProvider {

    val db = Firebase.firestore.collection("PagoMovil")
    val db2 = Firebase.firestore.collection("Clients")
    val authProvider = AuthProvider()

    fun create(pagoMovil: PagoMovilProvider): Task<DocumentReference> {
        return db.add(pagoMovil).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }
    fun createRegalo(pagoMovil: PagoMovil): Task<DocumentReference> {
        return db.add(pagoMovil).addOnFailureListener {
            Log.d("FIRESTORE", "ERROR: ${it.message}")
        }
    }
    fun remove(IdSolicitud:String): Task<Void> {
        return db.document(IdSolicitud).delete().addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }
    fun getPagosMoviles(idpagoMovil: String): Task<DocumentSnapshot> {
        return db.document(idpagoMovil).get()
    }
    fun getBookingSnap(): Query {
        return db.whereEqualTo("idDriver", authProvider.getId()).whereEqualTo("activo",true)
    }
    fun getVerificagos( verificacion: Boolean): Query {
        return db.whereEqualTo("verificado", verificacion)

    }
    fun updateVerificacion(idClient: String, verificar: Boolean): Task<Void> {
        return db.document(idClient).update("verificado", verificar,).addOnFailureListener { exception ->
            Log.d("FIRESTORE", "ERROR: ${exception.message}")
        }
    }

    fun update(pagoMovil: PagoMovil): Task<Void> {
        val map: MutableMap<String, Any> = HashMap()
        map["verificado"] = pagoMovil?.verificado!!
        return db.document(pagoMovil?.id!!).update(map)
    }



    fun getPagoMovil(idClient:String): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("idClient", authProvider.getId()).orderBy("timestamp", Query.Direction.DESCENDING)
    }
    fun getPagoMovilVerificado(verificado:Boolean): Query { // CONSULTA COMPUESTA - INDICE
        return db.whereEqualTo("verificado", verificado).orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun getHistoryById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }
    fun getPagoMovil(): Query {
        return db.orderBy("timestamp",Query.Direction.DESCENDING)
    }


//    fun getPagoMovil(): Query {
//        return db.whereEqualTo("idDriver", authProvider.getId())
//    }



    }



