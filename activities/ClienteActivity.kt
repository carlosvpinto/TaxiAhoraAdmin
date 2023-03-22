package com.carlosvicente.gaugegrafico.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.ClientesAdapter
import com.carlosvicente.gaugegrafico.adapters.ConductoresAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityClienteBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresBinding
import com.carlosvicente.gaugegrafico.models.Client
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.providers.ClientProvider
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.tommasoberlose.progressdialog.ProgressDialogFragment

class ClienteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityClienteBinding
    private var clienteProvider = ClientProvider()
    private var clientes = ArrayList<Client>()
    private lateinit var adapter: ClientesAdapter
    private var spConductores: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null


    val db2 = FirebaseFirestore.getInstance()
    val collection = db2.collection("Client")

    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog.showProgressBar(this)
        spConductores= findViewById(R.id.spinnerCliente)
        txtFiltro = findViewById(R.id.editTexFiltroC)
        val listaClientes= arrayListOf("ID","Nombre" ,"Apellido","Email","Telefono")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaClientes)
        spConductores?.adapter= adaptadorSp


        binding.spinnerCliente.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {

                if (position== 0){campo= "id"}
                if (position== 1){campo="name"}
                if (position== 2){campo="apellido"}
                if (position== 3){campo="email"}
                if (position== 4){campo="telefono"}


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@ClienteActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }

        }

        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewClientes.layoutManager = linearLayoutManager
        binding.editTexFiltroC.addTextChangedListener {userfilter ->
            if (campo=="id"){
                val clientesFiltrados =  clientes.filter { cliente ->
                  //  cliente.id!!.lowercase().contains(userfilter.toString().lowercase())}
                    cliente.id?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateCliente(clientesFiltrados)
            }
            if (campo=="name"){
                val clientesFiltrados =  clientes.filter { cliente->
                  //  conductor.name!!.lowercase().contains(userfilter.toString().lowercase())}
                    cliente.name?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateCliente(clientesFiltrados)

            }
            if (campo== "apellido"){
                val clientesFiltrados =  clientes.filter { cliente->
                   // cliente.lastname!!.lowercase().contains(userfilter.toString().lowercase())}
                    cliente.lastname?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateCliente(clientesFiltrados)
            }
            if (campo=="email"){
                val clientesFiltrados =  clientes.filter { cliente->
                    //cliente.email!!.lowercase().contains(userfilter.toString().lowercase())}
                    cliente.email?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateCliente(clientesFiltrados)
            }
            if (campo=="telefono"){
                val clientesFiltrados =  clientes.filter { cliente->
                    //cliente.email!!.lowercase().contains(userfilter.toString().lowercase())}
                    cliente.phone?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateCliente(clientesFiltrados)
            }


        }


        getCliente()


    }



    private fun getCliente() {
        clientes.clear()

        clienteProvider.getCliente().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var client = d.toObject(Client::class.java)
                        client?.id = d.id
                        clientes.add(client!!)
                    }

                    adapter = ClientesAdapter(this@ClienteActivity, clientes)
                    binding.recyclerViewClientes.adapter = adapter
                }
            }
             progressDialog.hideProgressBar(this)
        }
    }
}