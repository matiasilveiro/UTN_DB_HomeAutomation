package com.matiasilveiro.automastichome.login.ui.fragments

import android.content.pm.ActivityInfo
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.databinding.FragmentSplashBinding
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SplashNavigatorStates
import com.matiasilveiro.automastichome.login.ui.viewmodels.SplashViewModel

class SplashFragment : Fragment() {

    companion object {
        fun newInstance() = SplashFragment()
    }

    private val SPLASH_SCREEN_DELAY: Long = 1000

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        viewModel.goToSignIn(SPLASH_SCREEN_DELAY)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
    }

    private fun handleNavigation(navigation: SplashNavigatorStates) {
        when(navigation) {
            SplashNavigatorStates.ToSignIn -> {
                val action = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                findNavController().navigate(action)
            }
            SplashNavigatorStates.ToMainApplication -> {
                val action = SplashFragmentDirections.actionSplashFragmentToMainActivity()
                findNavController().navigate(action)
            }
        }.exhaustive
    }

}