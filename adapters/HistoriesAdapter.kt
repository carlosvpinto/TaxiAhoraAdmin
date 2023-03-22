package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.HistoriesDetailActivity
import com.carlosvicente.gaugegrafico.activities.HistoryCancelDetailActivity
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.models.HistoryCancel
import com.carlosvicente.gaugegrafico.utils.RelativeTime

class HistoresAdapter(val context: Activity, var historias: ArrayList<History>): RecyclerView.Adapter<HistoresAdapter.HistoryAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_history, parent, false)
        return HistoryAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoryAdapterViewHolder, position: Int) {

        val histoy =  historias[position] // UN SOLO HISTORIAL


        holder.textIdHistorias.text = histoy.id
        holder.textFechaHistorias.text = histoy.date.toString()
        holder.textIdClientHistorias.text = histoy.idClient
        holder.textIdCondHisytorias.text = histoy.idDriver
        holder.textDestinoHistorias.text = histoy.destination


        holder.itemView.setOnClickListener { goToDetail(histoy?.id!!) }
    }

    private fun goToDetail(idHistoria: String) {
        val i = Intent(context, HistoriesDetailActivity::class.java)
        i.putExtra("id", idHistoria)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {

        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= historias.size.toString()
        return historias.size


    }
    fun updateHistory(historysList: List<History> ){
        this.historias = historysList as ArrayList<History>
        notifyDataSetChanged()
    }



    class HistoryAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textIdHistorias: TextView
        val textFechaHistorias: TextView
        val textIdClientHistorias: TextView
        val textIdCondHisytorias: TextView
        val textDestinoHistorias: TextView


        init {
            textIdHistorias= view.findViewById(R.id.textIdHistorias)
            textFechaHistorias= view.findViewById(R.id.textFechaHistorias)
            textIdClientHistorias = view.findViewById(R.id.textIdCHistorias)
            textIdCondHisytorias = view.findViewById(R.id.textIdCondHIstorias)
            textDestinoHistorias= view.findViewById(R.id.textDestinoHistoria)

        }

    }


}