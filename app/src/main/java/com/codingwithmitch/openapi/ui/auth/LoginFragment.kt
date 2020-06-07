package com.codingwithmitch.openapi.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.codingwithmitch.openapi.R
import com.codingwithmitch.openapi.ui.auth.state.LoginFields
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState.loginFields?.let { loginFields ->
                Log.d(TAG, "ViesState Not null")
                loginFields.login_email?.let { input_email.setText(it) }
                loginFields.login_password?.let { input_password.setText(it) }
            }
            Log.d(TAG, "ViewState: $viewState")
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "OnDestroyView")
        viewModel.setLoginFields(
            LoginFields(
                input_email.text.toString(),
                input_password.text.toString()
            )
        )
    }
}