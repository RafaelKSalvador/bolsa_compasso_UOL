package com.example.newsnow.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsnow.utils.NewsNow
import com.example.newsnow.model.Article
import com.example.newsnow.model.NewsResponse
import com.example.newsnow.repository.NewsRepository
import com.example.newsnow.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {


    val breakingNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1


    init {
        getBreakingNews("br")
    }


    //função get para pegar as últimas notícias
    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    //função de pesquisa para notícias
    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleBreakingNewsResponse(response))
    }


//    //chamada da função BreakingNews e tratamento de erros para testar a conexão
//    private suspend fun safeBreakingNewsCall(countryCode: String) {
//        breakingNews.postValue(Resource.Loading())
//
//        try {
//            if (hasInternetConnection()) {
//                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
//                breakingNews.postValue(handleBreakingNewsResponse(response))
//            } else {
//                breakingNews.postValue(Resource.Error("No internet connection"))
//            }
//
//        } catch (t: Throwable) {
//            when (t) {
//                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
//                is UnknownHostException -> breakingNews.postValue(Resource.Error("Unknown host!"))
//                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
//            }
//        }
//    }

//    //chamada da função Search e tratamento de erros para testar a conexão
//    private suspend fun safeSearchNewsCall(searchQuery: String) {
//        searchNews.postValue(Resource.Loading())
//
//        try {
//            if (hasInternetConnection()) {
//                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
//                searchNews.postValue(handleSearchNewsResponse(response))
//            } else {
//                searchNews.postValue(Resource.Error("No internet connection"))
//            }
//
//        } catch (t: Throwable) {
//            when (t) {
//                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
//                else -> searchNews.postValue(Resource.Error("Conversion Error"))
//            }
//        }
//    }


    //trabalhando com a resposta da função Breaking News
    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    //trabalhando com a resposta da função Search News
    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

   fun getSavedArticles() = newsRepository.getSavedNews()

   fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

//    //função para testar a conexão com a rede
//    private fun hasInternetConnection(): Boolean {
//        val connectivityManager = getApplication<NewsNow>().getSystemService(
//                Context.CONNECTIVITY_SERVICE
//        ) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val activityNetwork = connectivityManager.activeNetwork ?: return false
//            val capabilities = connectivityManager.getNetworkCapabilities(activityNetwork) ?: return false
//            return when {
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
//                else -> false
//            }
//        } else {
//            connectivityManager.activeNetworkInfo?.run {
//                return when(type) {
//                    ConnectivityManager.TYPE_WIFI -> true
//                    ConnectivityManager.TYPE_MOBILE -> true
//                    ConnectivityManager.TYPE_ETHERNET -> true
//                    else -> false
//                }
//            }
//        }
//        return false
//    }
}