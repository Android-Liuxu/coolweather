package com.example.coolweather

import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.coolweather.db.County
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import interfaces.heweather.com.interfacesmodule.bean.Lang
import interfaces.heweather.com.interfacesmodule.bean.Unit
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather
import interfaces.heweather.com.interfacesmodule.view.HeConfig
import interfaces.heweather.com.interfacesmodule.view.HeWeather
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast_item.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggestion.*
import kotlinx.android.synthetic.main.title.*
import java.util.*

class WeatherActivity : AppCompatActivity() {
    private var location:String? = null
    private var forecastLayout: LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        HeConfig.init("HE1807041958511033", "48204e438982464098a4a17f73a8a830")
        HeConfig.switchToFreeServerNode()
        forecastLayout = findViewById<View>(R.id.forecast_layout) as LinearLayout
        val pref:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        location = pref.getString("location",null)
        if (location == null){
            //有缓存时解析数据
            location = intent.getStringExtra("county_name")
            val editor = PreferenceManager
                    .getDefaultSharedPreferences(this@WeatherActivity)
                    .edit()
            editor.putString("location",location)
            editor.apply()

        }

        /**
         * 获取天气信息
         */
        HeWeather.getWeather(this@WeatherActivity, location,Lang.CHINESE_SIMPLIFIED,
                Unit.METRIC,object:HeWeather.OnResultWeatherDataListBeansListener{
            override fun onSuccess(p0: MutableList<Weather>?) {
                val weather = Gson().toJson(p0)
                handleWeatherResponse(weather)
            }

            override fun onError(p0: Throwable?) {
                Toast.makeText(this@WeatherActivity,
                        "获取天气信息失败",Toast.LENGTH_SHORT).show()
            }
        })



    }


    /**
     * 设置天气界面
     */
    fun handleWeatherResponse(weather:String){
        try {
            val gson = Gson()
            val weatherList = gson.fromJson<List<Weather>>(weather,
                    object : TypeToken<List<Weather>>(){}.type)
            forecastLayout!!.removeAllViews()
            for (weather in weatherList){
                degree_text.text = weather.now.tmp +"℃"
                info_text.text = weather.now.cond_txt
                title_city.text = weather.basic.location
                title_update_time.text = weather.update.loc
                aqi_text.text = weather.now.wind_dir
                pm25_text.text = weather.now.wind_sc
                for (life in weather.lifestyle){
                    when(life.type){
                        "sport" -> sport_text.text = "运动建议："+life.txt
                        "cw"    -> car_wash_text.text = "洗车指数："+life.txt
                        "comf"  -> comfort_text.text = "舒适度："+life.txt
                    }

                }
                for (forecast in weather.daily_forecast){
                    val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,
                            forecastLayout, false)
                    val dateText = view.findViewById<View>(R.id.date_text) as TextView
                    val infoText = view.findViewById<View>(R.id.info2_text) as TextView
                    val maxText = view.findViewById<View>(R.id.max_text) as TextView
                    val minText = view.findViewById<View>(R.id.min_text) as TextView
                    dateText.text =forecast.date
                    infoText.text = forecast.cond_txt_d
                    maxText.text = forecast.tmp_max
                    minText.text = forecast.tmp_min
                    forecastLayout!!.addView(view)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
