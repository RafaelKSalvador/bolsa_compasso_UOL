package com.example.newsnow.api

import com.example.newsnow.model.NewsResponse
import com.example.newsnow.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//O endpoint everything - pesquisa todos os artigos publicados por mais de 75.000 fontes diferentes,
// grandes e pequenas, nos últimos 3 anos.
//O endpoint top-headlines - retorna manchetes de notícias de última hora para países,
// categorias e editores específicos.

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String = "in",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apikey:String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/top-headlines")
    suspend fun getTechNews(
        @Query("sources")
        source:String = "techcrunch",
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apikey:String = API_KEY
    ): Response<NewsResponse>


    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String ,
        @Query("page")
        pageNumber:Int = 1,
        @Query("apiKey")
        apikey:String = API_KEY
    ): Response<NewsResponse>

}