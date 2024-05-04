package com.example.appbanhangonline.fragments.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStateAtLeast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appbanhangonline.R
import com.example.appbanhangonline.activities.ShoppingActivity
import com.example.appbanhangonline.adapters.ColorAdapter
import com.example.appbanhangonline.adapters.SizeAdapter
import com.example.appbanhangonline.adapters.ViewPager2Adapter
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.databinding.FragmentProductDetailBinding
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.util.hideBottomNavigation
import com.example.appbanhangonline.viewmodel.DetailsViewModel
import com.example.appbanhangonline.viewmodel.MainCategoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.observeOn

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private val arg by navArgs<ProductDetailFragmentArgs>()
    private lateinit var binding: FragmentProductDetailBinding
    private val viewpagerAdapter by lazy { ViewPager2Adapter() }
    private val sizeAdapter by lazy { SizeAdapter() }
    private val colorAdapter by lazy { ColorAdapter() }
    private var seletedColor: Int? = null
    private var selectedSize: String? = "s"
    private val viewModel by viewModels<DetailsViewModel>()
    private var soLuong: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigation()

        binding = FragmentProductDetailBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = arg.product
        setUpSizeRv()
        setUpColorRv()
        setUpViewpager()



        sizeAdapter.setOnItemClickListener(object : SizeAdapter.OnItemClickListener {
            override fun onItemClick(size: String) {
                selectedSize = size
            }
        })

        colorAdapter.setOnItemClickListener(object : ColorAdapter.OnItemClickListener {
            override fun onItemClick(color: Int) {
                seletedColor = color
            }
        })

        binding.btnAddToCart.setOnClickListener {
            if ( seletedColor == null) {
                Toast.makeText(
                    requireContext(),
                    "Hãy chọn màu của sản phẩm",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.addUpdateProduct(CartProduct(product, soLuong, selectedSize, seletedColor))
            }


        }

        lifecycleScope.launchWhenStarted {
            viewModel.addToCart.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.btnAddToCart.startAnimation()

                    }

                    is Resource.Success -> {
                        binding.btnAddToCart.revertAnimation()
                        Snackbar.make(view, "Đã thêm sản phẩm vào giỏ hàng", Snackbar.LENGTH_SHORT)
                            .show()
                    }

                    is Resource.Error -> {
                        binding.btnAddToCart.stopAnimation()
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }

        binding.apply {
            tvProductName.text = product.name
            tvProductPrice.text = "$ ${product.price}"
            tvProductDescription.text = product.description

            ivClose.setOnClickListener {
                findNavController().navigateUp()
            }

            if (product.colors.isNullOrEmpty()) {
                tvProductColor.visibility = View.INVISIBLE
            }

            if (product.sizes.isNullOrEmpty()) {
                tvProductSize.visibility = View.INVISIBLE
            }


        }
        binding.btnTang.setOnClickListener {
            soLuong = binding.etSoLuong.text.toString().trim().toInt() + 1
            binding.etSoLuong.setText(soLuong.toString())

        }
        binding.btnGiam.setOnClickListener {
            soLuong = binding.etSoLuong.text.toString().trim().toInt() - 1
            binding.etSoLuong.setText(soLuong.toString())
        }



        viewpagerAdapter.differ.submitList(product.images)
        product.colors.let {
            colorAdapter.differ.submitList(it)
        }

        product.sizes.let {
            sizeAdapter.differ.submitList(it)
        }


    }

    private fun setUpViewpager() {
        binding.apply {
            viewPagerProductImages.adapter = viewpagerAdapter
        }
    }

    private fun setUpColorRv() {
        binding.rvColor.apply {
            adapter = colorAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpSizeRv() {
        binding.rvSize.apply {
            adapter = sizeAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


}