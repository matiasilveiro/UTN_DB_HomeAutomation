package com.matiasilveiro.automastichome.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.manadigital.tecontactolocal.CoreFeature.usecases.GetCurrentUserUseCase
import com.matiasilveiro.automastichome.R
import com.matiasilveiro.automastichome.core.utils.MyResult
import com.matiasilveiro.automastichome.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var getCurrentUserUseCase: GetCurrentUserUseCase

    private val topLevelDestinations = setOf(
        R.id.centralNodesListFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun setupNavigation() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(binding.drawerLayout)
            .build()

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navigationView, navController)
        binding.navigationView.setNavigationItemSelectedListener(this)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id) {
                /*
                R.id.categoriesFragment -> {
                    binding.searchView.visibility = View.VISIBLE
                    binding.searchView.queryHint = destination.label
                }
                 */
                else -> {
                }
            }
        }

        updateDrawerHeader()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true

        binding.drawerLayout.closeDrawers()

        try {
            when(item.itemId) {
                else -> { navController.navigate(item.itemId) }
            }
        } catch (e: Exception) {
            Log.w("MainActivity", "Navigation Drawer exception $e")
        }

        return true
    }

    private fun updateDrawerHeader() {
        lifecycleScope.launch {
            when(val result = getCurrentUserUseCase()) {
                is MyResult.Success -> {
                    result.data?.let {
                        val header = binding.navigationView.getHeaderView(0)
                        header.findViewById<TextView>(R.id.username).text = it.name
                        header.findViewById<TextView>(R.id.email).text = it.email
                        Glide.with(header)
                            .load(it.profileImage)
                            .centerCrop()
                            .into(header.findViewById(R.id.circle_image))
                    }
                }
                is MyResult.Failure -> {
                    // TODO: mostrar algo
                }
            }
        }
    }
}