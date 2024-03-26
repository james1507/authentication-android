package com.example.authentication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.authentication.R
import com.example.authentication.data.RegisterBody
import com.example.authentication.databinding.ActivityRegisterBinding
import com.example.authentication.repository.AuthRepository
import com.example.authentication.utils.APIService
import com.example.authentication.viewmodel.RegisterActivityViewModel
import com.example.authentication.viewmodel.RegisterActivityViewModelFactory

class RegisterActivity : AppCompatActivity(), View.OnFocusChangeListener, View.OnKeyListener,
    View.OnClickListener {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.fullName.onFocusChangeListener = this
        mBinding.email.onFocusChangeListener = this
        mBinding.password.onFocusChangeListener = this
        mBinding.password.setOnKeyListener(this)
        mBinding.registerButton.setOnClickListener(this)
        mViewModel = ViewModelProvider(
            this,
            RegisterActivityViewModelFactory(AuthRepository(APIService.getService()), application)
        ).get(RegisterActivityViewModel::class.java)
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel.getIsLoading().observe(this) {
            mBinding.progressBar.isVisible = it
        }

        mViewModel.getErrorMessage().observe(this) {
            val formErrorKey = arrayOf("fullName", "email", " password")
            val message = StringBuilder()
            it.map { entry ->
                if (formErrorKey.contains(entry.key)) {
                    when (entry.key) {
                        "fullName" -> {
                            mBinding.fullNameTile.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "email" -> {
                            mBinding.emailTitle.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }

                        "password" -> {
                            mBinding.passwordTitle.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                } else {
                    message.append(entry.value).append("\n")
                }

                if (message.isNotEmpty()) {
                    AlertDialog.Builder(this).setIcon(R.drawable.info_24).setTitle("INFORMATION")
                        .setMessage(message).setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }.show()
                }
            }
        }

        mViewModel.getUser().observe(this) {
            if (it != null) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }
    }

    private fun validateFullName(): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.fullName.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Full name is required"
        }

        if (errorMessage != null) {
            mBinding.fullNameTile.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.email.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMessage = "Email address is invalid"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.emailTitle.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    private fun validatePassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMessage: String? = null
        val value: String = mBinding.password.text.toString()
        if (value.isEmpty()) {
            errorMessage = "Password is required"
        } else if (value.length < 6) {
            errorMessage = "Password must be 6 characters long"
        }

        if (errorMessage != null && shouldUpdateView) {
            mBinding.passwordTitle.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }

        return errorMessage == null
    }

    override fun onClick(view: View?) {
        if (view != null && view.id == R.id.registerButton) {
            onSubmit()
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.fullName -> {
                    if (hasFocus) {
                        if (mBinding.fullNameTile.isCounterEnabled) {
                            mBinding.fullNameTile.isErrorEnabled = false
                        }
                    } else {
                        validateFullName()
                    }
                }

                R.id.email -> {
                    if (hasFocus) {
                        if (mBinding.emailTitle.isCounterEnabled) {
                            mBinding.emailTitle.isErrorEnabled = false
                        }
                    } else {
                        validateEmail()
                    }
                }

                R.id.password -> {
                    if (hasFocus) {
                        if (mBinding.passwordTitle.isCounterEnabled) {
                            mBinding.passwordTitle.isErrorEnabled = false
                        }
                    } else {
                        validatePassword()
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        if (validatePassword(shouldUpdateView = false)) {
            mBinding.passwordTitle.apply {
                if (isErrorEnabled) isErrorEnabled = false
            }
        } else {
            if (mBinding.passwordTitle.startIconDrawable != null)
                mBinding.passwordTitle.startIconDrawable = null
        }

        return false
    }

    private fun onSubmit() {
        if (validate()) {
            mViewModel.registerUser(
                RegisterBody(
                    mBinding.fullName.text!!.toString(),
                    mBinding.email.text!!.toString(),
                    mBinding.password.text!!.toString()
                )
            )
        }
    }

    private fun validate(): Boolean {
        var isValid = true

        if (!validateFullName()) isValid = false
        if (!validateEmail()) isValid = false
        if (!validatePassword()) isValid = false

        return isValid
    }
}