package com.skyward.router

import android.app.Application
import com.skyward.gradle.router.runtime.Router

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Router.init()
    }
}