package com.matiasilveiro.automastichome.login.ui.fragments

import android.content.ContentValues
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentSignInBinding
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignInNavigatorStates
import com.matiasilveiro.automastichome.login.ui.viewmodels.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.edtUsername.editText?.setText("matias.silveiro@gmail.com")
        binding.edtPassword.editText?.setText("proyectodb2020")

        binding.btnSignIn.setOnClickListener {
            signInCallback()
        }

        binding.btnSignUp.setOnClickListener {
            signUpCallback()
        }

        binding.txtPswdForgotten.setOnClickListener {
            Log.d(ContentValues.TAG, "SignInFragment: Password forgotten")
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewState(it)})
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
    }

    private fun handleNavigation(navigation: SignInNavigatorStates) {
        when(navigation) {
            SignInNavigatorStates.ToSignUp -> {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }
            SignInNavigatorStates.ToMainApplication -> {
                val action = SignInFragmentDirections.actionSignInFragmentToMainActivity()
                findNavController().navigate(action)
            }
        }.exhaustive
    }

    private fun handleViewState(state: BaseViewState) {
        when(state) {
            BaseViewState.Ready -> enableUI(true)
            BaseViewState.Loading -> enableUI(false)
            is BaseViewState.Failure -> {
                enableUI(true)
                handleExceptions(state.exception)
            }
        }.exhaustive
    }

    private fun handleExceptions(e: Exception) {
        Log.w("SignInFragment", "Exception thrown: ${e.message}")
        binding.root.snack("Error de credenciales")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signInCallback() {
        val username = binding.edtUsername.editText?.text.toString()
        val password = binding.edtPassword.editText?.text.toString()

        if(username.isNotBlank() and password.isNotBlank()) {
            Log.d(ContentValues.TAG, "SignInFragment: User: $username, Pass: $password")
            viewModel.doLogin(username, password)
        } else {
            binding.edtUsername.error = when(username.isBlank()) {
                true -> "Ingrese su usuario"
                false -> null
            }
            binding.edtPassword.error = when(password.isBlank()) {
                true -> "Ingrese su contraseña"
                false -> null
            }
        }
    }

    private fun signUpCallback() {
        viewModel.goToSignUp()
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

}