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
import com.carlosvicente.gaugegrafico.activities.SolicitudesDetailActivity
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Solicitudes

class SolicitudesAdapter(val context: Activity, var solicitudes: ArrayList<Solicitudes>): RecyclerView.Adapter<SolicitudesAdapter.SolicitudesAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_solicitudes, parent, false)
        return SolicitudesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: SolicitudesAdapterViewHolder, position: Int) {

        val solicitud =  solicitudes[position] // UN SOLO HISTORIAL
        holder.textIdSolicitud.text = solicitud.id
        holder.textNombreCliSolicitud.text = solicitud.name
        holder.textApellidoClientSolicitud.text = solicitud.lastname
        holder.textTelefonoSolicitud.text = solicitud.phone
        holder.textEmailClientSolicitud.text = solicitud.email
        holder.textIdClientSolicitud.text = solicitud.idClient
        holder.textIdDriverSolicitud.text = solicitud.idDriver
        holder.textDestinoSolicitud.text = solicitud.destination
        holder.textFechaSolicitud.text = solicitud.fecha.toString()
        holder.itemView.setOnClickListener { goToDetail(solicitud?.id!!) }
    }

    private fun goToDetail(idConductor: String) {
        val i = Intent(context, SolicitudesDetailActivity::class.java)
        i.putExtra("id", idConductor)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {

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
        val textTelefonoSolicitud: TextView
        val textEmailClientSolicitud: TextView
        val textIdClientSolicitud: TextView
        val textIdDriverSolicitud: TextView
        val textDestinoSolicitud: TextView
        val textFechaSolicitud: TextView



        init {
            textIdSolicitud= view.findViewById(R.id.textIdSolicitud)
            textNombreCliSolicitud = view.findViewById(R.id.textNombreSolicitud)
            textEmailClientSolicitud = view.findViewById(R.id.textEmailClienSolicitud)
            textApellidoClientSolicitud = view.findViewById(R.id.textApellidoCSolicitud)
            textTelefonoSolicitud = view.findViewById(R.id.textTelefonoSolicitud)
            textIdClientSolicitud = view.findViewById(R.id.textIdClienteSolicitud)
            textIdDriverSolicitud = view.findViewById(R.id.textIdCondSolicitud)
            textDestinoSolicitud = view.findViewById(R.id.textDestinoSolicitud)
            textFechaSolicitud = view.findViewById(R.id.textFechaSolicitud)

        }

    }


}