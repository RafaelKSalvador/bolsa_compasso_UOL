package com.example.newsnow.repository

import com.example.newsnow.api.RetrofitInstance
import com.example.newsnow.database.ArticleDatabase
import com.example.newsnow.model.Article

class NewsRepository(val db: ArticleDatabase){

    // função get para pegar as últimas notícias
    suspend fun getBreakingNews(countryCode:String, pageNumber:Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)

    // função get para pegar notícias de tecnologia
    suspend fun getTechNews(source:String, pageNumber:Int) =
        RetrofitInstance.api.getTechNews(source, pageNumber)

    // função para fazer a pesquisa de notícias
    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    // função get para salvar notícias
    fun getSavedNews() = db.getArticleDao().getAllArticles()

    // funções de CRUD
    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)
    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)

}