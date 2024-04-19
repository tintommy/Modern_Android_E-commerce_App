package com.example.appbanhangonline.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbanhangonline.R
import com.example.appbanhangonline.adapters.BestProductAdapter
import com.example.appbanhangonline.data.Product
import com.example.appbanhangonline.databinding.FragmentBaseCategoryBinding
import com.example.appbanhangonline.util.showBottomNavigation


open class BaseCategoryFragment : Fragment() {
    protected lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductsAdapter: BestProductAdapter by lazy { BestProductAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOfferRv()
        setUpBestProductsRv()


        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            if (v.getChildAt(0).bottom <= v.height + scrollY) {
                onBestProductsPagingRequest()

            }

        })

        bestProductsAdapter.setOnItemClickListener(object : BestProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val b = Bundle().apply { putParcelable("product", product) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, b)
            }

        })
        offerAdapter.setOnItemClickListener(object : BestProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                val b = Bundle().apply { putParcelable("product", product) }
                findNavController().navigate(R.id.action_homeFragment_to_productDetailFragment, b)
            }

        })
    }

    private fun setUpOfferRv() {

        binding.rvOffer.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
            var isOfferProductsScrolling = false
          addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isOfferProductsScrolling = true
                    }

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val sizeOfTheCurrentList = layoutManager.itemCount
                    val visibleItems = layoutManager.childCount
                    val topPosition = layoutManager.findFirstVisibleItemPosition()

                    val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
                    val shouldPaginate = hasReachedToEnd && isOfferProductsScrolling

                    if (shouldPaginate) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        onOfferPagingRequest()
                        isOfferProductsScrolling = false
                    }
                }
            })


        }
    }

    open fun onOfferPagingRequest() {

    }

    open fun onBestProductsPagingRequest() {

    }

    private fun setUpBestProductsRv() {

        binding.rvBestProduct.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = bestProductsAdapter

            var isProductsScrolling = false
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isProductsScrolling = true
                    }

                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = layoutManager as LinearLayoutManager
                    val sizeOfTheCurrentList = layoutManager.itemCount
                    val visibleItems = layoutManager.childCount
                    val topPosition = layoutManager.findFirstVisibleItemPosition()

                    val hasReachedToEnd = topPosition + visibleItems >= sizeOfTheCurrentList
                    val shouldPaginate = hasReachedToEnd && isProductsScrolling

                    if (shouldPaginate) {
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        onBestProductsPagingRequest()
                        isProductsScrolling = false
                    }
                }
            })


        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }

}