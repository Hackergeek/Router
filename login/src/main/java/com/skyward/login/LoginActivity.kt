package com.skyward.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.skyward.router.annotations.Destination

@Destination(url = "router://page-login", description = "登录页面")
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}