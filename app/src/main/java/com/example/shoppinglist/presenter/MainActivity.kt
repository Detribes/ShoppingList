package com.example.shoppinglist.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.databinding.ActivityMainBinding
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {

    private lateinit var _viewModel: MainViewModel
    private lateinit var _adapter: ShopListAdapter
//    private var _shopItemContainer: FragmentContainerView? = null
    private lateinit var _binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        _setupRecyclerView()
        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _viewModel.shopList.observe(this) {
            _adapter.submitList(it)
        }
        _binding.buttonAddShopItem.setOnClickListener{
            if(_isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                _launchFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    override fun onEditingFinished(){
        Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }

    private fun _isOnePaneMode(): Boolean{
        return _binding.shopItemContainer == null
    }

    private fun _launchFragment(fragment: Fragment){
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .add(R.id.shopItemContainer, fragment)
            .commit()
    }

    /*Setup RV params to use*/
    private fun _setupRecyclerView() {
        _adapter = ShopListAdapter()
        with(_binding.rvShopList) {
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
        _setupOnLongClickListener()
        _setupOnClickListener()
        _setupOnSwipeListener(_binding.rvShopList)
    }

    private fun _setupOnSwipeListener(rvShopList: RecyclerView) {
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

    private fun _setupOnClickListener() {
        _adapter.onShopItemClickListener = object : ShopListAdapter.OnShopItemClickListener {
            override fun onShopItemClickListener(shopItem: ShopItem) {
                Log.d("ObjectParams", "count: ${shopItem.count} state: ${shopItem.active}")
                if(_isOnePaneMode()) {
                    val intent = ShopItemActivity.newIntentEditItem(this@MainActivity, shopItem.id)
                    startActivity(intent)
                } else {
                    _launchFragment(ShopItemFragment.newInstanceEditItem(shopItem.id))
                }
            }
        }
    }

    private fun _setupOnLongClickListener() {
        _adapter.onShopItemLongClickListener =
            object : ShopListAdapter.OnShopItemLongClickListener {
                override fun onShopItemLongClickListener(shopItem: ShopItem) {
                    _viewModel.changeActiveState(shopItem)
                }
            }
    }
}