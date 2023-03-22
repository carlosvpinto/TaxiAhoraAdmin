package com.carlosvicente.gaugegrafico.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.ConductoresAdapter
import com.carlosvicente.gaugegrafico.adapters.ConectadosAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityConectadosBinding
import com.carlosvicente.gaugegrafico.models.Conectado
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.GeoProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class ConectadosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConectadosBinding
    private var driverProvider = DriverProvider()
    private var geoProvider = GeoProvider()
    private var conectadosLista = ArrayList<Conectado>()
    private lateinit var adapter: ConectadosAdapter
    private var spConectados: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null



    val db2 = FirebaseFirestore.getInstance()
    val collection = db2.collection("Drivers")




    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityConectadosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)

        spConectados= findViewById(R.id.spinnerConectados)
        txtFiltro = findViewById(R.id.editTexFiltro)
        val listaConductores= arrayListOf("ID")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaConductores)
        spConectados?.adapter= adaptadorSp


        binding.spinnerConectados.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

                if (position== 0){campo= "id"}


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@ConectadosActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }

        }



        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewConectados.layoutManager = linearLayoutManager
        binding.editTexFiltro.addTextChangedListener {userfilter ->
            if (campo=="id"){
                val conectadosFiltrados =  conectadosLista.filter { conductor->

                    conductor.id?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConectado(conectadosFiltrados)

            }


        }


        getConectados()


    }


    private fun getConectados() {
        conectadosLista.clear()

        geoProvider.getLocationTodos().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var conectado = d.toObject(Conectado::class.java)
                        conectado?.id = d.id
                        conectadosLista.add(conectado!!)
                    }

                    adapter = ConectadosAdapter(this@ConectadosActivity, conectadosLista)
                    binding.recyclerViewConectados.adapter = adapter
                }
            }
            progressDialog.hideProgressBar(this)
        }
    }

}