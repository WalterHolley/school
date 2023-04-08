package com.umsl.wdhq58.prj2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val popularMovieFragment = PopularMovieFragment()
        val upcomingMovieFragment = UpcomingMovieFragment()
        val popularPeopleFragment = PopularPeopleFragment()
        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        setFragment(popularMovieFragment)

        //setup bottom navigation bar
        bottomNavView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.action_popular->setFragment(popularMovieFragment)
                R.id.action_upcoming->setFragment(upcomingMovieFragment)
                //R.id.action_people->setFragment(popularPeopleFragment)
            }
            true
        }

    }

    /**
     * sets a fragment to the framelayout in the main activity
     */
    private fun setFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentView, fragment)
            commit()
        }

}