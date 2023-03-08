package com.carlosvicente.gaugegrafico.fragments


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.*
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.providers.*
import com.google.android.gms.tasks.Tasks.call

class ModalBottomSheetMenu: BottomSheetDialogFragment() {

    val clientProvider = ClientProvider()
    val authProvider = AuthProvider()

    var textViewUsername: TextView? = null
    var linearLayoutLogout: LinearLayout? = null
    var linearLayoutProfile: LinearLayout? = null
    var linearLayoutHistory: LinearLayout? = null
    var linearLayoutLlamar: LinearLayout? = null
    var linearLayoutConductores: LinearLayout? = null
    var linearLayoutClientes: LinearLayout? = null
    var linearLayoutSolicitudes: LinearLayout? = null
    var linearLayoutHistoriasCanceladas: LinearLayout? = null
    val REQUEST_PHONE_CALL = 30


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.modal_bottom_sheet_menu, container, false)

        textViewUsername = view.findViewById(R.id.textViewUsername)
        linearLayoutLogout = view.findViewById(R.id.linearLayoutLogout)
        linearLayoutProfile = view.findViewById(R.id.linearLayoutProfile)
        linearLayoutHistory = view.findViewById(R.id.linearLayoutHistory)
        linearLayoutLlamar = view.findViewById(R.id.linearLayoutLlamar)
        linearLayoutConductores = view.findViewById(R.id.linearLayoutConductor)
        linearLayoutClientes = view.findViewById(R.id.linearLayoutCliente)
        linearLayoutSolicitudes = view.findViewById(R.id.linearLayoutSolicitudes)
        linearLayoutHistoriasCanceladas = view.findViewById(R.id.linearLayoutHistoryCanceladas)

        getClient()

        linearLayoutLogout?.setOnClickListener { goToMain() }
        linearLayoutProfile?.setOnClickListener { goToProfile() }
        linearLayoutHistory?.setOnClickListener { goToHistories() }
        linearLayoutClientes?.setOnClickListener { goToClientes() }
        linearLayoutLlamar?.setOnClickListener {

            val driverTlf = "0584243454032"
                whatSapp(driverTlf)



        }
        linearLayoutConductores?.setOnClickListener { verConductores() }
        linearLayoutSolicitudes?.setOnClickListener { verSolicitudes() }


        return view
    }

    private fun verSolicitudes() {
        val i = Intent(activity, SolicitudesActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun goToClientes() {
        val i = Intent(activity, ClienteActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun verConductores() {
        val i = Intent(activity, ConductoresActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    //ENVIAR MSJ DE WHATSAPP*******YO******
    private fun whatSapp (phone: String){
        var phone58 = phone
        val cantNrotlf = phone.length // devuelve 10

            try {
                // código que puede generar una excepción
                val phone58 = "058$phone"
                val i  = Intent(Intent.ACTION_VIEW);
                val  uri =  "whatsapp://send?phone="+phone+"&text="+"hola te escribo de la apliacion TAXI AHORA:";
                i.setData(Uri.parse(uri))
                requireActivity().startActivity(i)
            } catch (e: Exception) {
                // código para manejar la excepción
                Toast.makeText(requireContext(), "Error al iniciar Whatsaap $e", Toast.LENGTH_SHORT).show()
                return
            }





    }

    private fun goToProfile() {
        val i = Intent(activity, ProfileActivity::class.java)
        startActivity(i)
    }

    private fun goToHistories() {
        val i = Intent(activity, HistoriesActivity::class.java)
        startActivity(i)
    }

    private fun goToMain() {
        authProvider.logout()
        val i = Intent(activity, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    private fun getClient() {
        clientProvider.getClientById(authProvider.getId()).addOnSuccessListener { document ->
            if (document.exists()) {
                val client = document.toObject(Client::class.java)
                textViewUsername?.text = "${client?.name} ${client?.lastname}"
            }
        }
    }

    companion object {
        const val TAG = "ModalBottomSheetMenu"
    }


}