package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.ConductoresDetailActivity
import com.carlosvicente.gaugegrafico.activities.HistoryCancelDetailActivity
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.HistoryCancel
import com.carlosvicente.gaugegrafico.utils.RelativeTime

class HistoryCancelAdapter(val context: Activity, var historysCancel: ArrayList<HistoryCancel>): RecyclerView.Adapter<HistoryCancelAdapter.HistoryAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_historycancel, parent, false)
        return HistoryAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: HistoryAdapterViewHolder, position: Int) {

        val histoyCancel =  historysCancel[position] // UN SOLO HISTORIAL


        holder.textIdHistoryCancel.text = histoyCancel.id
        if (histoyCancel.timestamp!=null){
            holder.textHistoriaCancelTimestamp.text = RelativeTime.getTimeAgo(histoyCancel.timestamp!!, context)// AQUI VA TIME STAMP
        }
        holder.textFechaHistoryCancel.text = histoyCancel.fecha.toString()

        holder.textIdClientHCancel.text = histoyCancel.idClient
        holder.textIdCondCancelada.text = histoyCancel.idDriver
        holder.textDestinoCancel.text = histoyCancel.destination
        holder.textCausaCancel.text = histoyCancel.causa

        holder.itemView.setOnClickListener { goToDetail(histoyCancel?.id!!) }
    }

    private fun goToDetail(idHistoriCancel: String) {
        val i = Intent(context, HistoryCancelDetailActivity::class.java)
        i.putExtra("id", idHistoriCancel)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {

        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= historysCancel.size.toString()
        return historysCancel.size


    }
    fun updateHistoryCancel(conductoresList: List<HistoryCancel> ){
        this.historysCancel = conductoresList as ArrayList<HistoryCancel>
        notifyDataSetChanged()
    }



    class HistoryAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textIdHistoryCancel: TextView
        val textFechaHistoryCancel: TextView
        val textIdClientHCancel: TextView
        val textIdCondCancelada: TextView
        val textDestinoCancel: TextView
        val textCausaCancel: TextView
        val textHistoriaCancelTimestamp: TextView


        init {
            textIdHistoryCancel= view.findViewById(R.id.textIdHistoryCancel)
            textFechaHistoryCancel= view.findViewById(R.id.textFechaCancel)
            textIdClientHCancel = view.findViewById(R.id.textIdCCanelada)
            textIdCondCancelada = view.findViewById(R.id.textIdCondCancelada)
            textDestinoCancel= view.findViewById(R.id.textDestinoCancel)
            textCausaCancel= view.findViewById(R.id.textCausaCancel)
            textHistoriaCancelTimestamp= view.findViewById(R.id.textHistoCancelTimetamp)
        }

    }


}