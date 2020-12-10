package com.matiasilveiro.automastichome.main.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentControlsListBinding
import com.matiasilveiro.automastichome.databinding.FragmentNewControlBinding
import com.matiasilveiro.automastichome.main.domain.CentralNode
import com.matiasilveiro.automastichome.main.domain.ControlFeedback
import com.matiasilveiro.automastichome.main.ui.adapters.ControlsAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.ControlsListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.navigatorstates.NewControlNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.ControlsListViewModel
import com.matiasilveiro.automastichome.main.ui.viewmodels.NewControlViewModel
import com.matiasilveiro.automastichome.main.ui.viewstates.DataViewState
import kotlinx.android.synthetic.main.fragment_new_control.*

class NewControlFragment : Fragment() {

    companion object {
        fun newInstance() = NewControlFragment()
    }

    private val viewModel: NewControlViewModel by activityViewModels()
    private var _binding: FragmentNewControlBinding? = null
    private val binding get() = _binding!!

    private val args: NewControlFragmentArgs by navArgs()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentNewControlBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        viewModel.centralUid = args.centralUid
        viewModel.loadNodes()

        binding.edtCondition.editText?.setOnClickListener{ showConditionDialog() }
        binding.edtActuator.editText?.setOnClickListener{ showActuatorPickDialog() }
        binding.edtSensor.editText?.setOnClickListener{ showSensorPickDialog() }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_done_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_done -> {
                val sensor = binding.edtSensor.editText!!.text.toString()
                val actuator = binding.edtActuator.editText!!.text.toString()
                val control = ControlFeedback(
                        "",
                        binding.edtName.editText!!.text.toString(),
                        binding.edtReference.editText!!.text.toString().toInt(),
                        1,
                        0,
                        binding.edtCondition.editText!!.text.toString(),)
                viewModel.createControl(control, sensor, actuator)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
    }

    private fun handleNavigation(navigation: NewControlNavigatorStates) {
        when(navigation) {
            is NewControlNavigatorStates.GoBack -> {
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
            binding.grayblur.visibility = View.GONE
            binding.progressLoader.visibility = View.GONE
        } else {
            binding.grayblur.visibility = View.VISIBLE
            binding.progressLoader.visibility = View.VISIBLE
        }
    }


    private fun showActuatorPickDialog() {
        val items = arrayListOf<String>()
        viewModel.actuators.value!!.forEach {
            items.add(it.name)
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccione el nodo")
            .setItems(items.toTypedArray()) { dialog, which ->
                binding.edtActuator.editText?.setText(items[which])
            }
            .show()
    }

    private fun showSensorPickDialog() {
        val items = arrayListOf<String>()
        viewModel.sensors.value!!.forEach {
            items.add(it.name)
        }
        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Seleccione el nodo")
                .setItems(items.toTypedArray()) { dialog, which ->
                    binding.edtSensor.editText?.setText(items[which])
                }
                .show()
    }

    private fun showConditionDialog() {
        val items = arrayListOf<String>()
        items.add(">")
        items.add("<")
        items.add("=")
        items.add(">=")
        items.add("<=")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Seleccione la condicion")
            .setItems(items.toTypedArray()) { dialog, which ->
                binding.edtCondition.editText?.setText(items[which])
            }
            .show()
}
}