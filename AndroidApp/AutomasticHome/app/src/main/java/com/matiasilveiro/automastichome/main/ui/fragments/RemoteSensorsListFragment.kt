package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentRemoteSensorsListBinding
import com.matiasilveiro.automastichome.main.domain.RemoteSensor
import com.matiasilveiro.automastichome.main.ui.adapters.RemoteSensorsAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteSensorsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.RemoteNodesListViewModel
import com.matiasilveiro.automastichome.main.ui.viewmodels.RemoteSensorsListViewModel
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState

class RemoteSensorsListFragment : Fragment() {

    companion object {
        fun newInstance() = RemoteSensorsListFragment()
    }

    private val viewModel: RemoteSensorsListViewModel by activityViewModels()
    private val nodesViewModel: RemoteNodesListViewModel by activityViewModels()
    private var _binding: FragmentRemoteSensorsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentRemoteSensorsListBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshNodes()
        }

        viewModel.setCentralNode(nodesViewModel.centralUid)
        viewModel.refreshNodes()

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        viewModel.nodes.observe(viewLifecycleOwner, Observer { setupRecyclerView(it) })
    }

    private fun handleNavigation(navigation: RemoteSensorsListNavigatorStates) {
        when(navigation) {
            is RemoteSensorsListNavigatorStates.GoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

    private fun handleViewStates(state: DataViewState) {
        when(state) {
            is DataViewState.Ready -> { enableUI(true) }
            is DataViewState.Refreshing -> { enableUI(true) }
            is DataViewState.Loading -> { enableUI(false) }
            is DataViewState.Failure -> { showMessage(getString(R.string.msg_error_default)) }
        }.exhaustive
    }

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun enableUI(enable: Boolean) {
        if(enable) {
            binding.grayblur.visibility = View.GONE
            binding.progressLoader.visibility = View.GONE
        } else {
            binding.grayblur.visibility = View.VISIBLE
            binding.progressLoader.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(list: ArrayList<RemoteSensor>) {
        val adapter = RemoteSensorsAdapter()
        adapter.setData(list)
        adapter.onClickListener = { onNodeClicked(it) }

        with(binding.recyclerView) {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        binding.swipeRefresh.isRefreshing = false
    }

    private fun onNodeClicked(node: RemoteSensor) {
        Log.d(RemoteActuatorsListFragment.TAG, "Node clicked: ${node.name}")
    }

}