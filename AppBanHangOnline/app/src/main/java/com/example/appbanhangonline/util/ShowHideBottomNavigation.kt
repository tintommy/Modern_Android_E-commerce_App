package com.example.appbanhangonline.util

import android.view.View
import androidx.fragment.app.Fragment
import com.example.appbanhangonline.R
import com.example.appbanhangonline.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation(){
    val bottomNavigation =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)

    bottomNavigation.visibility= View.GONE

}

fun Fragment.showBottomNavigation(){
    val bottomNavigation =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigation)

    bottomNavigation.visibility= View.VISIBLE

}