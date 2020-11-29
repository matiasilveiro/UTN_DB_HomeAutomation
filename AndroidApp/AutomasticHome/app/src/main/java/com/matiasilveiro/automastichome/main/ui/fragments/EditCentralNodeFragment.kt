package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentEditCentralNodeBinding
import com.matiasilveiro.automastichome.main.ui.navigatorstates.EditCentralNodeNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.EditCentralNodeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCentralNodeFragment : Fragment() {

    companion object {
        fun newInstance() = EditCentralNodeFragment()
    }

    private val viewModel: EditCentralNodeViewModel by activityViewModels()
    private var _binding: FragmentEditCentralNodeBinding? = null
    private val binding get() = _binding!!

    private val args: EditCentralNodeFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentEditCentralNodeBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        args.centralNode?.let {
            binding.textView.text = it.name
            binding.edtName.editText?.setText(it.name)
            binding.edtAdress.editText?.setText(it.address)
            binding.edtPassword.editText?.setText(it.password)
            Glide.with(binding.root)
                .load(it.imageUrl)
                .centerCrop()
                .into(binding.imageView)
        }

        binding.edtName.editText?.addTextChangedListener {
            binding.textView.text = binding.edtName.editText!!.text.toString()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_done_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_done -> {
                val node = args.centralNode!!
                node.name = binding.edtName.editText!!.text.toString()
                node.address = binding.edtAdress.editText!!.text.toString()
                node.password = binding.edtPassword.editText!!.text.toString()
                viewModel.saveChanges(node)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
    }

    private fun handleNavigation(navigation: EditCentralNodeNavigatorStates) {
        when(navigation) {
            is EditCentralNodeNavigatorStates.GoBack -> {
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