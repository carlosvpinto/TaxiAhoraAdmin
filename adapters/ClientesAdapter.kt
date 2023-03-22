package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.ClienteActivity
import com.carlosvicente.gaugegrafico.activities.ClientesDetailActivity
import com.carlosvicente.gaugegrafico.models.Client
private var totalCliente = 0
class ClientesAdapter(val context: Activity, var clientes: ArrayList<Client>): RecyclerView.Adapter<ClientesAdapter.ClientesAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientesAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_clientes, parent, false)
        return ClientesAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: ClientesAdapterViewHolder, position: Int) {

        val cliente =  clientes[position] // UN SOLO HISTORIAL
        holder.textId.text = cliente.id
        holder.textNombreCliente.text = cliente.name + " " + cliente.lastname
        //holder.textApellidoCliente.text = cliente.lastname
        Glide.with(holder.imgFotoCliente.context).load(cliente.image).into(holder.imgFotoCliente)
        holder.textEmail.text = cliente.email
        holder.tlfCliente.text = cliente.phone

        holder.itemView.setOnClickListener { goToDetail(cliente?.id!!) }
    }

    private fun goToDetail(idConductor: String) {
        val i = Intent(context, ClientesDetailActivity::class.java)
        i.putExtra("id", idConductor)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= clientes.size.toString()
        return clientes.size


    }
    fun updateCliente(clientesList: List<Client> ){
        this.clientes = clientesList as ArrayList<Client>
        notifyDataSetChanged()
    }



    class ClientesAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val textId: TextView
        val textNombreCliente: TextView
        //val textApellidoCliente: TextView
        val textEmail: TextView
        val imgFotoCliente: ImageView
        val tlfCliente:TextView



        init {
            textId= view.findViewById(R.id.textIdCliente)
            textNombreCliente = view.findViewById(R.id.textNombreCliente)
            //textApellidoCliente = view.findViewById(R.id.textApellidoClienteDetail)
            textEmail = view.findViewById(R.id.textEmailCliente2)
            imgFotoCliente= view.findViewById(R.id.imgFotoCliente)
            tlfCliente=view.findViewById(R.id.textTelefonoClienteCard)


        }

    }


}