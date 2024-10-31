package com.dsg.bankjoyassessment

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dsg.bankjoyassessment.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            validateLogin()
        }


        //Listener to the soft keyboard's done button
        binding.passwordInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                validateLogin()
                true
            } else {
                false // Let the system handle the action
            }
        }

        // Observe validation state from ViewModel
        viewModel.validationState.observe(viewLifecycleOwner) { state ->
            println(state)
            when (state) {
                is ValidationState.Valid -> {
                    //If Remember me is checked,
                    // save it accordingly in
                    // Keystore/EncryptedSharedPreferences

                    binding.apply {
                        usernameInput.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )
                        passwordInput.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.black
                            )
                        )

                        //Set error message
                        tvErrorMessage.animate().alpha(0f).setDuration(200).start()
                    }
                    hideKeyboard()
                    Snackbar.make(binding.root, "Login successful!", Snackbar.LENGTH_LONG)
                        .setAction("Reset") {
                            binding.apply {
                                usernameInput.text?.clear()
                                passwordInput.text?.clear()
                            }
                        }
                        .show()
                }

                is ValidationState.Invalid -> {
                    //Change textfield text colors
                    binding.apply {
                        usernameInput.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )
                        passwordInput.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.red
                            )
                        )

                        //Set error message
                        tvErrorMessage.apply {
                            text = state.message
                            if (alpha != 0f) alpha = 0f
                            animate().alpha(1f).setDuration(200).start()
                        }
                    }

                }
            }
        }
    }


    private fun hideKeyboard() {
        context?.let {
            val imm = requireContext().getSystemService(InputMethodManager::class.java)
            imm.hideSoftInputFromWindow(binding.passwordInput.windowToken, 0) // Hide the keyboard
        }
    }

    private fun validateLogin() {
        viewModel.validateCredentials(
            binding.usernameInput.text.toString(),
            binding.passwordInput.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}