package com.example.newsnow.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.newsnow.repository.NewsRepository

//Faz a inicialização do NewsViewModel
class NewsViewModelProviderFactory(val app: Application, val newsRepository: NewsRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(app, newsRepository) as T
    }
}