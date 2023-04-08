package com.umsl.wdhq58.prj2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj2.utility.adapter.MovieAdapter
import com.umsl.wdhq58.prj2.utility.api.ServiceBuilder
import com.umsl.wdhq58.prj2.utility.api.TMDBService
import com.umsl.wdhq58.prj2.utility.api.data.PopularMovies

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieView = findViewById<RecyclerView>(R.id.rvMovies)

        val service = ServiceBuilder.buildService(TMDBService::class.java)
        val serviceCall = service.getPopularMovies(getString(R.string.api_key))

        //retrofit style service call with callback functions for successful response and failure
        serviceCall.enqueue(object: Callback<PopularMovies>{
            override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>){
                movieView.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    adapter = MovieAdapter(response.body()!!.results)
                }
            }

            override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}