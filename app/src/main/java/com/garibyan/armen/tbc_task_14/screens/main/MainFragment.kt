package com.garibyan.armen.tbc_task_14.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.garibyan.armen.tbc_task_14.R
import com.garibyan.armen.tbc_task_14.databinding.FragmentMainBinding
import com.garibyan.armen.tbc_task_14.extentions.collectLatestLifecycleFlow
import com.garibyan.armen.tbc_task_14.network.News
import com.garibyan.armen.tbc_task_14.screens.ScreenState
import com.garibyan.armen.tbc_task_14.viewModels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var binding: FragmentMainBinding? = null
    private val modelAdapter: NewsAdapter by lazy { NewsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateListener()
        onClickListeners()
    }

    private fun onClickListeners() = with(binding!!) {
        root.setOnRefreshListener {
            viewModel.getNews()
            root.isRefreshing = false
        }
    }

    private fun stateListener(){
        collectLatestLifecycleFlow(viewModel.newsState){
            when(it){
                is ScreenState.Success ->{
                    successViewState(it.data)
                    Log.d("MODEL_STATE", "Success")
                    Log.d("RESPONSE_MODEL", it.data.toString())
                }
                is ScreenState.Loading ->{
                    loadingViewState()
                    Log.d("MODEL_STATE", "Loading")
                }
                is ScreenState.Error -> {
                    errorViewState()
                    Log.d("MODEL_STATE", "Error")
                }
            }
        }
    }

    private fun successViewState(list: List<News>) = with(binding!!) {
        binding!!.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = modelAdapter
            visibility = View.VISIBLE
            modelAdapter.submitList(list)
        }
        View.GONE.also {
            tvState.visibility = it
            progressBar.visibility = it
        }
    }

    private fun loadingViewState() = with(binding!!) {
        newsRecyclerView.visibility = View.GONE
        tvState.text = requireContext().getString(R.string.loading)
        View.VISIBLE.also {
            progressBar.visibility = it
            tvState.visibility = it
        }
    }

    private fun errorViewState() = with(binding!!) {
        View.GONE.also {
            newsRecyclerView.visibility = it
            progressBar.visibility = it
        }
        tvState.visibility = View.VISIBLE
        tvState.text = requireContext().getString(R.string.error)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

