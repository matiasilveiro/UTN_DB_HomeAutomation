package com.matiasilveiro.automastichome.main.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.ui.BaseViewState
import com.matiasilveiro.automastichome.core.utils.exhaustive
import com.matiasilveiro.automastichome.core.utils.snack
import com.matiasilveiro.automastichome.databinding.FragmentRemoteNodesListBinding
import com.matiasilveiro.automastichome.main.domain.RemoteNode
import com.matiasilveiro.automastichome.main.ui.adapters.RemoteActuatorsAdapter
import com.matiasilveiro.automastichome.main.ui.navigatorstates.RemoteNodesListNavigatorStates
import com.matiasilveiro.automastichome.main.ui.viewmodels.RemoteNodesListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoteNodesListFragment : Fragment() {

    companion object {
        fun newInstance() = RemoteNodesListFragment()
    }

    private val viewModel: RemoteNodesListViewModel by activityViewModels()
    private var _binding: FragmentRemoteNodesListBinding? = null
    private val binding get() = _binding!!

    private val args: RemoteNodesListFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentRemoteNodesListBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        viewModel.setCentralNode(args.centralNode.uid, args.centralNode.role)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        binding.viewPager.adapter = createCardAdapter()
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Actuadores"
                1 -> tab.text = "Sensores"
                else -> tab.text = "undefined"
            }
        }.attach()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        viewModel.navigation.observe(viewLifecycleOwner, Observer { handleNavigation(it) })
        viewModel.viewState.observe(viewLifecycleOwner, Observer { handleViewStates(it) })
        //viewModel.nodes.observe(viewLifecycleOwner, Observer { setupRecyclerView(it) })
    }

    private fun handleNavigation(navigation: RemoteNodesListNavigatorStates) {
        when(navigation) {
            is RemoteNodesListNavigatorStates.GoBack -> {
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

    /*
    private fun setupRecyclerView(list: ArrayList<RemoteNode>) {
        val adapter = RemoteActuatorsAdapter()
        adapter.setData(list)

        with(binding.recyclerView) {
            this.setHasFixedSize(true)
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }
     */

    private fun createCardAdapter(): ViewPagerAdapter? {
        return ViewPagerAdapter(requireActivity())
    }

    class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
        override fun createFragment(position: Int): Fragment {

            return when(position){
                0 -> RemoteActuatorsListFragment()
                1 -> RemoteSensorsListFragment()

                else -> RemoteActuatorsListFragment()
            }
        }

        override fun getItemCount(): Int {
            return TAB_COUNT
        }

        companion object {
            private const val TAB_COUNT = 2
        }
    }
}