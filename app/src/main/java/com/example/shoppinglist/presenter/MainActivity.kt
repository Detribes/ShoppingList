package com.example.shoppinglist.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var _viewModel: MainViewModel
    private lateinit var _adapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _setupRecyclerView()
        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _viewModel.shopList.observe(this) {
            _adapter.submitList(it)
        }
    }

    /*Setup RV params to use*/
    private fun _setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rvShopList)
        _adapter = ShopListAdapter()
        with(rvShopList) {
            adapter = _adapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_ENABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.VIEW_TYPE_DISABLED,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupOnLongClickListener()
        setupOnClickListener()
        setupOnSwipeListener(rvShopList)
    }

    private fun setupOnSwipeListener(rvShopList: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = _adapter.currentList[viewHolder.adapterPosition]
                _viewModel.deleteShopItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupOnClickListener() {
        _adapter.onShopItemClickListener = object : ShopListAdapter.OnShopItemClickListener {
            override fun onShopItemClickListener(shopItem: ShopItem) {
                Log.d("ObjectParams", "count: ${shopItem.count} state: ${shopItem.active}")
            }
        }
    }

    private fun setupOnLongClickListener() {
        _adapter.onShopItemLongClickListener =
            object : ShopListAdapter.OnShopItemLongClickListener {
                override fun onShopItemLongClickListener(shopItem: ShopItem) {
                    _viewModel.changeActiveState(shopItem)
                }
            }
    }
}