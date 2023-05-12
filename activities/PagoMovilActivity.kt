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
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.VerificarPagosAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityVerificapagoBinding
import com.carlosvicente.gaugegrafico.models.PagoMovil
import com.carlosvicente.gaugegrafico.providers.AuthProvider
import com.carlosvicente.gaugegrafico.providers.PagoMovilProvider
import com.google.android.material.badge.BadgeDrawable
import com.google.firebase.firestore.ListenerRegistration
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import kotlin.collections.ArrayList

class PagoMovilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificapagoBinding
    private lateinit var badge: BadgeDrawable
    private var contador = 0
    private val authProvider = AuthProvider()

    private var pagoProvider = PagoMovilProvider( )
    private var pagoMoviles = ArrayList<PagoMovil>()
    private lateinit var adapter: VerificarPagosAdapter
    private var spPagoMovil: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null
    private var toll:Toolbar?=null
    var verificado = false
    private var pagoMovilListener: ListenerRegistration? = null
    private lateinit var myActivity :Activity
    private var isActivityVisible = false



    //val db2 = FirebaseFirestore.getInstance()
    //val collection = db2.collection("PagoMovil")




    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityVerificapagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)


        verificado = intent.getBooleanExtra("verificado",verificado)

        cargarToolBar()

        spPagoMovil= findViewById(R.id.spinnerPagosR)
        txtFiltro = findViewById(R.id.editTexFiltroPagosR)

        //binding.imageViewBack.setOnClickListener { finish() }


        val listaPagosMovil= arrayListOf("ID","idClient" ,"montoBs","montoDollar","tazaCambiaria","Fecha","verificado")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaPagosMovil)
        spPagoMovil?.adapter= adaptadorSp


        binding.spinnerPagosR.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                if (position== 0){campo= "id"}
                if (position== 1){campo="idClient"}
                if (position== 2){campo="montoBs"}
                if (position== 3){campo="montoDollar"}
                if (position== 4){campo="tazaCambiaria"}
                if (position== 5){campo="date"}

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@PagoMovilActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }
        }

        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewPagosR.layoutManager = linearLayoutManager
        binding.editTexFiltroPagosR.addTextChangedListener {userfilter ->
            if (campo=="id"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                    pagosRealizados.id?.lowercase()?.contains(userfilter.toString().lowercase())==true}
               // historiaC.idDriver?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                adapter.updatePagosRealizados(pagosFiltrados)
            }
            if (campo=="idClient"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                  //  solicitud.destination!!.lowercase().contains(userfilter.toString().lowercase())}
                    pagosRealizados.idClient?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updatePagosRealizados(pagosFiltrados)
            }
            if (campo== "montoBs"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                    //solicitud.email!!.lowercase().contains(userfilter.toString().lowercase())}
                    pagosRealizados.montoBs.toString()?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updatePagosRealizados(pagosFiltrados)
            }
            if (campo=="montoDollar"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                    //solicitud.fecha!!.toString().lowercase().contains(userfilter.toString().lowercase())}
                    pagosRealizados.montoDollar?.toString()?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updatePagosRealizados(pagosFiltrados)
            }

            if (campo== "tazaCambiaria"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                   // solicitud.idClient!!.lowercase().contains(userfilter.toString().lowercase())}
                    pagosRealizados.tazaCambiaria?.toString()?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updatePagosRealizados(pagosFiltrados)
            }
            if (campo== "date"){
                val pagosFiltrados =  pagoMoviles.filter { pagosRealizados->
                  //  solicitud.idDriver!!.lowercase().contains(userfilter.toString().lowercase())}
                    pagosRealizados.date?.toString()?.lowercase()?.contains(userfilter.toString().lowercase())==true}
                adapter.updatePagosRealizados(pagosFiltrados)
            }

        }


        getPagosVerificado()


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

        supportActionBar?.title = "Verificar Pago Movil "
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


    // Optiene Pago Verificado
    private fun getPagosVerificado() {
        pagoMoviles.clear()

        pagoMovilListener= pagoProvider.getPagoMovilVerificado(verificado)
            .addSnapshotListener { querySnapshot, error ->
            Log.d("verificado", "ENTRO  averificado $querySnapshot")
                if (querySnapshot != null && isActivityVisible && binding.recyclerViewPagosR.isVisible) {
                    Log.d("verificado", "ENTRO  querySnapshot ${binding.recyclerViewPagosR.isVisible} querySnapshot.documents.size ${querySnapshot.documents.size}")
                    pagoMoviles.clear() // Limpiar la lista antes de agregar los nuevos elementos

                    if (querySnapshot.documents.size > 0) {
                        val documents = querySnapshot.documents

                        for (d in documents) {
                            var pagoMovil = d.toObject(PagoMovil::class.java)
                            pagoMovil?.id = d.id
                            pagoMoviles.add(pagoMovil!!)
                        }
                        //adapter?.updatePagosRealizados(pagoMoviles)
                        adapter = VerificarPagosAdapter(this@PagoMovilActivity, pagoMoviles)
                        binding.recyclerViewPagosR.adapter = adapter
                        progressDialog.hideProgressBar(this)
                        //adapter?.notifyDataSetChanged() // Notificar al adaptador después de asignar la nueva lista

                    }

                    progressDialog.hideProgressBar(this)

                }
           // progressDialog.hideProgressBar(this)
        }
    }


    //VA A LA ACTIVITY INDICADA POR EL MENU
    private fun goToActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        intent.putExtra("verificado", verificado)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Eliminar el listener de Firebase cuando se destruye la actividad
        pagoMovilListener?.remove()
        pagoMovilListener = null
    }
    override fun onResume() {
        super.onResume()
        isActivityVisible = true
    }

    override fun onPause() {
        super.onPause()
        isActivityVisible = false
    }


}