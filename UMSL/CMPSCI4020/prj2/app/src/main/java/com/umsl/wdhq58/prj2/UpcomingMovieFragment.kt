package com.umsl.wdhq58.prj2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj2.utility.adapter.MovieAdapter
import com.umsl.wdhq58.prj2.utility.api.ServiceBuilder
import com.umsl.wdhq58.prj2.utility.api.TMDBService
import com.umsl.wdhq58.prj2.utility.api.data.PopularMovies
import com.umsl.wdhq58.prj2.utility.api.data.UpcomingMovies
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpcomingMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpcomingMovieFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_popular_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = ServiceBuilder.buildService(TMDBService::class.java)
        val serviceCall = service.getUpcomingMovies(getString(R.string.api_key))
        val movieView = view.findViewById<RecyclerView>(R.id.rvMovies)

        //retrofit style service call with callback functions for successful response and failure
        serviceCall.enqueue(object: Callback<UpcomingMovies> {
            override fun onResponse(call: Call<UpcomingMovies>, response: Response<UpcomingMovies>){
               if(response.isSuccessful){
                   movieView.apply {
                       setHasFixedSize(true)
                       layoutManager = LinearLayoutManager(this@UpcomingMovieFragment.context)
                       adapter = MovieAdapter(response.body()!!.result)
                   }
               }

            }

            override fun onFailure(call: Call<UpcomingMovies>, t: Throwable) {
                Toast.makeText(this@UpcomingMovieFragment.activity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PopularMovieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpcomingMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}