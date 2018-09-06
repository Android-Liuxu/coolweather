package com.example.coolweather.util

import android.text.TextUtils
import android.widget.TextView

import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
//import com.example.coolweather.gson.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Administrator on 2018/7/5/005.
 * 解析和处理服务器返回的省级数据
 */

object Utility {
    fun handleProvinceResponse(response: String): Boolean {
        if (!TextUtils.isEmpty(response)) {
            try {
                val allProvince = JSONArray(response)
                for (i in 0 until allProvince.length()) {
                    val provinceObjece = allProvince.getJSONObject(i)
                    val province = Province()
                    province.provinceName = provinceObjece.getString("name")
                    province.provinceCode = provinceObjece.getInt("id")
                    province.save()
                }
                return true

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return false
    }

    /**
     * 解析和处理市级返回的数据
     */
    fun handleCityResponse(response: String, provinceId: Int): Boolean {
        if (!TextUtils.isEmpty(response)) {
            try {
                val allCities = JSONArray(response)
                for (i in 0 until allCities.length()) {
                    val cityObject = allCities.getJSONObject(i)
                    val city = City()
                    city.cityName = cityObject.getString("name")
                    city.cityCode = cityObject.getInt("id")
                    city.provinceId = provinceId
                    city.save()

                }
                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return false
    }

    /**
     * 解析和处理县级返回的数据
     */
    fun handleCountyResponse(response: String, cityId: Int): Boolean {
        if (!TextUtils.isEmpty(response)) {
            try {
                val allCounties = JSONArray(response)
                for (i in 0 until allCounties.length()) {
                    val countyObject = allCounties.getJSONObject(i)
                    val county = County()
                    county.countyName = countyObject.getString("name")
                    county.weatherId = countyObject.getString("weather_id")
                    county.cityId = cityId
                    county.save()
                }

                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return false

    }



}
