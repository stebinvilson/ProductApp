package com.restoo.machinetest.Listner

import com.restoo.machinetest.ResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Accept: application/json", "Content-Type: application/json;charset=UTF-8")
    @GET("5ec39cba300000720039c1f6")
    fun fetch(): Call<ResponseData?>
}