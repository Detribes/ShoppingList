package com.example.shoppinglist.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var _viewModel :MainViewModel
    private lateinit var _llShopeList :LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _llShopeList = findViewById(R.id.llShopList)
        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _viewModel.shopList.observe(this) {
            _showList(it)
        }
    }
    private fun _showList(list :List<ShopItem>) {
        _llShopeList.removeAllViews()

        for(shopItem in list){
            val layoutId = if(shopItem.active) {
                R.layout.item_shop_enabled
            } else {
                R.layout.item_shop_disabled
            }
            val view = LayoutInflater.from(this).inflate(layoutId, _llShopeList,false)
            val tvName = view.findViewById<TextView>(R.id.tvName)
            val tvCount = view.findViewById<TextView>(R.id.tvCount)

            tvName.text = shopItem.name
            tvCount.text = shopItem.count.toString()

            view.setOnLongClickListener {
                _viewModel.changeActiveState(shopItem)
                true //True variarty needs for correctly work of SOLCL. These may be False var
            }

            _llShopeList.addView(view)
        }
    }
}