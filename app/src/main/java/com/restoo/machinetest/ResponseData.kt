package com.restoo.machinetest

import java.io.Serializable

class ResponseData(var status : String = "", var msg : String = "", var categories : List<CategoriesData> ) : Serializable {
}