package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentCentralNodesListBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.ui.adapters.CentralNodesAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.CentralNodesListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.CentralNodesListViewModel
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CentralNodesListFragment : Fragment() {

    companion object {
        fun newInstance() = CentralNodesListFragment()
    }

    private val viewModel: CentralNodesListViewModel by activityViewModels()
    private var _binding: FragmentCentralNodesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentCentralNodesListBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        viewModel.nodes.observe(viewLifecycleOwner, Observer { setupRecyclerView(it) })
    }

    private fun handleNavigation(navigation: CentralNodesListNavigatorStates) {
        when(navigation) {
            is CentralNodesListNavigatorStates.ToRemoteNodesList -> {
                val action = CentralNodesListFragmentDirections.actionCentralNodesListFragmentToRemoteNodesListFragment(navigation.centralNode)
                findNavController().navigate(action)
            }
            is CentralNodesListNavigatorStates.ToEditCentralNode -> {
                val action = CentralNodesListFragmentDirections.actionCentralNodesListFragmentToEditCentralNodeFragment(navigation.centralNode)
                findNavController().navigate(action)
            }
            is CentralNodesListNavigatorStates.GoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

    private fun handleViewStates(state: DataViewState) {
        when(state) {
            is DataViewState.Ready -> { enableUI(true) }
            is DataViewState.Refreshing -> { enableUI(true) }
            is DataViewState.Loading -> { enableUI(false) }
            is DataViewState.Failure -> {
                enableUI(true)
                showMessage(getString(R.string.msg_error_default)) }
        }.exhaustive
    }

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun enableUI(enable: Boolean) {
        if(enable) {
            //binding.grayblur.visibility = View.GONE
            //binding.progressLoader.visibility = View.GONE
        } else {
            //binding.grayblur.visibility = View.VISIBLE
            //binding.progressLoader.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerView(list: ArrayList<CentralNode>) {
        val adapter = CentralNodesAdapter()
        adapter.setData(list)
        adapter.onClickListener = {
            viewModel.goToRemoteNodesList(it)
        }
        adapter.onEditListener = {
            viewModel.goToEditCentralNode(it)
        }

        with(binding.recyclerView) {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }
}