package com.skyward.gradle.router.runtime

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log

object Router {
    private const val TAG = "Router"
    private val mapping: HashMap<String, String> = HashMap()

    fun init() {
        try {
            val routerMappingClass =
                Class.forName("com.skyward.router.mapping.generated.RouterMapping")
            val method = routerMappingClass.getMethod("get")
            val allMapping = method.invoke(null) as Map<String, String>
            if (allMapping?.size > 0) {
                allMapping.onEach {
                    Log.d(TAG, "${it.key}: ${it.value}")
                }
                mapping.putAll(allMapping)
            }
        } catch (e: Throwable) {
            Log.e(TAG, "init router error: ", e)
        }

    }

    fun go(context: Context, url:String) {
        if(context == null || TextUtils.isEmpty(url)) {
            Log.e(TAG, "go: param error")
            return
        }

        val uri = Uri.parse(url)
        val scheme = uri.scheme
        val host = uri.host
        val path = uri.path

        var targetActivityClass = ""
        mapping.onEach {
            val tempUri = Uri.parse(it.key)
            val tempScheme = tempUri.scheme
            val tempHost = tempUri.host
            val tempPath = tempUri.path

            if (tempScheme == scheme &&
                    tempHost == host &&
                    tempPath == path) {
                targetActivityClass = it.value
            }
        }

        if (TextUtils.isEmpty(targetActivityClass)) {
            Log.d(TAG, "go: targetActivityClass not found")
            return
        }

        val bundle = Bundle()
        val query = uri.query
        query?.let {
            if (it.length >= 3) {
                val args = it.split("&")
                args.onEach {  arg->
                    val splits = arg.split("=")
                    bundle.putString(splits[0], splits[1])
                }
            }
        }

        try {
            val activityClass = Class.forName(targetActivityClass)
            val intent = Intent(context, activityClass)
            intent.putExtras(bundle)
            context.startActivity(intent)
        } catch (e:Throwable) {
            Log.e(TAG, "go: create activity failed $targetActivityClass", e)
        }



    }
}