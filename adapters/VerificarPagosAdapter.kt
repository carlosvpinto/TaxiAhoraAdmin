package com.carlosvicente.gaugegrafico.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.activities.ClientesDetailActivity
import com.carlosvicente.gaugegrafico.activities.HistoriesDetailActivity
import com.carlosvicente.gaugegrafico.activities.PagoMovilDetailActivity
import com.carlosvicente.gaugegrafico.activities.SolicitudesDetailActivity
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.PagoMovil
import com.carlosvicente.gaugegrafico.models.Solicitudes
import com.carlosvicente.gaugegrafico.providers.GeoProvider
import com.carlosvicente.gaugegrafico.providers.PagoMovilProvider
import com.carlosvicente.gaugegrafico.utils.RelativeTime
import com.tommasoberlose.progressdialog.ProgressDialogFragment

private var progressDialog = ProgressDialogFragment
private var pagoMovilProvider = PagoMovilProvider()
private var geoProvider = GeoProvider()
var verificator = false
var posicionList = 0

class VerificarPagosAdapter(val context: Activity, var pagoMoviles: ArrayList<PagoMovil>): RecyclerView.Adapter<VerificarPagosAdapter.PagoMovilAdapterViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoMovilAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_verificardepositos, parent, false)
        return PagoMovilAdapterViewHolder(view)
    }

    // ESTABLECER LA INFORMACION
    override fun onBindViewHolder(holder: PagoMovilAdapterViewHolder, position: Int) {

        val pagoRealizado =  pagoMoviles[position] // UN SOLO HISTORIAL
//        holder.textIdSolicitud.text= pagosRealizados.id
        if (pagoRealizado.timestamp!=null){
            holder.texTimestamp.text = RelativeTime.getTimeAgo(pagoRealizado.timestamp!!, context)// AQUI VA TIME STAMP
        }
        holder.textIdCliente.text = pagoRealizado.idClient
        holder.textFechaDeposito.text = pagoRealizado.date.toString()
        holder.textMontoBs.text = pagoRealizado.montoBs.toString()
        holder.textMontoDollar.text = pagoRealizado.montoDollar.toString()
        holder.textViewNroRecibo.text = pagoRealizado.nro.toString()
        holder.checkboxVerificacion.isChecked = pagoRealizado.verificado!!

        holder.itemView.setOnClickListener { goToDetail(pagoRealizado?.id!!) }
        holder.checkboxVerificacion.setOnClickListener{
            val pagoRealizado =  pagoMoviles[position]
            val verificar = pagoRealizado.id
            posicionList=  position


             verificator = holder.checkboxVerificacion.isChecked
            val id = holder.textIdCliente.text.toString()


            vericarpago(verificar.toString())
           (pagoRealizado?.id)}
    }
    private fun vericarpago(id:String){
       // removeItem(posicionList)
        progressDialog.hideProgressBar(context)
        pagoMovilProvider.updateVerificacion(id,verificator )
        progressDialog.hideProgressBar(context)
        Toast.makeText(context, "Pago Verificado", Toast.LENGTH_SHORT).show()
    }

    fun removeItem(position: Int) {
        pagoMoviles.removeAt(position)
        notifyItemRemoved(position)
    }


    private fun goToDetail(idSolicitud: String) {
        val i = Intent(context, PagoMovilDetailActivity::class.java)
        i.putExtra("id", idSolicitud)
        context.startActivity(i)
    }

    // EL TAMAñO DE LA LISTA QUE VAMOS A MOSTRAR
    override fun getItemCount(): Int {
        val textView = context.findViewById<TextView>(R.id.txtTotalFiltro)
        textView.text= pagoMoviles.size.toString()

        return pagoMoviles.size


    }
    fun updatePagosRealizados(pagosRealizadosList: List<PagoMovil> ){
        this.pagoMoviles = pagosRealizadosList as ArrayList<PagoMovil>
        notifyDataSetChanged()
    }

    class PagoMovilAdapterViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val texTimestamp: TextView
        val textIdCliente: TextView
        val textFechaDeposito: TextView
        val textMontoBs: TextView
        val textMontoDollar: TextView
        val textViewNroRecibo: TextView
        val checkboxVerificacion: CheckBox

        init {

            texTimestamp = view.findViewById(R.id.textViewTimestamp)
            textIdCliente = view.findViewById(R.id.textViewIdClientDep)
            textFechaDeposito = view.findViewById(R.id.textViewFDeposito)
            textMontoBs = view.findViewById(R.id.textViewMontoBs)
            textMontoDollar = view.findViewById(R.id.textViewMontoDollar)
            textViewNroRecibo = view.findViewById(R.id.textViewNroRecibo)
            checkboxVerificacion = view.findViewById(R.id.checkboxVerificacion)


        }

    }
}