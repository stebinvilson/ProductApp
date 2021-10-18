package com.restoo.machinetest.Listner

import com.restoo.machinetest.ResponseData

interface ItemListner {

    fun onItemClick(group : Int,child :Int)
}