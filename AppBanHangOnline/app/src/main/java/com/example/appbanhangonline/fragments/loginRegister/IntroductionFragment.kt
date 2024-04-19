package com.example.appbanhangonline.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.appbanhangonline.R
import com.example.appbanhangonline.activities.ShoppingActivity
import com.example.appbanhangonline.databinding.FragmentIntroductionBinding
import com.example.appbanhangonline.viewmodel.IntroductionViewModel
import com.example.appbanhangonline.viewmodel.IntroductionViewModel.Companion.ACCOUNT_OPTIONS_FRAGMENT
import com.example.appbanhangonline.viewmodel.IntroductionViewModel.Companion.SHOPPING_ACTIVITY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class IntroductionFragment : Fragment(R.layout.fragment_introduction) {
    private lateinit var binding: FragmentIntroductionBinding
    private val viewModel by viewModels<IntroductionViewModel>()
    val firebaseAuth: FirebaseAuth=FirebaseAuth.getInstance()
    val user = firebaseAuth.currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentIntroductionBinding.bind(view)

        lifecycleScope.launchWhenStarted {
            viewModel.navigate.collect{
                when(it) {
                    SHOPPING_ACTIVITY ->{

                        Intent(requireActivity(), ShoppingActivity::class.java).also {intent->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)

                        }
                    }
                    ACCOUNT_OPTIONS_FRAGMENT ->{

                    }
                    else ->{}
                }

            }

        }

        lifecycleScope.launchWhenStarted {
            viewModel.email.observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "Đã đăng nhập vào $it", Toast.LENGTH_SHORT).show()

            }
        }

        binding.btnStart.setOnClickListener{
            viewModel.startButtonClick()
            it.findNavController().navigate(R.id.action_introductionFragment_to_accountOptionFragment)
        }
    }
}