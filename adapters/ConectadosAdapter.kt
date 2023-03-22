package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.ConductoresDetailActivity
import com.carlosvicente.gaugegrafico.models.Conectado
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.providers.GeoProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider

class ConectadosAdapter(val context: Activity, var conectadosList: MutableList<Conectado>): RecyclerView.Adapter<ConectadosAdapter.ConectadosAdapterViewHolder>() {

    var posicionList = 0
    var idConectados:String? = null
    private var geoProvider = GeoProvider()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConectadosAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_conectados, parent, false)
        return ConectadosAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: ConectadosAdapterViewHolder, position: Int) {

        val conectados =  conectadosList[position] // UN SOLO HISTORIAL
       // Glide.with(binding.ivSuperHero.context).load(superHeroModel.photo).into(binding.ivSuperHero)
        holder.textId.text = conectados.id
        //holder.textEmail.text= conectados.g

        holder.imgDelete.setOnClickListener {
            posicionList=  position
            borrarConectado(conectados.id!!)}

        holder.itemView.setOnClickListener { goToDetail(conectados?.id!!) }
    }

    private fun borrarConectado(idConectado: String) {
            removeItem(posicionList)
            geoProvider.removeLocation(idConectado)


    }

    private fun goToDetail(idConectado: String) {
        val i = Intent(context, ConductoresDetailActivity::class.java)
        i.putExtra("id", idConectado)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= conectadosList.size.toString()
        return conectadosList.size


    }
    fun updateConectado(conductoresList: List<Conectado> ){
        this.conectadosList = conductoresList as ArrayList<Conectado>
        notifyDataSetChanged()
    }

//    fun addItem(item: String) {
//        conectadosList.add(item)
//        notifyItemInserted(conectadosList.size - 1)
//    }

    fun removeItem(position: Int) {
        conectadosList.removeAt(position)
        notifyItemRemoved(position)
    }



    class ConectadosAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textId: TextView
        val imgDelete: ImageView
//        val textNombreConductor: TextView
//        val textEmail: TextView
//        val imgFotoConductor: ImageView

        init {
            textId= view.findViewById(R.id.textIdConductorConect)
            imgDelete= view.findViewById(R.id.imgBorrar)
//            textNombreConductor = view.findViewById(R.id.textNombreConductor)
//            textEmail = view.findViewById(R.id.textEmailConductor)
//            imgFotoConductor= view.findViewById(R.id.imgFotoConductor)
        }

    }


}