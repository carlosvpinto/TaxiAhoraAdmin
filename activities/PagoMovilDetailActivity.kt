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
import com.carlosvicente.gaugegrafico.databinding.ActivityDetailPagoMovilBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesDetailBinding
import com.carlosvicente.gaugegrafico.models.*
import com.carlosvicente.gaugegrafico.providers.*
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.google.android.gms.tasks.Tasks
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.io.File
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PagoMovilDetailActivity : AppCompatActivity() {

    //para los efectos del floatingButton
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }

    private lateinit var binding: ActivityDetailPagoMovilBinding
    private var pagoMovilProvider = PagoMovilProvider()
    private var clientProvider = ClientProvider()

    private var extraId = ""
    private var progressDialog = ProgressDialogFragment

    var pagoMovil: PagoMovil? = null
    private var cliente: Client?=null
    private var imageFile: File? = null
    val REQUEST_PHONE_CALL = 30

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPagoMovilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        Log.d("SOLICITUD", "IDSOLICITUD: $extraId ")
        getPagoMovil()

        binding.imageViewBack.setOnClickListener { finish() }

        binding.layoutIdCliente.setOnClickListener {
            iraCliente()
        }

        binding.addbtn.setOnClickListener{
            onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener{
            updateVerificacion()
        }
        binding.floatingActionDelete.setOnClickListener {
            deletePagoMovil()

        }
    }

    private fun deletePagoMovil() {
        progressDialog.showProgressBar(this)
        val idPagoMovil = binding.textIdPagoMovil.text.toString()

        if (idPagoMovil!=null){
            pagoMovilProvider.remove(idPagoMovil).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@PagoMovilDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                    goToPagoMovilActivity()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@PagoMovilDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    //envia al el Activiti Solicitud
    private fun goToPagoMovilActivity() {
        val i = Intent(this, PagoMovilActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    //ACTUALIZA LA INFORMACION DEL LA SOLICITUD
    private fun updateVerificacion() {
        progressDialog.showProgressBar(this)
        val verificator = binding.checkboxVerificacion.isChecked
        val id = binding.textIdPagoMovil.text.toString()
        pagoMovilProvider.updateVerificacion(id,verificator).addOnCompleteListener {
            if (it.isSuccessful) {
                progressDialog.hideProgressBar(this)
                Toast.makeText(this@PagoMovilDetailActivity, "Pago Movil Verificado correctamente", Toast.LENGTH_LONG).show()
                goToPagoMovilActivity()
            }
            else {
                progressDialog.hideProgressBar(this)
                Toast.makeText(this@PagoMovilDetailActivity, "No se pudo Verificar la informacion", Toast.LENGTH_LONG).show()
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


    private fun iraCliente() {
        val i = Intent(this, ClientesDetailActivity::class.java)
        i.putExtra("id", binding.textIdCliente.text)
        this.startActivity(i)
    }




    private fun getPagoMovil() {

        pagoMovilProvider.getPagosMoviles(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                pagoMovil = document.toObject(PagoMovil::class.java)

                binding.textIdPagoMovil.text= extraId
                binding.textTiempoStamp.text = RelativeTime.getTimeAgo(pagoMovil?.timestamp!!, this)
               // binding.textNombreCliente.text = pagoMovil?.idClient
                binding.textIdCliente.text = pagoMovil?.idClient
                binding.textFecha.text= pagoMovil?.date.toString()
                binding.textFecha.text= pagoMovil?.date.toString()
                binding.textMontobs.text = pagoMovil?.montoBs.toString()
                binding.textMontoDollarDetail.text = pagoMovil?.montoDollar.toString()
                binding.textTazaCDetail.text = pagoMovil?.tazaCambiaria.toString()
                binding.checkboxVerificacion.isChecked = pagoMovil?.verificado!!


                getClienteInfo(pagoMovil?.idClient!!)
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
    private fun getClienteInfo(IdClient:String) {
        clientProvider.getClientById(IdClient).addOnSuccessListener { document ->
            if (document.exists()) {
                cliente = document.toObject(Client::class.java)

                binding.textNombreCliente.text = cliente?.name + " " + cliente?.lastname
            }
        }
    }
}