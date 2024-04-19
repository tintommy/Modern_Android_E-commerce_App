package com.example.appbanhangonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appbanhangonline.data.User
import com.example.appbanhangonline.util.Constants.USER_COLLECTION
import com.example.appbanhangonline.util.RegisterFieldState
import com.example.appbanhangonline.util.RegisterValidation
import com.example.appbanhangonline.util.Resource
import com.example.appbanhangonline.util.validateEmail
import com.example.appbanhangonline.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register

    private val _validation = MutableLiveData<RegisterFieldState>()
    val validation: LiveData<RegisterFieldState> = _validation

    fun createAccountWithEmailAndPassword(user: User, password: String, rePassword: String) {

        if (checkValidation(user, password, rePassword)) {

            runBlocking {
                _register.value = Resource.Loading()
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                saveUserInfo(it.user?.uid, user)
                _register.value = Resource.Success(user)

            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
        } else {
            val registerFieldState = RegisterFieldState(
                validateEmail(user.email), validatePassword(password, rePassword)
            )
            runBlocking {
                _validation.postValue(registerFieldState)
            }

        }
    }

    private fun saveUserInfo(userUid: String?, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid!!)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidation(
        user: User,
        password: String,
        rePassword: String
    ): Boolean {
        val emailValidation = validateEmail(user.email)
        val passValidation = validatePassword(password, rePassword)
        val shouldRegister =
            emailValidation is RegisterValidation.Success &&
                    passValidation is RegisterValidation.Success

        return shouldRegister
    }
}