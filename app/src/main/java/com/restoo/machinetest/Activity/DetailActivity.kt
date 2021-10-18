package com.restoo.machinetest.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.restoo.machinetest.ProductList
import com.restoo.machinetest.ResponseData
import com.restoo.machinetest.R

class DetailActivity : AppCompatActivity() {

    lateinit var mTxtTitle : TextView
    lateinit var mImgBack : ImageView
    lateinit var mTxtTopic : TextView
    lateinit var mTxtDate : TextView
    lateinit var mTxtDetail : TextView
    lateinit var mImgProduct : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.activity_apod_detail)
        setUi()
        setdata()
        setEventListner()
    }

    private fun setEventListner() {
        mImgBack.setOnClickListener(View.OnClickListener {
            intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        })
    }

    private fun setUi() {

        mTxtTitle = findViewById(R.id.txt_title)
        mImgBack = findViewById(R.id.img_back)
        mTxtTopic = findViewById(R.id.txt_detail_title)
        mTxtDate = findViewById(R.id.txt_detail_price)
        mTxtDetail = findViewById(R.id.txtdetail)
        mImgProduct = findViewById(R.id.img_detail)
    }

    private fun setdata() {

        mImgBack.visibility = View.VISIBLE
        var data = intent.getSerializableExtra(getString(R.string.productdata)) as ProductList
        mTxtTopic.text = data.title
        mTxtDate.text = data.price
        Glide.with(applicationContext).load(data.imageUrl).into(mImgProduct)
        mTxtDetail.setText(
            Html.fromHtml(
                Html.fromHtml(data.description).toString()
            )
        )
    }
}