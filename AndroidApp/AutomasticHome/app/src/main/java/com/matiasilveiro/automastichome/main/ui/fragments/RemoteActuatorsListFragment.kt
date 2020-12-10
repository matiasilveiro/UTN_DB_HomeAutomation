package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentRemoteActuatorsListBinding
import com.matiasilveiro.automastichome.main.domain.RemoteActuator
import com.matiasilveiro.automastichome.main.ui.adapters.RemoteActuatorsAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteActuatorsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.RemoteActuatorsListViewModel
import com.matiasilveiro.automastichome.main.ui.viewmodels.RemoteNodesListViewModel
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState

class RemoteActuatorsListFragment : Fragment() {

    companion object {
        fun newInstance() = RemoteActuatorsListFragment()
        const val TAG = "RemoteActuatorsListFragment"
    }

    private val viewModel: RemoteActuatorsListViewModel by activityViewModels()
    private val nodesViewModel: RemoteNodesListViewModel by activityViewModels()
    private var _binding: FragmentRemoteActuatorsListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentRemoteActuatorsListBinding.inflate(layoutInflater)
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

    private fun handleNavigation(navigation: RemoteActuatorsListNavigatorStates) {
        when(navigation) {
            is RemoteActuatorsListNavigatorStates.GoBack -> {
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
                binding.swipeRefresh.isRefreshing = false
                showMessage(getString(R.string.msg_error_default)) }
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

    private fun setupRecyclerView(list: ArrayList<RemoteActuator>) {
        val adapter = RemoteActuatorsAdapter()
        adapter.setData(list)

        adapter.onClickListener = { viewModel.onNodeClicked(it) }
        adapter.onSwitchListener = { node, state ->
            if(nodesViewModel.role < 2) {
                viewModel.onNodeSwitchToggled(node, state)
            } else {
                showPermissionErrorDialog()
            }
        }

        with(binding.recyclerView) {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showPermissionErrorDialog() {
        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Permisos insuficientes")
                .setMessage("No tienes permisos de interacción con los nodos remotos")
                .setPositiveButton("Aceptar") { _, _ -> }
                .show()
    }
}