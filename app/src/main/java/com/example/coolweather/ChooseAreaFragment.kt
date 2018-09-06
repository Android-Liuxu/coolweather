package com.example.coolweather

import android.app.Fragment
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.coolweather.db.City
import com.example.coolweather.db.County
import com.example.coolweather.db.Province
import com.example.coolweather.util.HttpUtil
import com.example.coolweather.util.Utility
import kotlinx.android.synthetic.main.choose_area.*
import okhttp3.Call
import okhttp3.Response
import org.litepal.crud.DataSupport
import java.io.IOException
import javax.security.auth.callback.Callback

class ChooseAreaFragment:Fragment(){
    companion object {
        val LEVEL_PROVINCE = 0
        val LEVEL_CITY = 1
        val LEVEL_COUNTY = 2
    }
    private var progressDialog:ProgressDialog? = null
    private var titleText: TextView? = null
    private var backButton: Button? = null
    private var listView: ListView? = null
    private var adapter:ArrayAdapter<String>? = null
    private var dataList = ArrayList<String>()
    private var provinceList:List<Province>? = null
    private var cityList:List<City>? = null
    private var countyList:List<County>? = null
    var selectedProvince:Province? = null
    var selectedCity:City? = null
    private var currentLevel:Int? = 0
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view:View = inflater!!.inflate(R.layout.choose_area,container,false)
        titleText = view.findViewById<View>(R.id.title_text) as TextView
        backButton = view.findViewById<View>(R.id.back_button) as Button
        listView = view.findViewById<View>(R.id.list_view) as ListView
        adapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,dataList)
        listView!!.adapter = adapter
        return view
    }


override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    listView!!.onItemClickListener = AdapterView.OnItemClickListener{ adapterView, view, position, id ->
        if (currentLevel == LEVEL_PROVINCE){
            selectedProvince = provinceList!![position]
            queryCities()
        }else if (currentLevel == LEVEL_CITY) {
            selectedCity = cityList!![position]
            queryCounties()
        }else if(currentLevel == LEVEL_COUNTY) {
            val weatherId = countyList!![position].countyName
                val intent = Intent(activity, WeatherActivity::class.java)
                intent.putExtra("county_name", weatherId)
                startActivity(intent)
                activity.finish()

        }
    }
    back_button.setOnClickListener {
        if (currentLevel == LEVEL_COUNTY){
            queryCities()
        }else if (currentLevel == LEVEL_CITY){
            queryProvinces()
        }

    }
    queryProvinces()

}
    fun queryProvinces(){
        titleText!!.text = "中国"
        back_button.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList!!.size>0){
            dataList.clear()
            for (province in provinceList!!) {
                dataList.add(province.provinceName!!)
            }
            adapter!!.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        }else{
            val address ="http://guolin.tech/api/china"
            queryFromServer(address,"province")
        }
    }
    fun queryCities(){
        titleText!!.text = selectedProvince?.provinceName
        back_button.visibility = View.VISIBLE
        cityList = DataSupport.where("provinceid=?", selectedProvince!!.id.toString()).find(City::class.java)
        if (cityList!!.size>0){
            dataList.clear()
            for (city in cityList!!){
                dataList.add(city.cityName!!)
            }
            adapter!!.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_CITY
        }else{
            val provinceCode = selectedProvince!!.provinceCode
            val address = "http://guolin.tech/api/china/$provinceCode"
            queryFromServer(address,"city")
        }
    }
    fun queryCounties(){
        titleText!!.text = selectedCity?.cityName
        back_button.visibility = View.VISIBLE
        countyList = DataSupport.where("cityid = ?", selectedCity!!.id.toString()).find(County::class.java)
        if (countyList!!.size>0){
            dataList.clear()
            for (county in countyList!!){
                dataList.add(county.countyName!!)
            }
            adapter?.notifyDataSetChanged()
            list_view.setSelection(0)
            currentLevel = LEVEL_COUNTY
        }else{
            val provinceCode = selectedProvince!!.provinceCode
            val cityCode = selectedCity?.cityCode
            val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
            queryFromServer(address,"county")

        }

    }
    private fun queryFromServer(address:String,type:String){
        showProgressDialog()
        HttpUtil.sendOkHttpRequest(address, object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(context,"加载失败",Toast.LENGTH_SHORT).show()
                }

            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseTest = response.body()!!.string()
                var result = false
                when(type){
                    "province" -> result = Utility.handleProvinceResponse(responseTest)
                    "city"     -> result = Utility.handleCityResponse(responseTest, selectedProvince!!.id!!)
                    "county"   -> result = Utility.handleCountyResponse(responseTest, selectedCity!!.id!!)
                }
                if (result){
                    activity.runOnUiThread {
                        closeProgressDialog()
                        when(type){
                            "province" -> queryProvinces()
                            "city"     -> queryCities()
                            "county"   -> queryCounties()
                        }

                    }
                }

            }
        })

    }
    private fun showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage("正在加载……")
            progressDialog!!.setCanceledOnTouchOutside(false)

        }
        progressDialog!!.show()
    }

    /**
     * 关闭进度对话框
     */
    private fun closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog!!.dismiss()
        }

    }



}