package com.example.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2018/7/4/004.
 */

class County : DataSupport() {
    //id每个实体类中都应该有的字段
    var id: Int = 0
    //countyName 记录县的名字
    var countyName: String? = null
    //weatherId 记录天气的id
    var weatherId: String? = null
    //cityId 记录市的代码
    var cityId: Int = 0
}
