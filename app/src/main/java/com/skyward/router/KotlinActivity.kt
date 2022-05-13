package com.skyward.router

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.skyward.gradle.router.runtime.Router
import com.skyward.router.annotations.Destination

@Destination(url = "router://page-kotlin", description = "Kotlin页面")
class KotlinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        findViewById<View>(R.id.btn).setOnClickListener {
            Router.go(
                it.context,
                "router://skyward/profile?name=skyward&message=hello"
            )
        }
    }
}