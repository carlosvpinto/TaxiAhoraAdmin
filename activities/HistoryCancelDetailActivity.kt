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
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryCancelDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesDetailBinding
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.HistoryCancel
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryCancelProvider
import com.carlosvicente.gaugegrafico.providers.SolicitudesProvider
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.util.*

class HistoryCancelDetailActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }

    private lateinit var binding: ActivityHistoryCancelDetailBinding
    private var historyCancelProvider = HistoryCancelProvider()
    private var driverProvider = DriverProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment
    private var historysCancel: HistoryCancel? = null
    var historiaCancel: HistoryCancel? = null
    private var driver: Driver?=null
    val REQUEST_PHONE_CALL = 30
    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryCancelDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        getHistoryCancel()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.addbtn.setOnClickListener{
            onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener{
            updateInfo()
        }
        binding.floatingActionDelete.setOnClickListener {
            deleteHistCancel()
        }
        binding.layoutIdCliente.setOnClickListener {
            iraCliente()
        }
        binding.layoutConductor.setOnClickListener {
            Log.d("IRACONDUCTOR", "iraConductor: Entro")
            iraConductor()
        }

    }

    //BUSCA AL CLIENTE SELECIONADO
    private fun iraCliente() {
        val i = Intent(this, ClientesDetailActivity::class.java)
        i.putExtra("id", binding.textIdCliente.text.toString())
        this.startActivity(i)
    }
    private fun iraConductor() {
        Log.d("IRACONDUCTOR", "iraConductor: Entro")
        val i = Intent(this, ConductoresDetailActivity::class.java)
        i.putExtra("id", binding.textViewIdConductor.text.toString())
        this.startActivity(i)
    }

    //PARA FUNCIONAR LOS FLOATING*******************
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

    //ACTUALIZA LA INFORMACION DEL CLIENTE
    private fun updateInfo() {
//        progressDialog.showProgressBar(this)
//        val name = binding.textNombreClienteDetail.text.toString()
//        val lastname = binding.textApellidoClienteDetail.text.toString()
//        val phone = binding.textTelefonoCliente.text.toString()
//        val idCliente = binding.textViewIdCliente.text.toString()
//
//        val client = Client(
//            id = idCliente,
//            name = name,
//            lastname = lastname,
//            phone = phone,
//
//            )
//
//        if (imageFile != null) {
//            clientProvider.uploadImage(idCliente, imageFile!!).addOnSuccessListener { taskSnapshot ->
//                clientProvider.getImageUrl().addOnSuccessListener { url ->
//                    val imageUrl = url.toString()
//                    client.image = imageUrl
//
//                    clientProvider.update(client).addOnCompleteListener {
//                        if (it.isSuccessful) {
//                            progressDialog.hideProgressBar(this)
//                            Toast.makeText(this@ClientesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
//                        }
//                        else {
//                            progressDialog.hideProgressBar(this)
//                            Toast.makeText(this@ClientesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                    Log.d("STORAGE", "$imageUrl")
//                }
//            }
//        }
//        else {
//            clientProvider.update(client).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    progressDialog.hideProgressBar(this)
//                    Toast.makeText(this@ClientesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
//                }
//                else {
//                    progressDialog.hideProgressBar(this)
//                    Toast.makeText(this@ClientesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
//                }
//            }
//        }

    }

    //ELIMINA LAS HISTORIAS CANCELADAS****************
    private fun deleteHistCancel() {
        progressDialog.showProgressBar(this)
        val idHistoryCancel = binding.textViewIdHistoria.text.toString()

        if (idHistoryCancel!=null){
            historyCancelProvider.remove(idHistoryCancel).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@HistoryCancelDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                    goToHistoryCancelActivity()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@HistoryCancelDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    //envia al el Activiti Soliccitud
    private fun goToHistoryCancelActivity() {
        val i = Intent(this, HistoryCancelActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }




    private fun getHistoryCancel() {
        historyCancelProvider.getHistoryCancelId(extraId).addOnSuccessListener { document ->

            if (document.exists()) {


                historiaCancel = document.toObject(HistoryCancel::class.java)
                binding.textViewIdHistoria.setText(extraId)
                binding.textIdCliente.text=historiaCancel?.idClient.toString()
                binding.textViewIdConductor.text=historiaCancel?.idDriver.toString()
                binding.textView4.text=historiaCancel?.destination
                binding.textView5.text= historiaCancel?.fecha.toString()
                binding.textView6.text=historiaCancel?.causa
                binding.textView7.text= historiaCancel?.causaConductor
                binding.textView8.text= historiaCancel?.km.toString()



                getDriverInfo(historiaCancel?.idDriver!!)
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