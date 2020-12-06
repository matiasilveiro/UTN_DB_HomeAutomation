package com.matiasilveiro.automastichome.login.ui.fragments

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.matiasilveiro.automastichome.core.ui.BaseAlertViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentSignUpBinding
import com.matiasilveiro.automastichome.login.ui.navigatorstates.SignUpNavigatorStates
import com.matiasilveiro.automastichome.login.ui.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.prefs.BackingStoreException

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

        binding.btnCreate.setOnClickListener {
            createUserCallback()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewState(it) })
    }

    private fun handleNavigation(navigation: SignUpNavigatorStates) {
        when(navigation) {
            SignUpNavigatorStates.GoBack -> {
                findNavController().navigateUp()
            }
        }.exhaustive
    }

    private fun handleViewState(state: BaseAlertViewState) {
        when(state) {
            is BaseAlertViewState.Ready -> { enableUI(true) }
            is BaseAlertViewState.Loading -> { enableUI(false) }
            is BaseAlertViewState.Alert -> {
                enableUI(true)
                showMessage(state.message)
            }
            is BaseAlertViewState.Failure -> {
                enableUI(true)
                showMessage(state.exception.message!!)
            }
        }
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

    private fun showMessage(msg: String) {
        binding.root.snack(msg, Snackbar.LENGTH_SHORT)
    }

    private fun createUserCallback() {
        val username = binding.edtUsername.editText?.text.toString()
        val password = binding.edtPassword.editText?.text.toString()
        val passwordCheck = binding.edtPasswordCheck.editText?.text.toString()

        if(username.isNotBlank() and password.isNotBlank() and passwordCheck.isNotBlank()) {
            Log.d(ContentValues.TAG, "SignUpFragment: User: $username, Pass: $password")
            if(password == passwordCheck) {
                viewModel.createUser(username, password)
            } else {
                showMessage("Las contraseñas no coinciden")
            }
        } else {
            binding.edtUsername.error = when(username.isBlank()) {
                true -> "Ingrese su usuario"
                false -> null
            }
            binding.edtPassword.error = when(password.isBlank()) {
                true -> "Ingrese su contraseña"
                false -> null
            }
            binding.edtPassword.error = when(passwordCheck.isBlank()) {
                true -> "Ingrese su contraseña"
                false -> null
            }
        }
    }

}