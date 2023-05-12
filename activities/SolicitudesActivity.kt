package com.carlosvicente.gaugegrafico.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.ConductoresAdapter
import com.carlosvicente.gaugegrafico.adapters.SolicitudesAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresBinding
import com.carlosvicente.gaugegrafico.databinding.ActivitySolicitudesBinding
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.carlosvicente.gaugegrafico.providers.AuthProvider
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.SolicitudesProvider
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.firebase.firestore.FirebaseFirestore
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.util.*
import kotlin.collections.ArrayList

class SolicitudesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySolicitudesBinding
    private lateinit var badge: BadgeDrawable
    private var contador = 0
    private val authProvider = AuthProvider()
    private var solicitudesProvider = SolicitudesProvider( )
    private var solicitudes = ArrayList<Solicitudes>()
    private lateinit var adapter: SolicitudesAdapter
    private var spSolicitudes: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null
    private var toll:Toolbar?=null
    private lateinit var myActivity : Activity
    var verificado = false



    val db2 = FirebaseFirestore.getInstance()
    val collection = db2.collection("pagomovil")




    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySolicitudesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)




       cargarToolBar()

        spSolicitudes= findViewById(R.id.spinnerSolicitud)
        txtFiltro = findViewById(R.id.editTexFiltroSoli)

        //binding.imageViewBack.setOnClickListener { finish() }

        val listaSolicitudes= arrayListOf("ID","destino" ,"email","fecha","idCliente","idConductor","apellidoCliente","nombreCliente","origen","telefono")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaSolicitudes)
        spSolicitudes?.adapter= adaptadorSp


        binding.spinnerSolicitud.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position== 0){campo= "id"}
                if (position== 1){campo="destino"}
                if (position== 2){campo="email"}
                if (position== 3){campo="fecha"}
                if (position== 4){campo="idCliente"}
                if (position== 5){campo="idConductor"}
                if (position== 6){campo="apellidoCliente"}
                if (position== 7){campo="nombreCliente"}
                if (position== 8){campo="origen"}
                if (position== 9){campo="telefono"}

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@SolicitudesActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }
        }

        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewSolicitud.layoutManager = linearLayoutManager
        binding.editTexFiltroSoli.addTextChangedListener {userfilter ->
            if (campo=="id"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                    solicitud.id?.lowercase()?.contains(userfilter.toString().lowercase())==true}
               // historiaC.idDriver?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo=="destino"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                  //  solicitud.destination!!.lowercase().contains(userfilter.toString().lowercase())}
                solicitud.destination?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "email"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                    //solicitud.email!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.email?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo=="fecha"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                    //solicitud.fecha!!.toString().lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.fecha?.toString()?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }

            if (campo== "idCliente"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                   // solicitud.idClient!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.idClient?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "idConductor"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                  //  solicitud.idDriver!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.idDriver?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "apellidoCliente"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                  //  solicitud.lastname!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.lastname?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "nombreCliente"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                   // solicitud.name!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.name?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "origen"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                 //   solicitud.origin!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.origin?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }
            if (campo== "telefono"){
                val solicitudesFiltrados =  solicitudes.filter { solicitud->
                   // solicitud.phone!!.lowercase().contains(userfilter.toString().lowercase())}
                    solicitud.phone?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updateSolicitudes(solicitudesFiltrados)
            }

        }


        getSolicitud()


    }


    // carga Tool bar************
    private fun cargarToolBar() {
        //CORREGIR EL ACTION BAR
        val actionBar = (this as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            // El tema actual utiliza ActionBar
            Log.d("TEMA", "ENTRO A TEMA CON ACTION VAR")
        } else {
            Log.d("TEMA", "ENTRO A TEMA SIN ACTION VAR")
            //badge= BadgeDrawable.create(this)
            setSupportActionBar(binding.toolbar)
        }
        //CONFIGURA EL TOOLBAR DE ARRIBA
        supportActionBar?.title = "Solicitudes "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setTitleTextColor(Color.WHITE)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu1,menu)

       // BadgeUtils.attachBadgeDrawable(badge,binding.toolbar, R.id.item1)

      //  badge.number = contador
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when(item.itemId){
            R.id.itemHome->{
                myActivity = MapActivity()
                goToActivity(myActivity)
            }
            R.id.item1->{
                // badge.number= contador++
                myActivity = ProfileActivity()
                goToActivity(myActivity)
            }
            R.id.itemCerrarSecion->{
                authProvider.logout()
                myActivity = MainActivity()
                goToActivity(myActivity)
            }
            R.id.itemConductores->{
                myActivity = ConductoresActivity()
                goToActivity(myActivity)
            }
            R.id.itemVerificados->{
                verificado = true
                myActivity = PagoMovilActivity()
                goToActivity(myActivity)
            }
            R.id.itemSinVerificados->{
                verificado = false
                myActivity = PagoMovilActivity()
                goToActivity(myActivity)
            }

        }
        return super.onOptionsItemSelected(item)
    }
    private fun goToFragment() {
        val i = Intent(this, fragmentActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }


    private fun getSolicitud() {
        solicitudes.clear()

        solicitudesProvider.getSolicitudes().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var solicitud = d.toObject(Solicitudes::class.java)
                        solicitud?.id = d.id
                       // Log.d("solicitud", "solicitud:${solicitud?.name} y ${solicitud?.lastname} ${solicitud?.email} y  ${solicitud?.idClient}  y ${solicitud?.phone}")
                        solicitudes.add(solicitud!!)
                    }

                    adapter = SolicitudesAdapter(this@SolicitudesActivity, solicitudes)
                    binding.recyclerViewSolicitud.adapter = adapter
                }
            }
             progressDialog.hideProgressBar(this)
        }
    }
    //VA A LA ACTIVITY INDICADA POR EL MENU
    private fun goToActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        intent.putExtra("verificado", verificado)
        startActivity(intent)
    }

}