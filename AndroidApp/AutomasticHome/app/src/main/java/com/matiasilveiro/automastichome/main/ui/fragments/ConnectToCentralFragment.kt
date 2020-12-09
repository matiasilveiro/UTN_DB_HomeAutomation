package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentConnectToCentralBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.ui.navigatorstates.ConnectToCentralNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.ConnectToCentralViewModel

class ConnectToCentralFragment : Fragment() {

    companion object {
        fun newInstance() = ConnectToCentralFragment()
    }

    private val viewModel: ConnectToCentralViewModel by activityViewModels()
    private var _binding: FragmentConnectToCentralBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentConnectToCentralBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        binding.btnSearch.setOnClickListener {
            val address = binding.edtAdress.editText?.text.toString()
            viewModel.searchNode(address)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_done_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_done -> {
                onDoneClicked()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        viewModel.node.observe(viewLifecycleOwner, Observer { updateNodeInformation(it) })
    }

    private fun handleNavigation(navigation: ConnectToCentralNavigatorStates) {
        when(navigation) {
            is ConnectToCentralNavigatorStates.GoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

    private fun handleViewStates(state: BaseViewState) {
        when(state) {
            is BaseViewState.Ready -> { enableUI(true) }
            is BaseViewState.Loading -> { enableUI(false) }
            is BaseViewState.Failure -> { showMessage(getString(R.string.msg_error_default)) }
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

    private fun updateNodeInformation(node: CentralNode) {
        binding.textView.text = node.name

        Glide.with(binding.root)
            .load(node.imageUrl)
            .centerCrop()
            .into(binding.imageView)
    }

    private fun onDoneClicked() {
        if(viewModel.node.value?.uid?.isNotEmpty() == true) {
            viewModel.createRole(1)
        } else {
            showMessage("Por favor, buscar el nodo antes de agregarlo")
        }
    }

}