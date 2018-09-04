package com.example.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2018/7/4/004.
 */
//LitePal中每个实体类都要继承DataSupport
class Province : DataSupport() {
    //id每个实体类中都应该有的字段
    var id: Int = 0
    //provinceName 记录省的名字
    var provinceName: String? = null
    //provinceCode 记录省的代号
    var provinceCode: Int = 0


}
