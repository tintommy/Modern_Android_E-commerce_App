package com.example.appbanhangonline.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Unspecified())
    val address = _address.asStateFlow()

    init {
        getUserAddresses()
    }

    fun getUserAddresses() {

        viewModelScope.launch {
            _address.emit(Resource.Loading())
            firestore.collection("user").document(auth.uid!!).collection("address")
                .addSnapshotListener { value, error ->

                    if (error != null) {
                        viewModelScope.launch { _address.emit(Resource.Error(error.message.toString())) }

                        return@addSnapshotListener
                    } else {
                        val address = value?.toObjects(Address::class.java)
                        viewModelScope.launch {
                            _address.emit(Resource.Success(address!!))
                        }
                    }


                }
        }
    }
}