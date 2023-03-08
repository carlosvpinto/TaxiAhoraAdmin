package com.carlosvicente.gaugegrafico.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresDetailBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryDetailBinding
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class ConductoresDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConductoresDetailBinding
    private var historyProvider = HistoryProvider()
    private var driverProvider = DriverProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment
    private var driver: Driver? = null
    var conductor:Driver? = null
    val REQUEST_PHONE_CALL = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConductoresDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

       // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        getConductores()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.layoutTelefono.setOnClickListener {
          llamarPorTelefono()
        }
    }

    private fun llamarPorTelefono() {
        Log.d("LLAMAR", "VALOR DE driver?.phone: ${conductor?.phone}")
        if (conductor?.phone != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PHONE_CALL)
            }

            call(conductor?.phone!!)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_PHONE_CALL) {
            if (conductor?.phone != null) {
                call(conductor?.phone!!)
            }
        }

    }

    private fun getConductores() {
        driverProvider.getConductorById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                conductor = document.toObject(Driver::class.java)
                binding.textViewIdConduc.text = conductor?.id
                binding.textViewNameCond.text = conductor?.name
                binding.textNombreCondu.text= conductor?.name + conductor?.lastname
                binding.textEmailCondu.text= conductor?.email.toString()

                binding.textTelefonoConduc.text = conductor?.phone.toString()
                binding.textColorCarro.text = conductor?.colorCar
                binding.textViewPlaca.text = conductor?.plateNumber
                binding.textViewTipoVehi.text = conductor?.tipo.toString()
                binding.textViewActivado.text = conductor?.activado.toString()
                if (conductor?.image != null) {
                    if (conductor?.image != "") {
                        Glide.with(this).load(conductor?.image).into(binding.circleImageProfileConduc)
                    }
                }
                //getDriverInfo(conductor?.id!!)
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
    private fun getDriverInfo() {
        driverProvider.getDriver(extraId).addOnSuccessListener { document ->
            if (document.exists()) {
                driver = document.toObject(Driver::class.java)
            }
        }
    }
}