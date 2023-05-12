package com.carlosvicente.gaugegrafico.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.carlosvicente.gaugegrafico.R
import com.carlosvicente.gaugegrafico.databinding.ActivityFragmentBinding
import com.carlosvicente.gaugegrafico.fragments.PrimerFragment
import com.carlosvicente.gaugegrafico.fragments.SegundoFragment
import com.carlosvicente.gaugegrafico.fragments.TercerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class fragmentActivity : AppCompatActivity() {

    lateinit var binding: ActivityFragmentBinding

    lateinit var navigation : BottomNavigationView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigation= findViewById(R.id.navMenu)
        cargarToolBar()
        CargarFragment()

        }

    private fun CargarFragment() {
        navigation.setOnItemSelectedListener{
            when (it.itemId) {
                R.id.itemClientes -> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = PrimerFragment() // Reemplaza MyFragment con el nombre de tu Fragment
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.itemConectados -> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = SegundoFragment() // Reemplaza MyFragment con el nombre de tu Fragment
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.itemHistorias-> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = TercerFragment() // Reemplaza MyFragment con el nombre de tu Fragment
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.itemSolicitudes-> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = TercerFragment() // Reemplaza MyFragment con el nombre de tu Fragment
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.commit()
                    return@setOnItemSelectedListener true
                }
                R.id.itemCanceladas-> {
                    val fragmentManager = supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    val fragment = TercerFragment() // Reemplaza MyFragment con el nombre de tu Fragment
                    fragmentTransaction.replace(R.id.fragmentContainer, fragment)
                    fragmentTransaction.commit()
                    return@setOnItemSelectedListener true
                }

            }
            false
        }
    }
    // carga Tool bar************
    private fun cargarToolBar() {
        //CORREGIR EL ACTION BAR
        val actionBar = (this as AppCompatActivity).supportActionBar
        if (actionBar != null) {
            // El tema actual utiliza ActionBar
            Log.d("TEMA", "ENTRO A TEMA CON ACTION VAR")
        } else {
            Log.d("TEMA", "ENTRO A TEMA SIN ACTION VAR")
            //badge= BadgeDrawable.create(this)
            setSupportActionBar(binding.toolbar)
        }

        supportActionBar?.title = "Tres Fragment "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setTitleTextColor(Color.WHITE)
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu1,menu)

        // BadgeUtils.attachBadgeDrawable(badge,binding.toolbar, R.id.item1)

        //  badge.number = contador
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.item1->{
                // badge.number= contador++
                Toast.makeText(this, "Presiono el boton", Toast.LENGTH_SHORT).show()
                goToInicio()

            }
            R.id.itemCerrarSecion->{
                Toast.makeText(this, "Presiono el Boton 2", Toast.LENGTH_SHORT).show()
            }
            R.id.itemConductores->{
                Toast.makeText(this, "Presiono el Boton Perfil", Toast.LENGTH_SHORT).show()
            }
            R.id.itemBilletera->{
                Toast.makeText(this, "Presiono el Boton Busqueda", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun goToInicio() {
            val i = Intent(this, MapActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)

    }


}