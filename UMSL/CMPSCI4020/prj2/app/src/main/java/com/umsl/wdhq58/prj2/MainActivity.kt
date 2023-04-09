package com.umsl.wdhq58.prj2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

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
                R.id.action_people->setFragment(popularPeopleFragment)
            }
            true
        }

        //setup child spinner menu(bad design, but I want those extra points)
        val spinnerMenu: Spinner = findViewById<Spinner>(R.id.spSpinMenu)
        spinnerMenu.onItemSelectedListener = SpinnerMenuActivity(this,upcomingMovieFragment,
            popularMovieFragment, popularPeopleFragment)

        ArrayAdapter.createFromResource(this, R.array.spin_menu_array,
            android.R.layout.simple_spinner_item)
            .also{
                arrayAdapter ->  arrayAdapter.setDropDownViewResource(androidx.constraintlayout.widget.R.layout.support_simple_spinner_dropdown_item)
                spinnerMenu.adapter = arrayAdapter
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