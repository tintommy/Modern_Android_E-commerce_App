package com.example.appbanhangonline.util

import android.util.Patterns
import java.util.regex.Pattern

fun validateEmail(email: String): RegisterValidation {
    val emailPattern= Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    if (email.isEmpty())
        return RegisterValidation.Failed("Email không thể trống")
    if (email.matches(emailPattern)==false)
        return RegisterValidation.Failed("Email không đúng định dạng")
    return RegisterValidation.Success
}

fun validatePassword(password: String, rePassword: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Mật khẩu không thể trống")
    if (password.length < 8)
        return RegisterValidation.Failed("Mật khẩu phải có ít nhất 8 kí tự")
    if (password.equals(rePassword) == false)
        return RegisterValidation.Failed("Xác nhận mật khẩu không chính xác")
    return RegisterValidation.Success
}