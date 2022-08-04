package com.garibyan.armen.tbc_task_14.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.garibyan.armen.tbc_task_14.R
import com.garibyan.armen.tbc_task_14.databinding.FragmentMainBinding
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
        onClicklisteners()
    }

    private fun onClicklisteners() = with(binding!!) {
        root.setOnRefreshListener {
            viewModel.getNews()
            root.isRefreshing = false
        }
    }

    private fun stateListener() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.modelState.collect {
                when (it) {
                    is MainViewModel.ModelState.Success -> {
                        modelAdapter.submitList(it.newsList.content)
                        successViewState()
                        Log.d("MODEL_STATE", "Success")
                        Log.d("RESPONSE_MODEL", it.newsList.toString())
                    }
                    is MainViewModel.ModelState.Error -> {
                        errorViewState()
                        Log.d("MODEL_STATE", "Error")

                    }
                    is MainViewModel.ModelState.Loading -> {
                        loadingViewState()
                        Log.d("MODEL_STATE", "Loading")
                    }
                }
            }
        }
    }

    private fun successViewState() = with(binding!!) {
        binding!!.newsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = modelAdapter
            visibility = View.VISIBLE
        }
        progressBar.visibility = View.GONE
        tvState.visibility = View.GONE
    }

    private fun loadingViewState() = with(binding!!) {
        newsRecyclerView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        tvState.visibility = View.VISIBLE
        tvState.text = requireContext().getString(R.string.loading)
    }

    private fun errorViewState() = with(binding!!) {
        newsRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        tvState.visibility = View.VISIBLE
        tvState.text = requireContext().getString(R.string.error)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}