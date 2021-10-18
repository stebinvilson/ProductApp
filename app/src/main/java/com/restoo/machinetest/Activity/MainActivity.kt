package com.restoo.machinetest.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ExpandableListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.restoo.machinetest.*

import com.restoo.machinetest.Adapter.ExpandableListAdapter

import com.restoo.machinetest.Helper.MainActivityHelper
import com.restoo.machinetest.Listner.ItemListner
import com.restoo.machinetest.Listner.MainActivityListner
import java.util.ArrayList

class MainActivity : AppCompatActivity(), MainActivityListner, ItemListner {
    lateinit var mRvApodList: ExpandableListView
    lateinit var mAdapter: ExpandableListAdapter
    lateinit var mTxtTitle: TextView
    lateinit var helper: MainActivityHelper
    lateinit var mDb: AppDatabase
    lateinit var activity: Activity
    lateinit var mTxtNoData: TextView
    lateinit var mCategories : List<CategoriesData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        helper = MainActivityHelper(applicationContext, this, this)
        firsttime()
        setUI()
        setData()
    }

    private fun firsttime() {
        var PREFS_NAME : String = "MyPrefsFile";
        val settings: SharedPreferences = getSharedPreferences(PREFS_NAME, 0)

        if (settings.getBoolean("my_first_time", true)) {
            helper.callApi(true)
            settings.edit().putBoolean("my_first_time", false).commit()
        } else {
            helper.callApi(false)
        }
    }


    private fun setUI() {
        mTxtTitle = findViewById(R.id.txt_title)
        mRvApodList = findViewById(R.id.rv_product)
        mTxtNoData = findViewById(R.id.no_data)
    }


    private fun setData() {

        mTxtTitle.setText(getString(R.string.title_listing))


        mDb = Room.databaseBuilder(this, AppDatabase::class.java, "RoomDatabase")
            .allowMainThreadQueries().build()
        mDb = AppDatabase.getInstance(applicationContext)
    }


    override fun onsuccess(responsedata: ResponseData, local: Boolean) {
        var parentdata : ArrayList<String> = ArrayList()
        var childdata : HashMap<String,List<ProductList>> = HashMap()
        if (responsedata != null) {
            for (item in responsedata.categories) {
               parentdata.add(item.title)

                childdata.put(item.title,item.products)
            }
            mCategories = ArrayList()
            mCategories = responsedata.categories


            var adapter1 = ExpandableListAdapter(this, parentdata,childdata,this)
            mRvApodList.setAdapter(adapter1)
        }
            if (!local) {
                doAsync(responsedata, applicationContext).execute()
            }
//            mTxtNoData.visibility = View.GONE
//        } else {
//
//            mTxtNoData.visibility = View.VISIBLE
//        }
    }


    class doAsync(var response: ResponseData, var con: Context) : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg params: Void?): Boolean? {
            var mDb: AppDatabase =
                Room.databaseBuilder(con, AppDatabase::class.java, "RoomDatabase")
                    .allowMainThreadQueries().build()

            var list :List<CategoriesData> = ArrayList()
            list = response.categories

            for (items in list) {
                if (items.products != null ) {
                    for (item in items.products) {
                        var data = ProductData(
                            id = null,
                            item.title,
                            item.description,
                            item.price,
                            item.imageUrl,
                            items.title
                        )
                        mDb.datadao().insert(data)
                    }
                }
            }
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
        }
    }

    override fun onfail() {

    }

    override fun onItemClick(group: Int, child : Int) {

        var product : ProductList = mCategories.get(group).products.get(child)

        var intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(getString(R.string.productdata), product)
        startActivity(intent)
    }
}
