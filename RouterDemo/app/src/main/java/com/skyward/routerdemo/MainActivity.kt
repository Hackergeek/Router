package com.skyward.routerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.skyward.gradle.router.runtime.Router
import com.skyward.router.annotations.Destination

@Destination(url = "router://page-home", description = "应用主页")
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btn).setOnClickListener {
            Router.go(
                it.context,
                "router://skyward/profile?name=skyward&message=hello"
            )
        }
    }
}