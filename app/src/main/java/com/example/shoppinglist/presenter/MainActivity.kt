package com.example.shoppinglist.presenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R

class MainActivity : AppCompatActivity() {

    private lateinit var _viewModel: MainViewModel

    private var _count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        _viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        _viewModel.shopList.observe(this) {
            Log.d("Main Activity Test", it.toString())
            if (_count == 0) {
                _count++
                val item = it[0]
                _viewModel.changeActiveState(item)
            }
        }
    }
}