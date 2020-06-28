package com.example.codingchallenge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codingchallenge.network.ApiClient
import com.example.codingchallenge.response.Cards
import com.example.codingchallenge.response.PageData
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var progressBar:ProgressBar? = null
    private var errorLayout:LinearLayout? = null
    private var errorView:TextView? = null
    private var retry:Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.cards_list)
        progressBar = view.findViewById(R.id.progress_bar)
        errorLayout = view.findViewById(R.id.error_layout)
        errorView = view.findViewById(R.id.error_msg)
        retry = view.findViewById(R.id.retry)
        retry?.setOnClickListener {
            loadData()
        }
        recyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        loadData()
        return view;
    }

    private fun loadData() {
        if (!isOnline(requireActivity())) {
            showErrorView("Wifi and mobile data are unavailable. \n Check your connection and try again")
            return
        }
        progressBar?.visibility = View.VISIBLE
        errorLayout?.visibility = View.GONE

        val responseCall = ApiClient.apiServices.fetchHomePageResponse()
        responseCall.enqueue(object : Callback<PageData?> {
            override fun onResponse(call: Call<PageData?>, response: Response<PageData?>) {
                response.body()?.let { pageData ->
                    progressBar?.visibility = View.GONE
                    errorLayout?.visibility = View.GONE
                    recyclerView?.visibility = View.VISIBLE
                    setAdapter(pageData.page.cards)
                }
            }
            override fun onFailure(call: Call<PageData?>, t: Throwable) {
                t.message?.let {
                    showErrorView(it)
                }
                Log.e("onFailure", "onFailure")
            }
        })
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
}
