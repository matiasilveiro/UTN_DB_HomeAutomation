package com.matiasilveiro.automastichome.main.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.main.ui.viewmodels.NewControlViewModel

class NewControlFragment : Fragment() {

    companion object {
        fun newInstance() = NewControlFragment()
    }

    private lateinit var viewModel: NewControlViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_control, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewControlViewModel::class.java)
        // TODO: Use the ViewModel
    }

}