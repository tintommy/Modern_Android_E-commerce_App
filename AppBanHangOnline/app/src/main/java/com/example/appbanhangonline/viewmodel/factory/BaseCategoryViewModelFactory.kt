package com.example.appbanhangonline.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appbanhangonline.data.Category
import com.example.appbanhangonline.fragments.categories.BaseCategoryFragment
import com.example.appbanhangonline.viewmodel.CategoryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class BaseCategoryViewModelFactory(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(firestore, category) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")

    }
}