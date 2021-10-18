package com.restoo.machinetest.Adapter
import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.restoo.machinetest.Listner.ItemListner
import com.restoo.machinetest.ProductList
import com.restoo.machinetest.R
import kotlin.collections.HashMap


class ExpandableListAdapter internal constructor(private val _context: Context, private val _listDataHeader: List<String>,
                            private val _listDataChild: HashMap<String, List<ProductList>>,var listner : ItemListner) : BaseExpandableListAdapter(){


    override fun getChild(groupPosition: Int, childPosititon: Int): ProductList {
        return _listDataChild[_listDataHeader.get(groupPosition)]!!.get(childPosititon)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View? {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition)
        if (convertView == null) {
            val infalInflater = _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.layoutchild, null)
        }
        val txtListChild = convertView
            ?.findViewById<View>(R.id.childname) as TextView
        val imgListChild = convertView
            ?.findViewById<View>(R.id.imgproduct) as ImageView
        val iChildlayout = convertView
            ?.findViewById<View>(R.id.child_layout) as RelativeLayout
        Glide.with(_context).load(childText.imageUrl).into(imgListChild)
        txtListChild.text = childText.title
        txtListChild.setOnClickListener(View.OnClickListener {
            listner.onItemClick(groupPosition,childPosition)
        })
        imgListChild.setOnClickListener(View.OnClickListener {
            listner.onItemClick(groupPosition,childPosition)
        })
        iChildlayout.setOnClickListener(View.OnClickListener {
            listner.onItemClick(groupPosition,childPosition)
        })
        return convertView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return _listDataChild[_listDataHeader[groupPosition]]!!.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return _listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return _listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View? {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as String
        if (convertView == null) {
            val infalInflater = _context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.layoutparent, null)
        }
        val lblListHeader = convertView
            ?.findViewById<View>(R.id.parentname) as TextView
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle
        return convertView
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }
}