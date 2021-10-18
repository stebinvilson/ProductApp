package com.restoo.machinetest.Helper

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.restoo.machinetest.*
import com.restoo.machinetest.Listner.ApiInterface
import com.restoo.machinetest.Listner.MainActivityListner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityHelper(
    var context: Context,
    var activity: Activity,
    var listner: MainActivityListner
) {

    protected var dialog: Dialog? = null
    private val dialogLock = Any()


    fun callApi(state : Boolean) {
        if (state) {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    isNetworkConnected()
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            ) {
                showProgress()
                val service: ApiInterface? = getRetrofitObject()?.create(ApiInterface::class.java)
                var call: Call<ResponseData?>? = service?.fetch()
                call?.enqueue(object : Callback<ResponseData?> {
                    override fun onResponse(
                        call: Call<ResponseData?>,
                        response: Response<ResponseData?>
                    ) {
                        //  closeProgress()
                        if (response.isSuccessful() && response.body() != null) {
                            var responsedata: ResponseData = response.body()!!
                            listner.onsuccess(responsedata, false)
                            closeProgress()

                        } else {
                            closeProgress()
                            listner.onfail()

                        }
                    }

                    override fun onFailure(call: Call<ResponseData?>, t: Throwable) {
                        listner.onfail()
                        Log.d(ContentValues.TAG, t.toString())
                        closeProgress()
                    }
                })
            }else {
                fetchdata(context, listner).execute()
            }
        } else {
            fetchdata(context, listner).execute()
        }
    }

    class fetchdata(var context: Context, var listner: MainActivityListner) :
        AsyncTask<Void, Void, List<ProductData>>() {


        override fun doInBackground(vararg params: Void?): List<ProductData>? {
            var mDb: AppDatabase =
                Room.databaseBuilder(context, AppDatabase::class.java, "RoomDatabase")
                    .allowMainThreadQueries().build()
            var fetchdata = mDb.datadao().getAll() as List<ProductData>

            return fetchdata
        }

        override fun onPostExecute(result: List<ProductData>?) {
            super.onPostExecute(result)
            if (result != null) {
                var categorydata : ArrayList<CategoriesData> = ArrayList()
                var productlist1 : ArrayList<ProductList>  = ArrayList()
                var productlist2 : ArrayList<ProductList>  = ArrayList()
                var productlist3 : ArrayList<ProductList>  = ArrayList()
                var productlist4 : ArrayList<ProductList>  = ArrayList()
                var productlist5 : ArrayList<ProductList>  = ArrayList()
                var productlist6 : ArrayList<ProductList>  = ArrayList()

                var rommdata: ArrayList<ResponseData> = ArrayList()
                for (items in result) {
                    if (items.product.equals("Category 1")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist1.add(product)
                    } else if (items.product.equals("Category 2")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist2.add(product)
                    }else if (items.product.equals("Category 3")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist3.add(product)
                    }else if (items.product.equals("Category 4")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist4.add(product)
                    }else if (items.product.equals("Category 5")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist5.add(product)
                    }else if (items.product.equals("Category 6")) {
                        var product : ProductList = ProductList(items.title,items.price,items.imageurl,items.description)
                        productlist6.add(product)
                    }
                }
                categorydata.add(CategoriesData("Category 1",productlist1))
                categorydata.add(CategoriesData("Category 2",productlist2))
                categorydata.add(CategoriesData("Category 3",productlist3))
                categorydata.add(CategoriesData("Category 4",productlist4))
                categorydata.add(CategoriesData("Category 5",productlist5))
                categorydata.add(CategoriesData("Category 6",productlist6))

                var resdata : ResponseData = ResponseData("","",categorydata)
                listner.onsuccess(resdata, true)
            }
        }
    }


    fun getRetrofitObject(): Retrofit? {
        val url: String = "http://www.mocky.io/v2/"
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork

        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun showProgress() {
        synchronized(dialogLock) {
            if ((dialog == null || !dialog!!.isShowing())
                && !activity.isFinishing()
            ) {
                dialog = Dialog(activity)
                dialog!!.setContentView(R.layout.layout_progress)
                dialog!!.setCancelable(false)
                val dialogWinfow: Window? = dialog!!.getWindow()
                val WMLP = dialogWinfow?.attributes
                WMLP?.gravity = Gravity.CENTER
                WMLP?.width = WindowManager.LayoutParams.WRAP_CONTENT
                dialog!!.show()
            }
        }
    }

    fun closeProgress() {
        synchronized(dialogLock) {
            dialog!!.dismiss()
        }
    }
}