package com.umsl.wdhq58.weatherfinal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.umsl.wdhq58.weatherfinal.utility.api.ServiceBuilder
import com.umsl.wdhq58.weatherfinal.utility.api.WeatherService
import com.umsl.wdhq58.weatherfinal.utility.api.data.WeatherData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "lat"
private const val ARG_PARAM2 = "long"
private const val DEGREE_SYMBOL = "°"

/**
 * A simple [Fragment] subclass.
 * Use the [weather.newInstance] factory method to
 * create an instance of this fragment.
 */
class weather : Fragment() {

    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            latitude = it.getDouble(ARG_PARAM1)
            longitude = it.getDouble(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val service = ServiceBuilder.buildService(WeatherService::class.java)
        val serviceCall = service.getWeather(latitude!!, longitude!!,"imperial",
            getString(R.string.WEATHER_API_KEY))

        serviceCall.enqueue(object: Callback<WeatherData> {
            override fun onResponse(call: Call<WeatherData>, response: Response<WeatherData>) {

                populateView(response.body()!!, view)

            }

            override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                Toast.makeText(view.context, "There was a problem reaching the weather API", Toast.LENGTH_SHORT)
            }

        })
    }

    private fun populateView(data: WeatherData, view: View){
        val temperatureText = String.format("%.0f%s", data.main.temp, DEGREE_SYMBOL)
        val hiLowText = String.format("%.0f%s%c%.0f%s %s %.0f%s", data.main.temp_min, DEGREE_SYMBOL,
        '/', data.main.temp_max, DEGREE_SYMBOL, getString(R.string.feels_like), data.main.feels_like, DEGREE_SYMBOL)

        //view objects
        val temp = view.findViewById<TextView>(R.id.tvTemp)
        val hilow = view.findViewById<TextView>(R.id.tvHiLow)
        val location = view.findViewById<TextView>(R.id.tvLocationName)
        val description = view.findViewById<TextView>(R.id.tvDescription)

        //update values
        temp.setText(temperatureText)
        hilow.setText(hiLowText)
        location.setText(data.name)
        description.setText((data.weather.get(0).description))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment weather.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(lat: Double, long: Double) =
            weather().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_PARAM1, lat)
                    putDouble(ARG_PARAM2, long)
                }
            }
    }
}