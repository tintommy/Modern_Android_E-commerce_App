package com.example.appbanhangonline.fragments.categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbanhangonline.R
import com.example.appbanhangonline.adapters.BestDealAdapter
import com.example.appbanhangonline.adapters.BestProductAdapter
import com.example.appbanhangonline.adapters.SpecialProductAdapter
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.FragmentMainCategoryBinding
import com.example.appbanhangonline.databinding.ProductRvItemBinding
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.util.showBottomNavigation
import com.example.appbanhangonline.viewmodel.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealsAdapter: BestDealAdapter
    private lateinit var bestProductAdapter: BestProductAdapter
    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        setUpSpecialProductRv()
        setUpBestDealsRv()
        setUpBestProductRv()

        specialProductAdapter.setOnItemClickListener(object :
            SpecialProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val b = Bundle().apply { putParcelable("product", product) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, b)
            }
        })

        bestDealsAdapter.setOnItemClickListener(object : BestDealAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val b = Bundle().apply { putParcelable("product", product) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, b)
            }
        })

        bestProductAdapter.setOnItemClickListener(object : BestProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val b = Bundle().apply { putParcelable("product", product) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, b)
            }

        })


        lifecycleScope.launchWhenStarted {
            viewModel.specialProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.specialProductProgressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        specialProductAdapter.differ.submitList(it.data)
                        binding.specialProductProgressbar.visibility = View.GONE

                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Đã xảy ra lỗi mục 1", Toast.LENGTH_SHORT)
                            .show()
                        binding.specialProductProgressbar.visibility = View.GONE
                    }

                    else -> {}
                }

            }


        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestDeal.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestDealsProgressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        binding.bestDealsProgressbar.visibility = View.GONE

                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Đã xảy ra lỗi mục 2", Toast.LENGTH_SHORT)
                            .show()
                        binding.bestDealsProgressbar.visibility = View.GONE
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.bestProduct.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.bestProductProgressbar.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        bestProductAdapter.differ.submitList(it.data)
                        binding.bestProductProgressbar.visibility = View.GONE

                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Đã xảy ra lỗi mục 2", Toast.LENGTH_SHORT)
                            .show()
                        binding.bestProductProgressbar.visibility = View.GONE
                    }

                    else -> {}
                }
            }
        }
        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                viewModel.fetchBestProduct()

            }

        })
        hideLoading()

    }


    private fun hideLoading() {
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }

    private fun setUpSpecialProductRv() {
        specialProductAdapter = SpecialProductAdapter()
        binding.rvSpecialProduct.apply {

            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter


//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    if (!recyclerView.canScrollVertically(1) && dx != 0) {
//                        viewModel.fetchSpecialProducts()
//                        Toast.makeText(requireContext(), "Cuối rv spe", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            })
            var isSpecialProductsScrolling = false
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isSpecialProductsScrolling = true
                    }

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val sizeOfTheCurrentList = layoutManager.itemCount
                    val visibleItems = layoutManager.childCount
                    val topPosition = layoutManager.findFirstVisibleItemPosition()

                    val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
                    val shouldPaginate = hasReachedToEnd && isSpecialProductsScrolling

                    if (shouldPaginate) {

                        viewModel.fetchSpecialProducts()
                        isSpecialProductsScrolling = false
                    }
                }
            })


        }
    }

    private fun setUpBestDealsRv() {
        bestDealsAdapter = BestDealAdapter()
        binding.rvBestDealProduct.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter

            var isBestDealsScrolling = false
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isBestDealsScrolling = true
                    }

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val sizeOfTheCurrentList = layoutManager.itemCount
                    val visibleItems = layoutManager.childCount
                    val topPosition = layoutManager.findFirstVisibleItemPosition()

                    val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
                    val shouldPaginate = hasReachedToEnd && isBestDealsScrolling

                    if (shouldPaginate) {

                        viewModel.fetchBestDeals()
                        isBestDealsScrolling = false
                    }
                }
            })


        }
    }

    private fun setUpBestProductRv() {
        bestProductAdapter = BestProductAdapter()
        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = bestProductAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }
}