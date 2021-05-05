package com.example.newsnow.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsnow.ui.activities.MainActivity
import com.example.newsnow.R
import com.example.newsnow.adapter.NewsAdapter
import com.example.newsnow.ui.viewmodel.NewsViewModel
import com.example.newsnow.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.example.newsnow.utils.Resource
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //inicializa o recyclerview
        setUpRecyclerView()

        //set onClick listener o item do recyclerview
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
//            findNavController().navigate(
//                R.id.action_homeFragment_to_articleDetailsFragment,
//                bundle
//            )
        }

        viewModel = (activity as MainActivity).viewModel


        viewModel.techNews.observe(viewLifecycleOwner, Observer {  response ->

            when(response){

                is Resource.Loading ->{
                    showProgressBar()
                }

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let {  newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.techNewsPage == totalPages
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let {message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }

        })

    }


    private fun hideProgressBar() {
        news_progress.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        news_progress.visibility = View.VISIBLE
        isLoading = true
    }


    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getTechNews("techcrunch")
                isScrolling = false
            } else {
                top_news_rv.setPadding(0, 0, 0, 0)
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    //configura o recyclerview
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter()
        top_news_rv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@HomeFragment.scrollListener)
        }
    }

}