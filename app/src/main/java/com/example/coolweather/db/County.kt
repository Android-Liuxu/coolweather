package com.example.coolweather.db

import android.provider.ContactsContract
import org.litepal.crud.DataSupport

class County:DataSupport(){
    var id:Int?=null
    var countyName:String?=null
    var weatherId:Int?=null
    var cityId:Int?=null

}