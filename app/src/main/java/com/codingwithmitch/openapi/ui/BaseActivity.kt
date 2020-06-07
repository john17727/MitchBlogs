package com.codingwithmitch.openapi.ui

import com.codingwithmitch.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TAG = "BaseActivity"
    }

    @Inject
    lateinit var sessionManager: SessionManager

}