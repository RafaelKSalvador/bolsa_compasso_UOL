package com.example.newsnow.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsnow.model.Article


@Dao
interface ArticleDao {

    //atualização ou inserção de artigos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article):Long

    //pega todas as notícias do banco de dados
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    //deleta o artigo do database
    @Delete
    suspend fun deleteArticle(article: Article)

}