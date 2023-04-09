package com.umsl.wdhq58.prj2

import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment



class SpinnerMenuActivity(currentActivity: MainActivity, upcomingMovieFragment: UpcomingMovieFragment, popularMovieFragment: PopularMovieFragment,
popularPeopleFragment: PopularPeopleFragment) : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val popularPersons = popularPeopleFragment
    private val popularMovies = popularMovieFragment
    private val upcomingMovies = upcomingMovieFragment
    private val activity = currentActivity

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        when(p2){
            0 -> setFragment(popularMovies)
            1 -> setFragment(popularPersons)
            2 -> setFragment(upcomingMovies)
            else -> {
                Toast.makeText(activity, "Invalid Selection", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    /**
     * sets a fragment to the framelayout in the main activity
     */
    private fun setFragment(fragment: Fragment) =
        activity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentView, fragment)
            commit()
        }

}