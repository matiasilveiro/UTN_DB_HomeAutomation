package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentNodeSensorBinding
import com.matiasilveiro.automastichome.main.ui.navigatorstates.NodeSensorNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.NodeSensorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NodeSensorFragment : Fragment() {

    companion object {
        fun newInstance() = NodeSensorFragment()
    }

    private val viewModel: NodeSensorViewModel by activityViewModels()
    private var _binding: FragmentNodeSensorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNodeSensorBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
    }

    private fun handleNavigation(navigation: NodeSensorNavigatorStates) {
        when(navigation) {
            is NodeSensorNavigatorStates.GoBack -> {
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
            //binding.grayblur.visibility = View.GONE
            //binding.progressLoader.visibility = View.GONE
        } else {
            //binding.grayblur.visibility = View.VISIBLE
            //binding.progressLoader.visibility = View.VISIBLE
        }
    }

}