package com.example.newsnow.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.newsnow.R
import com.example.newsnow.database.ArticleDatabase
import com.example.newsnow.repository.NewsRepository
import com.example.newsnow.ui.viewmodel.NewsViewModel
import com.example.newsnow.ui.viewmodel.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(NewsViewModel::class.java)

//        // inicialização do bottom navigation
//        bottom_navigation.setupWithNavController(nav_host_fragment.findNavController())

    }
}