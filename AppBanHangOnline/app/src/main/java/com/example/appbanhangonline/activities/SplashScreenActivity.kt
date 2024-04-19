package com.example.appbanhangonline.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.appbanhangonline.R
import com.example.appbanhangonline.viewmodel.IntroductionViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {
    private val viewModel by viewModels<IntroductionViewModel>()
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)





        handler.postDelayed({
            lifecycleScope.launchWhenStarted {
                viewModel.navigate.collect{
                    when(it) {
                        IntroductionViewModel.SHOPPING_ACTIVITY ->{

                            Intent(this@SplashScreenActivity, ShoppingActivity::class.java).also {intent->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)

                            }
                        }
                        IntroductionViewModel.ACCOUNT_OPTIONS_FRAGMENT -> {
                            Intent(
                                this@SplashScreenActivity,
                                LoginRegisterActivity::class.java
                            ).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }
                        else ->{}
                    }

                }

            }
            finish()
        }, 1500)
    }


}