package com.carlosvicente.gaugegrafico.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File

class ClientesDetailActivity : AppCompatActivity() {

    private val rotateOpen:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_open_anim) }
    private val rotateClose:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.rotate_close_anim) }
    private val fromBottom:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.from_bottom_anim) }
    private val toBottom:Animation by lazy { AnimationUtils.loadAnimation(this,R.anim.to_bottom_anim) }


    val clientProvider = ClientProvider()
    private lateinit var binding: ActivityClientesDetailBinding
    private var clienteProvider = ClientProvider()
    private var extraId = ""
    private var progressDialog = ProgressDialogFragment
    var cliente:Client? = null
    val REQUEST_PHONE_CALL = 30
    var seleccion = ""
    private var clicked = false
    private var forma = false
    private var imageFile: File? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClientesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

       // progressDialog.showProgressBar(this)
        extraId = intent.getStringExtra("id")!!
        getCliente()

        binding.imageViewBack.setOnClickListener { finish() }
        binding.iconTelefono.setOnClickListener {

            llamarPorTelefono()
        }
        binding.iconWhatSapp.setOnClickListener{
            if (cliente?.phone!= null){
                whatSapp(cliente?.phone!!)
            }
        }
        binding.addbtn.setOnClickListener{
        onAddButtonClicked()
        }
        binding.floatingActionEdit.setOnClickListener{
            updateInfo()
        }
        binding.floatingActionDelete.setOnClickListener {
            Toast.makeText(this, "No se puede Borrar un cliente!", Toast.LENGTH_SHORT).show()
        }
        binding.circleImageProfileCliente.setOnClickListener{
            selectImage()
        }

    }

    //SELECIONA LA FOTO
    private fun selectImage() {
        ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080,1080)
            .createIntent { intent ->
                startImageForResult.launch(intent)
            }
    }
    //SELECCIONA LA FOTO*******
    private val startImageForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

        val resultCode = result.resultCode
        val data = result.data

        if (resultCode == Activity.RESULT_OK) {
            val fileUri = data?.data
            imageFile = File(fileUri?.path)
            binding.circleImageProfileCliente.setImageURI(fileUri)
        }
        else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_LONG).show()
        }
        else {
            Toast.makeText(this, "Tarea cancelada", Toast.LENGTH_LONG).show()
        }

    }

    //ACTUALIZA LA INFORMACION DEL CLIENTE
    private fun updateInfo() {
        progressDialog.showProgressBar(this)
        val name = binding.textNombreClienteDetail.text.toString()
        val lastname = binding.textApellidoClienteDetail.text.toString()
        val phone = binding.textTelefonoCliente.text.toString()
        val idCliente = binding.textViewIdCliente.text.toString()
        val billetera = binding.textBilleteraCliente.text.toString().toDouble()

        val client = Client(
            id = idCliente,
            name = name,
            lastname = lastname,
            phone = phone,
            billetera=billetera,

        )

        if (imageFile != null) {
            clientProvider.uploadImage(idCliente, imageFile!!).addOnSuccessListener { taskSnapshot ->
                clientProvider.getImageUrl().addOnSuccessListener { url ->
                    val imageUrl = url.toString()
                    client.image = imageUrl

                    clientProvider.update(client).addOnCompleteListener {
                        if (it.isSuccessful) {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@ClientesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                        }
                        else {
                            progressDialog.hideProgressBar(this)
                            Toast.makeText(this@ClientesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                        }
                    }
                    Log.d("STORAGE", "$imageUrl")
                }
            }
        }
        else {
            clientProvider.update(client).addOnCompleteListener {
                if (it.isSuccessful) {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@ClientesDetailActivity, "Datos actualizados correctamente", Toast.LENGTH_LONG).show()
                }
                else {
                    progressDialog.hideProgressBar(this)
                    Toast.makeText(this@ClientesDetailActivity, "No se pudo actualizar la informacion", Toast.LENGTH_LONG).show()
                }
            }
        }


    }

    //ENVIAR MSJ DE WHATSAPP*******YO******
    private fun whatSapp (phone: String){
        var phone58 = phone
        val cantNrotlf = phone.length // devuelve 10
        if (cantNrotlf<=11){
            val phone58 = "058$phone"
            val i  = Intent(Intent.ACTION_VIEW);
            val  uri = "whatsapp://send?phone="+phone58+"&text="+cliente?.name +" ¡Hola!\n" +
                    "\n" +
                    "Quiero agradecerte por descargar nuestra aplicación. ¡Es genial tenerte como parte de nuestra comunidad!\n" +
                    "\n" +
                    "Esperamos que puedas probarla y descubrir por ti mismo lo fácil que es usarla.\n" +
                    "\n" +
                    "Si tienes algún problema o sugerencia, no dudes en ponerte en contacto con nuestro equipo de soporte. Estamos siempre dispuestos a ayudar.\n" +
                    "\n" +
                    "¡Gracias de nuevo por descargar nuestra aplicación! Esperamos que la disfrutes.\n" +
                    "\n" +
                    "TAXI AHORA";
            i.setData(Uri.parse(uri))
            this.startActivity(i)
        }else{

            val i  = Intent(Intent.ACTION_VIEW);
            val  uri = "whatsapp://send?phone="+phone58+"&text="+cliente?.name +" ¡Hola!\n" +
                    "\n" +
                    "Quiero agradecerte por descargar nuestra aplicación. ¡Es genial tenerte como parte de nuestra comunidad!\n" +
                    "\n" +
                    "Esperamos que puedas probarla y descubrir por ti mismo lo fácil que es usarla.\n" +
                    "\n" +
                    "Si tienes algún problema o sugerencia, no dudes en ponerte en contacto con nuestro equipo de soporte. Estamos siempre dispuestos a ayudar.\n" +
                    "\n" +
                    "¡Gracias de nuevo por descargar nuestra aplicación! Esperamos que la disfrutes.\n" +
                    "\n" +
                    "TAXI AHORA";
            i.setData(Uri.parse(uri))
            this.startActivity(i)
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
                binding.textViewIdCliente.setText(cliente?.id)
                binding.textNombreClienteDetail.setText(cliente?.name)
                binding.textApellidoClienteDetail.setText(cliente?.lastname)
                binding.textEmailCliente.text= cliente?.email.toString()
                binding.textTelefonoCliente.setText(cliente?.phone.toString())
                binding.textViewNameCond.text = cliente?.name +" "+ cliente?.lastname
                binding.textBilleteraCliente.text= cliente?.billetera.toString()
                if (cliente?.image != null) {
                    if (cliente?.image != "") {
                        Glide.with(this).load(cliente?.image).into(binding.circleImageProfileCliente)
                    }
                }else{
                    Toast.makeText(this, "Sin Foto", Toast.LENGTH_SHORT).show()
                }
                //getDriverInfo(conductor?.id!!)
            }

        }
    }

}