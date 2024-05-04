package com.example.appbanhangonline.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appbanhangonline.data.Address
import com.example.appbanhangonline.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {


    private val _addNewAddress = MutableStateFlow<Resource<Address>>(Resource.Unspecified())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _error = MutableSharedFlow<String>()
    val error = _error.asSharedFlow()
    fun addAddress(address: Address) {

        val validInput = validInput(address)
        if(validInput){
        firestore.collection("user").document(auth.uid!!).collection("address").document()
            .set(address).addOnSuccessListener {
                viewModelScope.launch {
                    _addNewAddress.emit(
                        Resource.Success(address)
                    )

                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addNewAddress.emit(
                        Resource.Error(it.message.toString())
                    )
                }
            }}
        else{
            viewModelScope.launch {
                _error.emit("Hãy nhập đủ thông tin")
            }

        }
    }

    private fun validInput(address: Address): Boolean {
        return address.addressTittle.trim().isNotEmpty() && address.fullName.trim().isNotEmpty()
                && address.city.trim().isNotEmpty() && address.phone.trim().isNotEmpty()
                && address.state.trim().isNotEmpty() && address.street.trim().isNotEmpty()
    }
}