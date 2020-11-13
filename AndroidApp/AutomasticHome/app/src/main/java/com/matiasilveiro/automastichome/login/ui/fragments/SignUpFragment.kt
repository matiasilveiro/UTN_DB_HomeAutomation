package com.matiasilveiro.automastichome.login.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.databinding.FragmentSignUpBinding
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignUpNavigatorStates
import com.matiasilveiro.automastichome.login.ui.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
    }

    private fun handleNavigation(navigation: SignUpNavigatorStates) {
        when(navigation) {
            SignUpNavigatorStates.GoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

}