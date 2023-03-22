package com.carlosvicente.gaugegrafico.models

import android.opengl.Matrix
import com.beust.klaxon.*

private val klaxon = Klaxon()

data class Conectado (
    var id: String? = null,
//    val g: String ? = null,
//    val l: String? = null,

) {


    public fun toJson() = klaxon.toJsonString(this)

    companion object {
        public fun fromJson(json: String) = klaxon.parse<Client>(json)
    }
}