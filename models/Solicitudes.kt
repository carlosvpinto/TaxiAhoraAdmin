package com.carlosvicente.gaugegrafico.models

import com.beust.klaxon.*
import java.util.*

private val klaxon = Klaxon()

data class Solicitudes (

    var id: String? = null,
    var destination: String? = null,
    val email: String ? = null,
    val fecha: Date ? = null,
    val idClient: String ? = null,
    var idDriver: String ? = null,
    val km: Int ? = null,
    val lastname: String ? = null,
    val name: String ? = null,
    val origin: String ? = null,
    var phone: String? = null,
    var tiempo: Int? = null

) {


    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Client>(json)
    }
}