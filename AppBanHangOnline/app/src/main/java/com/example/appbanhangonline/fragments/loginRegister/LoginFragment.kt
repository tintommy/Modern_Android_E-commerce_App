package com.example.appbanhangonline.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.appbanhangonline.R
import com.example.appbanhangonline.activities.LoginRegisterActivity
import com.example.appbanhangonline.activities.ShoppingActivity
import com.example.appbanhangonline.databinding.FragmentLoginBinding
import com.example.appbanhangonline.dialog.setupBottomSheetDialog
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.viewmodel.LoginViewModel
import com.example.appbanhangonline.viewmodel.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.apply {
            buttonLogin.setOnClickListener {
                val email = etEmailLogin.text.toString().trim()
                val password = etPassLogin.text.toString()
                viewModel.login(email, password)
            }

        }

        binding.tvForgotPass.setOnClickListener {
            setupBottomSheetDialog { email -> viewModel.resetPassword(email) }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.resetPass.collect {
                when (it) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        Snackbar.make(
                            requireView(),
                            "Link đã được gửi đến email của bạn",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is Resource.Error -> {

                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.login.collect {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonLogin.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonLogin.revertAnimation()
                        Intent(requireActivity(), ShoppingActivity::class.java).also {
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(it)
                        }
                    }

                    is Resource.Error -> {
                        binding.buttonLogin.revertAnimation()
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }

                    else -> Unit
                }

            }
        }

      //  (activity as LoginRegisterActivity).finish()
    }


}