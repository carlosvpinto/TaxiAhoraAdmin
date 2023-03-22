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
//import com.carlosvicente.gaugegrafico.adapters.HistoriesAdapter
import com.carlosvicente.gaugegrafico.adapters.HistoryCancelAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryCancelBinding
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.models.HistoryCancel
import com.carlosvicente.gaugegrafico.providers.HistoryCancelProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class HistoryCancelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryCancelBinding
    private var historyCancelProvider = HistoryCancelProvider()
    private var historiesCancel = ArrayList<HistoryCancel>()
    private var historiesFiltradas = ArrayList<HistoryCancel>()
    private lateinit var adapter: HistoryCancelAdapter
    private var spHistoriasCancel: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null

    private var progressDialog = ProgressDialogFragment

    var historiasFiltradas: List<HistoryCancel> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryCancelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)
        progressDialog.hideProgressBar(this)

        spHistoriasCancel= findViewById(R.id.spinnerHistoriaCancel)
        txtFiltro = findViewById(R.id.editTexFiltro)
        val listaHistoriasCancel= arrayListOf("ID","causa" ,"destino","IdCliente","IdConductor")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaHistoriasCancel)
        spHistoriasCancel?.adapter= adaptadorSp


        spHistoriasCancel?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

                if (position== 0){campo= "id"}
                if (position== 1){campo="causa"}
                if (position== 2){campo="destino"}
                if (position== 3){campo="IdCliente"}
                if (position== 4){campo="IdConductor"}

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@HistoryCancelActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }

        }



        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewHistoriesCancel.layoutManager = linearLayoutManager
        binding.editTexFiltro.addTextChangedListener {userfilter ->

            if (campo=="id"){
                 historiasFiltradas =  historiesCancel.filter { historiaC->
                     historiaC.id?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                 }

            }
            if (campo=="causa"){
                historiasFiltradas = historiesCancel.filter { historiaC->
                    historiaC.causa?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
            if (campo== "destino"){
                historiasFiltradas =  historiesCancel.filter { historiaC->

                    historiaC.destination?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
            if (campo=="IdCliente"){
                 historiasFiltradas =  historiesCancel.filter { historiaC->
                     historiaC.idClient?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                 }
            }

            if (campo== "IdConductor"){
                historiasFiltradas =  historiesCancel.filter { historiaC->
                    historiaC.idDriver?.lowercase()?.contains(userfilter.toString().lowercase()) == true
                }
            }
                adapter.updateHistoryCancel(historiasFiltradas)

        }


        getHistories()
    }

    private fun getHistories() {
        historiesCancel.clear()

        historyCancelProvider.getHistoryCancel().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var history = d.toObject(HistoryCancel::class.java)
                        history?.id = d.id
                        historiesCancel.add(history!!)
                    }

                    adapter = HistoryCancelAdapter(this@HistoryCancelActivity, historiesCancel)
                    binding.recyclerViewHistoriesCancel.adapter = adapter
                }
            }
            progressDialog.hideProgressBar(this)
        }
    }
}