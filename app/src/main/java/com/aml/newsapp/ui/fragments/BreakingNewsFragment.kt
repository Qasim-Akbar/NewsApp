package com.aml.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aml.newsapp.R
import com.aml.newsapp.adapters.NewsAdapter
import com.aml.newsapp.databinding.FragmentBreakingNewsBinding
import com.aml.newsapp.ui.NewsActivity
import com.aml.newsapp.ui.NewsViewModel
import com.aml.newsapp.util.Constants.Companion.QUERY_PAGE_SIZE
import com.aml.newsapp.util.Resource


class BreakingNewsFragment : Fragment() {

    private val TAG = "BreakingNewsFragment"
    private  var _binding: FragmentBreakingNewsBinding?=null
    lateinit var viewModel: NewsViewModel
    private var newsAdapter: NewsAdapter?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupResyclerView()
        Log.e(TAG, "onViewCreated")

        newsAdapter?.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }

            (activity as NewsActivity).navController.navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    Log.e(TAG, "Success")

                    hideProgressBar()
                    response.data?.let{ newsResponse ->
                        newsAdapter?.differ?.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults/ QUERY_PAGE_SIZE+2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                        if(isLastPage){
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error ->{
                    hideProgressBar()
                    response.message?.let{ message->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                        Log.e(TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading -> {
                    Log.e(TAG, "Loading")
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadinfAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition+visibleItemCount >=totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition>=0
            val isTotalMoreThanVisible  = totalItemCount>=QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadinfAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                viewModel.getBreakingNews("us")
                isScrolling = false
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        newsAdapter=null
    }

    private fun setupResyclerView(){
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
    }
}