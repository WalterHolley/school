package com.umsl.wdhq58.prj2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umsl.wdhq58.prj2.utility.api.ServiceBuilder
import com.umsl.wdhq58.prj2.utility.api.TMDBService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = ServiceBuilder.buildService(TMDBService::class.java)
    }
}