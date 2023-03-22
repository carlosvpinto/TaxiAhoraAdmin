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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresDetailBinding
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.io.File

class ConductoresDetailActivity : AppCompatActivity() {

    //para los efectos del floatingButton
    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(this,
            R.anim.rotate_open_anim)
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(this,
            R.anim.rotate_close_anim)
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this,
            R.anim.from_bottom_anim)
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(this,
            R.anim.to_bottom_anim)
    }

    private lateinit var binding: ActivityConductoresDetailBinding
    private var historyProvider = HistoryProvider()
    private var driverProvider = DriverProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment
    private var driver: Driver? = null
    var conductor: Driver? = null
    val REQUEST_PHONE_CALL = 30

    private var imageFile: File? = null

    private var clicked = false
    var campoSp: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConductoresDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // progressDialog.showProgressBar(this)
        cargarSpiner()
        extraId = intent.getStringExtra("id")!!
        getConductores()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.iconTelefono.setOnClickListener {

            llamarPorTelefono()
        }
        binding.iconWhatSapp.setOnClickListener {
            if (conductor?.phone != null) {
                whatSapp(conductor?.phone!!)
            }
        }
        binding.addbtn.setOnClickListener {
            onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener {
            updateInfo()
        }

    }

    private fun deleteUser() {

        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        currentUser?.delete()?.addOnCompleteListener(OnCompleteListener { task ->
            if (task.isSuccessful) {
                // El usuario fue eliminado exitosamente
                Toast.makeText(this, "Usuario Eliminado", Toast.LENGTH_SHORT).show()
            } else {
                // Ocurrió un error al eliminar al usuario
                Toast.makeText(this, "Error al eliminar ${task.exception}", Toast.LENGTH_LONG).show()
            }
        })

    }


    //CARGA LOS DATOS AL SPINER
    private fun cargarSpiner() {
        binding.spTipoVehi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position== 0){campoSp= "Carro"}
                if (position== 1){campoSp="Moto"}
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@ConductoresDetailActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
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

            binding.addbtn.startAnimation(rotateOpen)
        }else{
            binding.floatingActionEdit.startAnimation(toBottom)

            binding.addbtn.startAnimation(rotateClose)
        }
    }

    //VISIBLE Y INVISBLE LOS BOTONES FLOATING******
    private fun setVisibility(clicked: Boolean) {
        //  Log.d("Cliquio", "dio CLICK EN EL BOTON SetVisivilty= $clicked")
        if (!clicked){

            binding.floatingActionEdit.visibility= View.VISIBLE
        }else{

            binding.floatingActionEdit.visibility= View.INVISIBLE
        }
    }
    private fun setClickable(clicked: Boolean){
        if (!clicked){

            binding.floatingActionEdit.isClickable= true
        }else{

            binding.floatingActionEdit.isClickable= false
        }
    }
    //ACTUALIZA LA INFORMACION DEL CLIENTE
    private fun updateInfo() {
        progressDialog.showProgressBar(this)
        val idConductor = binding.textViewIdConduc.text.toString()
        val name = binding.textNombreCondu.text.toString()
        val lastname = binding.textApellidoCondu.text.toString()
        val phone = binding.textTelefonoConduc.text.toString()
        val plateNumber = binding.textViewPlaca.text.toString()
        val colorCar = binding.textColorCarro.text.toString()



        val brandCar = binding.textViewMarca.text.toString()
        val activado = binding.checkboxActivado.isChecked



        val driver = Driver(
            id = idConductor,
            name = name,
            lastname = lastname,
            phone = phone,
            plateNumber= plateNumber,
            colorCar= colorCar,
            tipo = campoSp,
            brandCar= brandCar,
            activado = activado,
        )

        if (imageFile != null) {
            driverProvider.uploadImage(idConductor, imageFile!!).addOnSuccessListener { taskSnapshot ->
                driverProvider.getImageUrl().addOnSuccessListener { url ->
                    val imageUrl = url.toString()
                    driver.image = imageUrl

                    driverProvider.update(driver).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@ConductoresDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                            goToConductoresActivity()
                        }
                        else {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@ConductoresDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d("STORAGE", "$imageUrl")
                }
            }
        }
        else {
            driverProvider.update(driver).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@ConductoresDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                    goToConductoresActivity()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@ConductoresDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    private fun goToConductoresActivity() {
        val i = Intent(this, ConductoresActivity::class.java)
        //i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    //ENVIAR MSJ DE WHATSAPP*******YO******
    private fun whatSapp (phone: String){
        var phone58 = phone
        val cantNrotlf = phone.length // devuelve 10
        if (cantNrotlf<=11){
            val phone58 = "058$phone"
            val i  = Intent(Intent.ACTION_VIEW);
            val  uri = "whatsapp://send?phone="+phone58+"&text="+conductor?.name +"  ${conductor?.lastname}  ¡Hola!\n"
            i.setData(Uri.parse(uri))
            this.startActivity(i)
        }else{

            val i  = Intent(Intent.ACTION_VIEW);
            val  uri = "whatsapp://send?phone="+phone58+"&text="+conductor?.name +" ${conductor?.lastname} ¡Hola!\n"
            i.setData(Uri.parse(uri))
            this.startActivity(i)
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
        //TRAE LA INFO DE LOS CONDUCTORES*****************
    private fun getConductores() {
        driverProvider.getConductorById(extraId).addOnSuccessListener { document ->

            if (document.exists()) {
                var activo = true
                val listaTipo = arrayListOf("Carro","Moto")
                var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaTipo)
                binding.spTipoVehi?.adapter = adaptadorSp
                conductor = document.toObject(Driver::class.java)
                binding.textViewIdConduc.text = conductor?.id
                binding.textViewNameCond.text = conductor?.name
                binding.textNombreCondu.setText(conductor?.name)
                binding.textApellidoCondu.setText(conductor?.lastname)
                binding.textEmailCondu.text= conductor?.email.toString()
                binding.textTelefonoConduc.setText(conductor?.phone.toString())
                binding.textColorCarro.setText(conductor?.colorCar)
                binding.textViewPlaca.setText(conductor?.plateNumber)
                binding.textViewMarca.setText(conductor?.brandCar)

                activo = conductor?.activado ?: false
                binding.checkboxActivado.isChecked = activo

                if (conductor?.image != null) {
                    if (conductor?.image != "") {
                        Glide.with(this).load(conductor?.image).into(binding.circleImageProfileConduc)
                    }
                }
                //seleccion la posicion que va a cargar el spiner******
                if (conductor?.tipo== "Carro"){
                    binding.spTipoVehi.setSelection(0)
                }
                if (conductor?.tipo== "Moto"){
                    binding.spTipoVehi.setSelection(1)
                }

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
}