package com.carlosvicente.gaugegrafico.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
//import com.carlosvicente.uberkotlin.databinding.ActivityHistoriesDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryDetailBinding

import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.providers.ClientProvider
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class HistoriesDetailActivity : AppCompatActivity() {

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }

    private lateinit var binding: ActivityHistoryDetailBinding
    private var historyProvider = HistoryProvider()
    private var driverProvider = DriverProvider()
    private var extraId = ""
    private var clicked = false
    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        getHistory()

        binding.imageViewBack.setOnClickListener { finish() }

        binding.addbtn.setOnClickListener{
            onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener{
            updateInfo()
        }
        binding.floatingActionDelete.setOnClickListener {
            deleteHistory()
        }
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

    //ELIMINA LAS HISTORIAS ****************
    private fun deleteHistory() {
        progressDialog.showProgressBar(this)
        val idHistory = binding.textViewIdHistoria.text.toString()

        if (idHistory!=null){
            historyProvider.remove(idHistory).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@HistoriesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                    goToHistoryActivity()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@HistoriesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    //envia al el Activity Historia
    private fun goToHistoryActivity() {
        val i = Intent(this, HistoriesActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }


    //ACTUALIZA LA INFORMACION DE LA HISTORIA
    private fun updateInfo() {
        Toast.makeText(this, "No se puede Modificar los datos de historia", Toast.LENGTH_LONG).show()
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

    private fun getHistory() {
        historyProvider.getHistoryById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                val history = document.toObject(History::class.java)
                binding.textViewIdHistoria.text= extraId
                binding.textViewOrigin.text = history?.origin
                binding.textViewDestination.text = history?.destination
                binding.textViewDate.text = RelativeTime.getTimeAgo(history?.timestamp!!, this@HistoriesDetailActivity)
                binding.textViewDateFija.text= history?.date.toString()
                binding.textViewPrice.text = "${String.format("%.1f", history?.price)}$"
                binding.textViewMyCalification.text = "${history?.calificationToDriver}"
                binding.textViewClientCalification.text = "${history?.calificationToClient}"
                binding.textViewTimeAndDistance.text = "${history?.time} Min - ${String.format("%.1f", history?.km)} Km"
                getDriverInfo(history?.idDriver!!)
            }

        }
    }

    private fun getDriverInfo(id: String) {
        driverProvider.getDriver(id).addOnSuccessListener { document ->
            if (document.exists()) {
                val driver = document.toObject(Driver::class.java)
                binding.textViewEmail.text = driver?.email
                binding.textViewName.text = "${driver?.name} ${driver?.lastname}"

            }
        }
        progressDialog.hideProgressBar(this)
    }
}