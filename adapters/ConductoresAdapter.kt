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
import com.carlosvicente.gaugegrafico.activities.ConductoresDetailActivity
import com.carlosvicente.gaugegrafico.models.Driver

class ConductoresAdapter(val context: Activity, var conductores: ArrayList<Driver>): RecyclerView.Adapter<ConductoresAdapter.ConductoresAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConductoresAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_conductores, parent, false)
        return ConductoresAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: ConductoresAdapterViewHolder, position: Int) {

        val conductor =  conductores[position] // UN SOLO HISTORIAL
       // Glide.with(binding.ivSuperHero.context).load(superHeroModel.photo).into(binding.ivSuperHero)
        Glide.with(holder.imgFotoConductor.context).load(conductor.image).into(holder.imgFotoConductor)
        holder.textId.text = conductor.id
        holder.textNombreConductor.text = conductor.name +" "+ conductor.lastname
        holder.textEmail.text = conductor.email

        holder.itemView.setOnClickListener { goToDetail(conductor?.id!!) }
    }

    private fun goToDetail(idConductor: String) {
        val i = Intent(context, ConductoresDetailActivity::class.java)
        i.putExtra("id", idConductor)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        return conductores.size


    }
    fun updateConductor(conductoresList: List<Driver> ){
        this.conductores = conductoresList as ArrayList<Driver>
        notifyDataSetChanged()
    }



    class ConductoresAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textId: TextView
        val textNombreConductor: TextView
        val textEmail: TextView
        val imgFotoConductor: ImageView

        init {
            textId= view.findViewById(R.id.textIdConductor)
            textNombreConductor = view.findViewById(R.id.textNombreConductor)
            textEmail = view.findViewById(R.id.textEmailConductor)
            imgFotoConductor= view.findViewById(R.id.imgFotoConductor)
        }

    }


}