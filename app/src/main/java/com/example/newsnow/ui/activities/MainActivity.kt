package com.example.newsnow.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

import androidx.navigation.ui.setupWithNavController
import com.example.newsnow.R
import com.example.newsnow.database.ArticleDatabase
import com.example.newsnow.repository.NewsRepository
import com.example.newsnow.ui.viewmodel.NewsViewModel
import com.example.newsnow.ui.viewmodel.NewsViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        // inicialização do bottom navigation
        bottom_navigation.setupWithNavController(nav_host_fragment.findNavController())

        // implementação do menu lateral
        navController = findNavController(R.id.nav_host_fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        configuraDrawble()
    }

    private fun configuraDrawble() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.abrir, R.string.fechar)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.mapa_menu_item -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}