package com.example.appbanhangonline.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.appbanhangonline.R
import com.example.appbanhangonline.data.User
import com.example.appbanhangonline.databinding.FragmentRegisterBinding
import com.example.appbanhangonline.util.RegisterValidation
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.io.path.Path

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        binding.apply {
            buttonRegister.setOnClickListener {

//               if (checkValid())
//                {
//                    val user = User(
//                        etFirstName.text.toString().trim(),
//                        etLastName.text.toString().trim(),
//                        etEmail.text.toString().trim()
//                    )
//                    viewModel.createAccountWithEmailAndPassword(user, etPass.toString().trim(),etRePass.toString().trim())
//                }

                val user = User(
                    etFirstName.text.toString().trim(),
                    etLastName.text.toString().trim(),
                    etEmail.text.toString().trim()
                )
                viewModel.createAccountWithEmailAndPassword(
                    user,
                    etPass.text.toString().trim(),
                    etRePass.text.toString().trim()
                )
            }
        }

        lifecycleScope.launchWhenStarted {


            viewModel.register.collect() {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonRegister.startAnimation()
                    }

                    is Resource.Success -> {
                        Toast.makeText(
                            activity,
                            "Tạo tài khoản thành công ",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.buttonRegister.revertAnimation()

                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }

                    is Resource.Error -> {
                        Toast.makeText(
                            activity,
                            "Error " + it.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                        binding.buttonRegister.revertAnimation()
                    }

                    is Resource.Unspecified -> {}
                }
            }
        }


        lifecycleScope.launchWhenStarted {
            viewModel.validation.observe(viewLifecycleOwner) {
                if (it.email is RegisterValidation.Failed) {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.etEmail.requestFocus()
                        binding.etEmail.setError(it.email.message)
                    }

                }
                if (it.password is RegisterValidation.Failed) {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.etPass.requestFocus()
                        binding.etPass.setError(it.password.message)
                    }

                }
            }

        }

    }

//    private fun checkValid(): Boolean {
//        var fault = false
//        binding.apply {
//            if (etFirstName.text.toString().trim().equals("")) {
//                etFirstName.setError("Hãy điền họ")
//                fault = true
//            }
//            if (etLastName.text.toString().trim().equals("")) {
//                etLastName.setError("Hãy điền tên")
//                fault = true
//            }
//            if (etEmail.text.toString().trim().equals("")) {
//                etEmail.setError("Hãy điền email")
//                fault = true
//            }
//
//            if(!Patterns.EMAIL_ADDRESS.equals(etEmail.text.toString().trim())){
//                etEmail.setError("Email không đúng định dạng")
//                fault = true
//            }
//
//            if (etPass.text.toString().trim().length<8) {
//                etPass.setError("Mật khẩu phải lớn hơn 8 kí tự")
//                fault = true
//            }
//            if (etPass.text.toString().trim().equals("")) {
//                etPass.setError("Hãy điền mật khẩu")
//                fault = true
//            }
//            if(etPass.text.toString().trim().equals(etRePass.text.toString().trim())==false)
//            {
//                etRePass.setError("Xác nhận mật khẩu không đúng")
//                fault = true
//            }
//        }
//        return fault ==false
//    }

}