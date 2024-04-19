package com.example.appbanhangonline.fragments.loginRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.appbanhangonline.R
import com.example.appbanhangonline.databinding.FragmentAccountOptionsBinding
import com.example.appbanhangonline.databinding.FragmentIntroductionBinding

class AccountOptionFragment:Fragment(R.layout.fragment_account_options) {
    private lateinit var binding: FragmentAccountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountOptionsBinding.bind(view)
        binding.buttonLoginAccountOptions.setOnClickListener{
            it.findNavController().navigate(R.id.action_accountOptionFragment_to_loginFragment)
        }
        binding.buttonRegisterAccountOptions.setOnClickListener{
            it.findNavController().navigate(R.id.action_accountOptionFragment_to_registerFragment)
        }
    }
}