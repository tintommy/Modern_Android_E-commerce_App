package com.example.appbanhangonline.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.appbanhangonline.R
import com.example.appbanhangonline.adapters.CartProductAdapter
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.databinding.FragmentCartBinding
import com.example.appbanhangonline.firebase.FirebaseCommon
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.util.VerticalItemDecoration
import com.example.appbanhangonline.viewmodel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCartRv()
        cartViewModel.getCartProducts()
        var total:Float?=0f

        lifecycleScope.launch {
            cartViewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBarCart.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressBarCart.visibility = View.GONE

                        if (it.data!!.isEmpty()) {
                            showEmptyCard()
                            hideOtherView()
                        } else {
                            hideEmptyCard()
                            cartAdapter.differ.submitList(it.data)
                            showOtherView()
                        }
                    }

                    is Resource.Error -> {
                        binding.progressBarCart.visibility = View.GONE
                        Toast.makeText(requireContext(), "Lỗi khi tải giả hàng", Toast.LENGTH_SHORT)
                            .show()

                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            cartViewModel.cartProductPrice.collectLatest { price ->
                total= price as? Float
                binding.tvTotal.text = "$ " + price.toString()

            }
        }
        binding.btnCheckout.setOnClickListener {
            val action= total?.let { it1 ->
                CartFragmentDirections.actionCartFragmentToBillingFragment(
                    it1,cartAdapter.differ.currentList.toTypedArray())
            }
            if (action != null) {
                findNavController().navigate(action)
            }
        }
    }

    private fun showOtherView() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            btnCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideOtherView() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            btnCheckout.visibility = View.GONE
        }
    }

    private fun hideEmptyCard() {
        binding.layoutCartEmpty.visibility = View.GONE
    }

    private fun showEmptyCard() {
        binding.layoutCartEmpty.visibility = View.VISIBLE
    }

    private fun setUpCartRv() {
        binding.rvCart.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
            cartAdapter.setOnItemClickListener(object : CartProductAdapter.OnItemClickListener {
                override fun onItemClick(cartProduct: CartProduct) {
                    val b = Bundle()
                    b.putParcelable("product", cartProduct.product)
                    findNavController().navigate(
                        R.id.action_cartFragment_to_productDetailFragment,
                        b
                    )

                }

                override fun onPlusClick(cartProduct: CartProduct) {
                    cartViewModel.changeQuantity(
                        cartProduct,
                        FirebaseCommon.QuantityChanging.INCREASE
                    )
                }

                override fun onMinusClick(cartProduct: CartProduct) {
                    cartViewModel.changeQuantity(
                        cartProduct,
                        FirebaseCommon.QuantityChanging.DECREASE
                    )

                }
            })
        }
    }

}

