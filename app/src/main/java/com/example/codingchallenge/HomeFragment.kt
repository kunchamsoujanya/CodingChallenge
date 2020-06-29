package com.example.codingchallenge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.network.ApiClient
import com.example.codingchallenge.repository.HomeRepository
import com.example.codingchallenge.response.Cards
import com.example.codingchallenge.response.EventObserver

class HomeFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var progressBar:ProgressBar? = null
    private var errorLayout:LinearLayout? = null
    private var errorView:TextView? = null
    private var retry:Button? = null

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(HomeRepository(ApiClient.apiServices))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.cards_list)
        progressBar = view.findViewById(R.id.progress_bar)
        errorLayout = view.findViewById(R.id.error_layout)
        errorView = view.findViewById(R.id.error_msg)
        retry = view.findViewById(R.id.retry)
        retry?.setOnClickListener {
           loadHomePageData()
        }
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        loadHomePageData()
        init()

        return view;
    }

    private fun init() {
        val itemsSize = viewModel.getHomePageCards().value?.size ?: 0
        if (itemsSize > 0) hideLoading()
        viewModel.getHomePageCards().observe(viewLifecycleOwner, Observer { response ->
            if (response.isNotEmpty()) hideLoading()
            setAdapter(response.toMutableList())
        })

        viewModel.getEvents().observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is HomeViewModel.ViewEvent.FinishedLoading -> {
                    hideLoading()
                }
                is HomeViewModel.ViewEvent.ShowError -> {
                    showErrorView(it.errorMsg)
                }
            }
        })
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
        errorLayout?.visibility = View.GONE
        recyclerView?.visibility = View.VISIBLE
    }


    private fun showErrorView(message: String) {
        progressBar?.visibility = View.GONE
        errorLayout?.visibility = View.VISIBLE
        recyclerView?.visibility = View.GONE
        errorView?.text = message
    }

    private fun setAdapter(cards: List<Cards>) {
        val adapter = CardsAdapter(cards)
        recyclerView?.adapter = adapter
    }

    private fun loadHomePageData() {
        if (isOnline(requireActivity())) {
            viewModel.loadHomePageCards()
        } else {
            showErrorView("Wifi and mobile data are unavailable. \n Check your connection and try again")
        }
    }
}
