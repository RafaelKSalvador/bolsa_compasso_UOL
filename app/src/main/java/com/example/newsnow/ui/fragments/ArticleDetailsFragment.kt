package com.example.newsnow.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_article_details.*
import com.example.newsnow.ui.activities.MainActivity
import com.example.newsnow.R
import com.example.newsnow.ui.viewmodel.NewsViewModel

class ArticleDetailsFragment : Fragment(R.layout.fragment_article_details) {

    lateinit var viewModel: NewsViewModel
    val args: ArticleDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // inicializa o viewModel
        viewModel = (activity as MainActivity).viewModel

        val article = args.article

        webView.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                webViewClient = webViewClient
            }
            loadUrl(article.url)
        }

        // botão para salvar artigo
        save_articles_button.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

    }
}