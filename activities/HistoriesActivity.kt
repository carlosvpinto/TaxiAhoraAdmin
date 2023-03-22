package com.carlosvicente.gaugegrafico.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.HistoresAdapter
//import com.carlosvicente.gaugegrafico.adapters.HistoriesAdapter
import com.carlosvicente.gaugegrafico.adapters.HistoryCancelAdapter
//import com.carlosvicente.gaugegrafico.databinding.ActivityHistoriesBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryCancelBinding
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.models.HistoryCancel
import com.carlosvicente.gaugegrafico.providers.HistoryCancelProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class HistoriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private var historyCancelProvider = HistoryCancelProvider()
    private var historyProvider = HistoryProvider()

    private var historias = ArrayList<History>()
    private var historiesFiltradas = ArrayList<History>()
    private lateinit var adapter: HistoresAdapter
    private var spHistorias: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null

    private var progressDialog = ProgressDialogFragment

    var historiasFiltradas: List<History> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)


        spHistorias= findViewById(R.id.spinnerHistorias)
        txtFiltro = findViewById(R.id.editTexFiltro)
        val listaHistorias= arrayListOf("ID","Calificacion Cliente" ,"Calificacion Conductor" ,"Id Conductor" ,"Id Cliente" ,"destino")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaHistorias)
        spHistorias?.adapter= adaptadorSp


        spHistorias?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

                if (position== 0){campo= "id"}
                if (position== 1){campo="calificacionCli"}
                if (position== 2){campo="calificacionCon"}
                if (position== 3){campo="IdConductor"}
                if (position== 4){campo="IdCliente"}
                if (position== 5){campo="destino"}

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@HistoriesActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }

        }



        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistorias.layoutManager = linearLayoutManager
        binding.editTexFiltro.addTextChangedListener {userfilter ->

            if (campo=="id"){
                historiasFiltradas =  historias.filter { historia->
                    historia.id?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }

            }
            if (campo=="calificacionCli"){
                historiasFiltradas = historias.filter { historia->
                    historia.calificationToClient?.toString()?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
            if (campo== "calificacionCon"){
                historiasFiltradas =  historias.filter { historiaC->

                    historiaC.calificationToDriver?.toString()?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
            if (campo=="IdCliente"){
                historiasFiltradas =  historias.filter { historiaC->
                    historiaC.idClient?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }

            if (campo== "IdConductor"){
                historiasFiltradas =  historias.filter { historiaC->
                    historiaC.idDriver?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
            adapter.updateHistory(historiasFiltradas)

        }


        getHistories()
    }

    private fun getHistories() {
        historias.clear()

        historyProvider.getHistorias().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var history = d.toObject(History::class.java)
                        history?.id = d.id
                        historias.add(history!!)
                    }

                    adapter = HistoresAdapter(this@HistoriesActivity, historias)
                    binding.recyclerViewHistorias.adapter = adapter
                }
            }
            progressDialog.hideProgressBar(this)
        }
    }
}