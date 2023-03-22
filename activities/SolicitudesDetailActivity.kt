package com.carlosvicente.gaugegrafico.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesDetailBinding
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.carlosvicente.gaugegrafico.providers.SolicitudesProvider
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.io.File

class SolicitudesDetailActivity : AppCompatActivity() {

    //para los efectos del floatingButton
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }

    private lateinit var binding: ActivitySolicitudesDetailBinding
    private var solicitudesProvider = SolicitudesProvider()
    private var driverProvider = DriverProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment

    var solicitud: Solicitudes? = null
    private var driver: Driver?=null
    private var imageFile: File? = null
    val REQUEST_PHONE_CALL = 30

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySolicitudesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        Log.d("SOLICITUD", "IDSOLICITUD: $extraId ")
        getSolicitudes()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.layoutTelefono.setOnClickListener {
            llamarPorTelefono()
        }
        binding.layoutIdCliente.setOnClickListener {
            iraCliente()
        }
        binding.layoutConductor.setOnClickListener {
            iraConductor()
        }
        binding.addbtn.setOnClickListener{
            onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener{
            updateInfo()
        }
        binding.floatingActionDelete.setOnClickListener {
            deleteSolictud()

        }
    }

    private fun deleteSolictud() {
                progressDialog.showProgressBar(this)
        val idSolicitud = binding.textIdSolicitud.text.toString()

        if (idSolicitud!=null){
            solicitudesProvider.remove(idSolicitud).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@SolicitudesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                    goToSolicitudActivity()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@SolicitudesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



//envia al el Activiti Solicitud
    private fun goToSolicitudActivity() {
    val i = Intent(this, SolicitudesActivity::class.java)
    //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(i)
    }

    //ACTUALIZA LA INFORMACION DEL LA SOLICITUD
    private fun updateInfo() {
        progressDialog.showProgressBar(this)
        val id = binding.textIdSolicitud.text.toString()
        val name = binding.textNombreCondu.text.toString()
        val lastname = binding.textNombreCondu.text.toString()
        val phone = binding.textTelefonoClientSoli.text.toString()
        val idCliente = binding.textIdCliente.text.toString()

        val solicitud = Solicitudes(
            id = id,
            name = name,
            lastname = lastname,
            phone = phone,
        )

        if (imageFile != null) {
            solicitudesProvider.uploadImage(idCliente, imageFile!!).addOnSuccessListener { taskSnapshot ->
                solicitudesProvider.getImageUrl().addOnSuccessListener { url ->


                    solicitudesProvider.update(solicitud).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@SolicitudesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                        }
                        else {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@SolicitudesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
        else {
            solicitudesProvider.update(solicitud).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@SolicitudesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@SolicitudesDetailActivity , "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }


    }


    //ANIMA EL FLOATINBOTTOM
    private fun onAddButtonClicked() {
        // Log.d("Cliquio", "dio CLICK EN  on addButton EL BOTON= $clicked ")
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    //ACTIVA LA ANIMACION EL FLOATINBOTTOM
    private fun setAnimation(clicked: Boolean) {
        if (!clicked){
            binding.floatingActionEdit.startAnimation(fromBottom)
            binding.floatingActionDelete.startAnimation(fromBottom)
            binding.addbtn.startAnimation(rotateOpen)
        }else{
            binding.floatingActionEdit.startAnimation(toBottom)
            binding.floatingActionDelete.startAnimation(toBottom)
            binding.addbtn.startAnimation(rotateClose)
        }
    }

    //VISIBLE Y INVISBLE LOS BOTONES FLOATING******
    private fun setVisibility(clicked: Boolean) {
        //  Log.d("Cliquio", "dio CLICK EN EL BOTON SetVisivilty= $clicked")
        if (!clicked){
            binding.floatingActionDelete.visibility = View.VISIBLE
            binding.floatingActionEdit.visibility= View.VISIBLE
        }else{
            binding.floatingActionDelete.visibility = View.INVISIBLE
            binding.floatingActionEdit.visibility= View.INVISIBLE
        }
    }
    private fun setClickable(clicked: Boolean){
        if (!clicked){
            binding.floatingActionDelete.isClickable = true
            binding.floatingActionEdit.isClickable= true
        }else{
            binding.floatingActionDelete.isClickable = false
            binding.floatingActionEdit.isClickable= false
        }
    }
    private fun iraConductor() {
        val i = Intent(this, ConductoresDetailActivity::class.java)
        i.putExtra("id", binding.textIdConductor.text)
        this.startActivity(i)
    }

    private fun iraCliente() {
        val i = Intent(this, ClientesDetailActivity::class.java)
        i.putExtra("id", binding.textIdCliente.text)
        this.startActivity(i)
    }

    private fun llamarPorTelefono() {
        Log.d("LLAMAR", "VALOR DE driver?.phone: ${solicitud?.phone}")
        if (solicitud?.phone != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }

            call(solicitud?.phone!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_CALL) {
            if (solicitud?.phone != null) {
                call(solicitud?.phone!!)
            }
        }

    }

    private fun getSolicitudes() {
        solicitudesProvider.getSolicitudes(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                solicitud = document.toObject(Solicitudes::class.java)

                binding.textIdSolicitud.text= extraId
                binding.textNombreCondu.text = solicitud?.name + " " + solicitud?.lastname
                binding.textDestino.text = solicitud?.destination
                binding.textEmailSolicitud.text= solicitud?.email
                binding.textFecha.text= solicitud?.fecha.toString()
                binding.textTelefonoClientSoli.text = solicitud?.phone.toString()
                binding.textIdConductor.text = solicitud?.idDriver
                binding.textIdCliente.text = solicitud?.idClient
                binding.textOrigen.text = solicitud?.origin


                getDriverInfo(solicitud?.idDriver!!)
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
    private fun getDriverInfo(Iddriver:String) {
        driverProvider.getDriver(Iddriver).addOnSuccessListener { document ->
            if (document.exists()) {
                driver = document.toObject(Driver::class.java)
            }
        }
    }
}