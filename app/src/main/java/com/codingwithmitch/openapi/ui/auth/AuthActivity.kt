package com.codingwithmitch.openapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.BaseActivity
import com.codingwithmitch.openapi.ui.ResponseType
import com.codingwithmitch.openapi.ui.ResponseType.*
import com.codingwithmitch.openapi.ui.main.MainActivity
import com.codingwithmitch.openapi.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener {

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            dataState.data?.let { data ->

                data.data?.let { event ->

                    event.getContentIfNotHandled()?.let { viewState ->
                        viewState.authToken?.let {
                            viewModel.setAuthToken(it)
                        }
                    }
                }

                data.response?.let { event ->

                    event.getContentIfNotHandled()?.let { response ->
                        when (response.responseType) {
                            is Dialog -> {
                                // inflate error dialog
                            }

                            is Toast -> {
                                // show toast message
                            }

                            is None -> {
                                Log.e(TAG, "subscribeObservers: DataState Response: ${response.message}")
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(this, Observer { viewState ->
            viewState.authToken?.let {
                sessionManager.login(it)
            }
        })

        sessionManager.cachedToken.observe(this, Observer { authToken ->
            if (authToken != null && authToken.account_pk != -1 && authToken.token != null) {
                navMainActivity()
            }
        })
    }

    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJobs()
    }
}