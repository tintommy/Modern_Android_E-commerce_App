package com.example.appbanhangonline.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.appbanhangonline.R
import com.example.appbanhangonline.adapters.BestProductAdapter
import com.example.appbanhangonline.data.Category
import com.example.appbanhangonline.databinding.FragmentBaseCategoryBinding
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.viewmodel.CategoryViewModel
import com.example.appbanhangonline.viewmodel.factory.BaseCategoryViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class RefrigeratorFragment : BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore

    private val viewModel by viewModels<CategoryViewModel>() {
        BaseCategoryViewModelFactory(firestore, Category.Refrigerator)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.offerProgressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(it.data)

                        binding.offerProgressbar.visibility= View.GONE

                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        binding.offerProgressbar.visibility= View.GONE
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductProgressbar.visibility= View.VISIBLE
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductProgressbar.visibility= View.GONE

                    }
                    is Resource.Error -> {
                        Snackbar.make(requireView(),it.message.toString(), Snackbar.LENGTH_LONG).show()
                        binding.bestProductProgressbar.visibility= View.GONE
                    }
                    else -> {}
                }
            }
        }
    }

}