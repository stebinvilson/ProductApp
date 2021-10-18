package com.restoo.machinetest.Listner

import com.restoo.machinetest.ResponseData

interface MainActivityListner {

    fun onsuccess(data :ResponseData, local : Boolean)
    fun onfail()
}