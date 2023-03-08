package com.carlosvicente.gaugegrafico.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityClientesDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryDetailBinding
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.providers.ClientProvider
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class ClientesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClientesDetailBinding

    private var clienteProvider = ClientProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment
    var cliente:Client? = null
    val REQUEST_PHONE_CALL = 30
    var seleccion = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

       // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        getCliente()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.layoutTelefonoCliente.setOnClickListener {

            llamarPorTelefono()
        }
    }
    //PREGUNTA POR DONDE DESEA UBICAR AL CLIENTE
    private fun whatsaapOTelefono() {
        val opciones = arrayOf("Enviar Msj WhatsApp", "LLamar por Telefono")
        val singleChoiceDialogo = AlertDialog.Builder(this)
            .setTitle("Seleccione")

            .setSingleChoiceItems(opciones, 0) { dialog, which ->

                // Aquí puedes hacer lo que necesites con la opción seleccionada
                // Por ejemplo, mostrarla en un Toast:
                Toast.makeText(this, "Seleccionaste la opción ${opciones[which]}", Toast.LENGTH_SHORT).show()
                seleccion = opciones[which]
            }
            .setIcon(R.drawable.whatsappsinbordecien)

            .setPositiveButton("Aceptar"){_,_ ->
                Toast.makeText(this, "Seleccionaste aceptar $seleccion", Toast.LENGTH_LONG).show()


            }
            .setNegativeButton("Cancelar"){_,_ ->
                return@setNegativeButton
                Toast.makeText(this, "Seleccionaste Cancelar", Toast.LENGTH_SHORT).show()
            }
            .create()

        singleChoiceDialogo.show()
    }


    private fun llamarPorTelefono() {
        Log.d("LLAMAR", "VALOR DE driver?.phone: ${cliente?.phone}")
        if (cliente?.phone != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }

            call(cliente?.phone!!)
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_CALL) {
            if (cliente?.phone != null) {
                call(cliente?.phone!!)
            }
        }

    }
    //LLAMAR POR TELEFONO
    private fun call(phone: String) {

        val i = Intent(Intent.ACTION_CALL)
        i.data = Uri.parse("tel:$phone")

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        this.startActivity(i)
    }

    private fun getCliente() {
        clienteProvider.getClientById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                cliente = document.toObject(Client::class.java)
                binding.textViewIdCliente.text = cliente?.id
                binding.textNombreCliente.text =  cliente?.name +" "+ cliente?.lastname

                binding.textEmailCliente.text= cliente?.email.toString()
                binding.textTelefonoCliente.text = cliente?.phone.toString()
                binding.textViewNameCond.text = cliente?.name +" "+ cliente?.lastname
                if (cliente?.image != null) {
                    if (cliente?.image != "") {
                        Glide.with(this).load(cliente?.image).into(binding.circleImageProfileCliente)
                    }
                }
                //getDriverInfo(conductor?.id!!)
            }

        }
    }

}