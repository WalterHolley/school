package com.umsl.wdhq58.androidapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    private  val logTag = "umsl:4020:wdhq58"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        Log.i(logTag, "onStart override initiated")
        super.onStart()
    }

    override fun onResume() {
        Log.i(logTag, "onResume override initiated")
        super.onResume()
    }

    override fun onPause() {
        Log.i(logTag, "onPause override initiated")
        super.onPause()

    }

    override fun onStop() {
        Log.i(logTag, "onStop override initiated")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(logTag, "onDestroy override initiated")
        super.onDestroy()
    }
}