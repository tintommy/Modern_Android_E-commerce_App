package com.example.appbanhangonline.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appbanhangonline.R
import com.example.appbanhangonline.adapters.AddressAdapter
import com.example.appbanhangonline.adapters.BillingProductAdapter
import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.data.CartProduct
import com.example.appbanhangonline.data.Order.Order
import com.example.appbanhangonline.data.Order.OrderStatus
import com.example.appbanhangonline.databinding.FragmentBillingBinding
import com.example.appbanhangonline.util.HorizontalItemDecoration
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.viewmodel.BillingViewModel
import com.example.appbanhangonline.viewmodel.OrderViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val billingProductAdapter by lazy { BillingProductAdapter() }
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingViewModel by viewModels<BillingViewModel>()
    private val args by navArgs<BillingFragmentArgs>()
    private var products = emptyList<CartProduct>()
    private var total = 0f

    private var selectedAddress: Address? = null
    private val orderViewModel by viewModels<OrderViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        products = args.products.toList()

        total = args.totalFloat

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBillingRv()
        setUpAddressRv()
        Toast.makeText(requireContext(), products.size.toString(), Toast.LENGTH_SHORT).show()
        binding.imageAddAddress.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
        lifecycleScope.launch {
            billingViewModel.address.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressbarAddress.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarAddress.visibility = View.GONE
                        addressAdapter.differ.submitList(it.data)
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }

        lifecycleScope.launch {
            orderViewModel.order.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.buttonPlaceOrder.startAnimation()
                    }

                    is Resource.Success -> {
                        binding.buttonPlaceOrder.revertAnimation()
                        findNavController().navigateUp()
                        Snackbar.make(
                            requireView(),
                            "Đơn hàng của bạn đã được đặt",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    is Resource.Error -> {
                        binding.progressbarAddress.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {

                    }
                }
            }
        }
        billingProductAdapter.differ.submitList(products)
        binding.tvTotalPrice.text = "$ $total"

        binding.buttonPlaceOrder.setOnClickListener {
            if (selectedAddress == null) {
                Toast.makeText(requireContext(), "Hãy chọn địa chỉ giao hàng", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            showConfirmDialog()
        }
    }

    private fun showConfirmDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Xác nhận đặt hàng")
        builder.setTitle("XÁC NHẬN !")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") { dialog, which ->
            val order = Order(OrderStatus.Ordered.status, total, products, selectedAddress!!)
            orderViewModel.placeOrder(order)
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun setUpAddressRv() {
        binding.apply {
            rvAddress.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvAddress.adapter = addressAdapter
            rvAddress.addItemDecoration(HorizontalItemDecoration())
        }

        addressAdapter.setOnItemClickListener(object : AddressAdapter.OnItemClickListener {
            override fun onItemClick(address: Address) {
                selectedAddress = address
            }
        })


    }

    private fun setUpBillingRv() {
        binding.apply {
            rvProducts.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvProducts.adapter = billingProductAdapter
            rvProducts.addItemDecoration(HorizontalItemDecoration())
        }

    }
}