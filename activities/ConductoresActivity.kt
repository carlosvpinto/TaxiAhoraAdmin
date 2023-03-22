package com.carlosvicente.gaugegrafico.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.adapters.ConductoresAdapter
///import com.carlosvicente.gaugegrafico.adapters.HistoriesAdapter
import com.carlosvicente.gaugegrafico.databinding.ActivityConductoresBinding
import com.carlosvicente.gaugegrafico.databinding.ActivityHistoryBinding
import com.carlosvicente.gaugegrafico.models.Driver
import com.carlosvicente.gaugegrafico.models.History
import com.carlosvicente.gaugegrafico.providers.DriverProvider
import com.carlosvicente.gaugegrafico.providers.HistoryProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList

class ConductoresActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConductoresBinding
    private var driverProvider = DriverProvider()
    private var conductores = ArrayList<Driver>()
    private lateinit var adapter: ConductoresAdapter
    private var spConductores: Spinner? = null
    private var txtFiltro: EditText? = null
    var campo:String? = null



    val db2 = FirebaseFirestore.getInstance()
    val collection = db2.collection("Drivers")




    private var progressDialog = ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityConductoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog.showProgressBar(this)

        spConductores= findViewById(R.id.spinnerConductor)
        txtFiltro = findViewById(R.id.editTexFiltro)
        val listaConductores= arrayListOf("ID","Nombre" ,"Apellido","Email","Placa","Color")
        var adaptadorSp: ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_spinner_item,listaConductores)
        spConductores?.adapter= adaptadorSp


        binding.spinnerConductor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
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
                if (position== 4){campo="placa"}
                if (position== 5){campo="color"}

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@ConductoresActivity, "No seleccionaste nada", Toast.LENGTH_SHORT).show()
            }

        }



        //progressDialog.showProgressBar(this)
        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerViewConductores.layoutManager = linearLayoutManager
        binding.editTexFiltro.addTextChangedListener {userfilter ->
            if (campo=="id"){
                val conductoresFiltrados =  conductores.filter { conductor->

                conductor.id?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }
            if (campo=="name"){
                val conductoresFiltrados =  conductores.filter { conductor->
                conductor.name?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }
            if (campo== "apellido"){
                val conductoresFiltrados =  conductores.filter { conductor->
                    conductor.lastname?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }
            if (campo=="email"){
                val conductoresFiltrados =  conductores.filter { conductor->

                    conductor.email?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }

            if (campo== "placa"){
                val conductoresFiltrados =  conductores.filter { conductor->
                   // conductor.plateNumber!!.lowercase().contains(userfilter.toString().lowercase())}
                    conductor.plateNumber?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }
            if (campo== "Color"){
                val conductoresFiltrados =  conductores.filter { conductor->
                conductor.brandCar?.lowercase()?.contains(userfilter.toString().lowercase()) == true}
                adapter.updateConductor(conductoresFiltrados)
            }

        }


        getConductor()


    }


    private fun getConductor() {
        conductores.clear()

        driverProvider.getDriver().get().addOnSuccessListener { query ->

            if (query != null) {
                if (query.documents.size > 0) {
                    val documents = query.documents

                    for (d in documents) {
                        var driver = d.toObject(Driver::class.java)
                        driver?.id = d.id
                        conductores.add(driver!!)
                    }

                    adapter = ConductoresAdapter(this@ConductoresActivity, conductores)
                    binding.recyclerViewConductores.adapter = adapter
                }
            }
            progressDialog.hideProgressBar(this)
        }
    }

}