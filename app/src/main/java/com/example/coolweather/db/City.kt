package com.example.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2018/7/4/004.
 */
//LitePal中每个实体类都要继承DataSupport
class City : DataSupport() {
    //id每个实体类中都应该有的字段
    var id: Int = 0
    //cityName 记录市的名字
    var cityName: String? = null
    //cityName 记录市的代码
    var cityCode: Int = 0
    //provinceId 记录省的代码
    var provinceId: Int = 0
}
