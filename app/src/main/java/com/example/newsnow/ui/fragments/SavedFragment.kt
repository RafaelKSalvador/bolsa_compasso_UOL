package com.example.newsnow.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsnow.R
import com.example.newsnow.adapter.NewsAdapter
import com.example.newsnow.ui.activities.MainActivity
import com.example.newsnow.ui.viewmodel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_saved.*

class SavedFragment : Fragment(R.layout.fragment_saved) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //inicialização do viewModel
        viewModel = (activity as MainActivity).viewModel


        //inicialização do RecyclerView
        setUpRecyclerView()

        //set onClick listener para o Item do RecyclerView
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedFragment_to_articleDetailsFragment,
                bundle
            )
        }

        //inicialização item touch callback para swipe action, ele permite que você
        //controle quais comportamentos de toque são ativados para cada ViewHolder
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //onSwiped é chamado quando um ViewHolder é deslizado pelo usuário
                //pega a posição do artigo e deleta o mesmo
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                //o Snackbar permite adicionar uma ação a uma mensagem que pode ser respondida
                Snackbar.make(view, "Artigo deletado com sucesso !", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        //anexa o retorno da chamada de deslizar do RecyclerView
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(saved_news_rv)
        }


        //observa a mudança de dados
        viewModel.getSavedArticles().observe(viewLifecycleOwner, Observer { articles ->
            newsAdapter.differ.submitList(articles)
        })

    }

    //inicializa a lista de salvos com um RecyclerView
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        saved_news_rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

}