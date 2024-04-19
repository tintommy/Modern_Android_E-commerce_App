package com.example.appbanhangonline.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appbanhangonline.adapters.HomeViewpagerAdapter
import com.example.appbanhangonline.databinding.FragmentHomeBinding
import com.example.appbanhangonline.fragments.categories.CookerFragment
import com.example.appbanhangonline.fragments.categories.FanFragment
import com.example.appbanhangonline.fragments.categories.MainCategoryFragment
import com.example.appbanhangonline.fragments.categories.RefrigeratorFragment
import com.example.appbanhangonline.fragments.categories.TvFragment
import com.example.appbanhangonline.fragments.categories.WasherFragment
import com.google.android.material.tabs.TabLayoutMediator

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //tạo categories ngang ( cần có kết hợp giữa viewPager2 và tabLayout)
        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            TvFragment(),
            RefrigeratorFragment(),
            WasherFragment(),
            FanFragment(),
            CookerFragment()
        )

        //tắt vuốt ngang chuyển tab
        binding.viewPagerHome.isUserInputEnabled=false

        val viewPager2Adapter = HomeViewpagerAdapter(categoriesFragments,childFragmentManager, lifecycle)
        binding.viewPagerHome.adapter= viewPager2Adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPagerHome){
            tab, position ->
            when(position){
                0 -> tab.text = "Trang chính"
                1 -> tab.text = "Tivi"
                2 -> tab.text = "Tủ lạnh"
                3 -> tab.text = "Máy giặt"
                4 -> tab.text = "Quạt"
                5 -> tab.text = "Nồi cơm"

            }
        }.attach()
    }

}