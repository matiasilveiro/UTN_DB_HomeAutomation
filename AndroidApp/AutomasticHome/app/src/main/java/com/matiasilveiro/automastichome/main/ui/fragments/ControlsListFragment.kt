package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentControlsListBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.ui.adapters.CentralNodesAdapter
import com.matiasilveiro.automastichome.main.ui.adapters.ControlsAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.ControlsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.ControlsListViewModel
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState

class ControlsListFragment : Fragment() {

    companion object {
        fun newInstance() = ControlsListFragment()
    }

    private val viewModel: ControlsListViewModel by activityViewModels()
    private var _binding: FragmentControlsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectedNode: CentralNode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentControlsListBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshControls()
        }

        binding.floatingActionButton.setOnClickListener {
            if(this::selectedNode.isInitialized) {
                viewModel.goToNewControl()
            } else {
                showDialog("Error de capa 8","Por favor, elegir un nodo central para asociar el nuevo control automatico")
            }
        }

        binding.btnPick.setOnClickListener {
            showSelectDialog()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        viewModel.controls.observe(viewLifecycleOwner, Observer { setupRecyclerView(it) })
    }

    private fun handleNavigation(navigation: ControlsListNavigatorStates) {
        when(navigation) {
            is ControlsListNavigatorStates.ToNewControl -> {
                val action = ControlsListFragmentDirections.actionControlsListFragmentToNewControlFragment(null,navigation.centralUid)
                findNavController().navigate(action)
            }
            is ControlsListNavigatorStates.ToEditControl -> {
                val action = ControlsListFragmentDirections.actionControlsListFragmentToNewControlFragment(navigation.control,navigation.centralUid)
                findNavController().navigate(action)
            }
            is ControlsListNavigatorStates.GoBack -> {
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

    private fun setupRecyclerView(list: ArrayList<ControlFeedback>) {
        val adapter = ControlsAdapter()
        adapter.setData(list)
        adapter.setRole(selectedNode.role)
        adapter.onClickListener = {
            showMessage("Nada por aquí")
        }
        adapter.onEditListener = {
            viewModel.goToEditControl(it)
        }

        with(binding.recyclerView) {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
        binding.swipeRefresh.isRefreshing = false
    }

    private fun showSelectDialog() {
        val items = arrayListOf<String>()
        viewModel.nodes.value!!.forEach {
            items.add(it.name)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccione el nodo")
            .setItems(items.toTypedArray()) { dialog, which ->
                binding.txtNodeName.text = items[which]
                selectedNode = viewModel.nodes.value!!.find { it.name == items[which] }!!
                viewModel.setCentralNode(selectedNode)
            }
            .show()
    }

    private fun showDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Aceptar") { _, _ -> }
            .show()
    }

}