package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.ClientesDetailActivity
import com.carlosvicente.gaugegrafico.activities.HistoriesDetailActivity
import com.carlosvicente.gaugegrafico.activities.SolicitudesDetailActivity
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.carlosvicente.gaugegrafico.utils.RelativeTime

class SolicitudesAdapter(val context: Activity, var solicitudes: ArrayList<Solicitudes>): RecyclerView.Adapter<SolicitudesAdapter.SolicitudesAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_solicitudes, parent, false)
        return SolicitudesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: SolicitudesAdapterViewHolder, position: Int) {

        val solicitud =  solicitudes[position] // UN SOLO HISTORIAL
        holder.textIdSolicitud.text= solicitud.id
        if (solicitud.time!=null){
            holder.textFechaTimestamp.text = RelativeTime.getTimeAgo(solicitud.time!!, context)// AQUI VA TIME STAMP
        }
        holder.textNombreCliSolicitud.text = solicitud.name
        holder.textApellidoClientSolicitud.text = solicitud.lastname
        holder.textIdDriverSolicitud.text = solicitud.idDriver
        holder.textDestinoSolicitud.text = solicitud.destination
        holder.textFechaSolicitud.text = solicitud.fecha.toString()
        holder.itemView.setOnClickListener { goToDetail(solicitud?.id!!) }
    }

    private fun goToDetail(idSolicitud: String) {
        val i = Intent(context, SolicitudesDetailActivity::class.java)
        i.putExtra("id", idSolicitud)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= solicitudes.size.toString()

        return solicitudes.size


    }
    fun updateSolicitudes(solicitudesList: List<Solicitudes> ){
        this.solicitudes = solicitudesList as ArrayList<Solicitudes>
        notifyDataSetChanged()
    }

    class SolicitudesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textIdSolicitud: TextView
        val textNombreCliSolicitud: TextView
        val textApellidoClientSolicitud: TextView
        val textIdDriverSolicitud: TextView
        val textDestinoSolicitud: TextView
        val textFechaSolicitud: TextView
        val textFechaTimestamp: TextView

        init {

            textIdSolicitud = view.findViewById(R.id.textIdSolicitudCard)
            textNombreCliSolicitud = view.findViewById(R.id.textNombreSolicitud)
            textApellidoClientSolicitud = view.findViewById(R.id.textApellidoCSolicitud)
            textIdDriverSolicitud = view.findViewById(R.id.textIdCondSolicitud)
            textDestinoSolicitud = view.findViewById(R.id.textDestinoSolicitud)
            textFechaSolicitud = view.findViewById(R.id.textFechaSolicitud)
            textFechaTimestamp = view.findViewById(R.id.textFechaTimestamp)


        }

    }
}