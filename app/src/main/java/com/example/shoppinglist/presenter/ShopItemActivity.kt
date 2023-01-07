package com.example.shoppinglist.presenter

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity(), ShopItemFragment.OnEditingFinishedListener {
//    private lateinit var _viewModel: ShopItemViewModel
//    private lateinit var _tilName: TextInputLayout
//    private lateinit var _tilCount: TextInputLayout
//    private lateinit var _etName: EditText
//    private lateinit var _etCount: EditText
//    private lateinit var _buttonSave:Button

    private var _screenMode = UNDEFINED_MODE
    private var _shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        _parseIntent()
        if(savedInstanceState == null){
            _launchRightMode()
        }
    }
//
//    private fun _observeViewModel(){
//        _viewModel.errorInputData.observe(this){
//            val message = if(it){
//                getString(R.string.error_input_data)
//            } else {
//                null
//            }
//            _tilName.error = message
//        }
//        _viewModel.shouldCloseScreen.observe(this){
//            finish()
//        }
//    }
//
    private fun _launchRightMode(){
    val fragment = when(_screenMode){
            MODE_EDIT -> ShopItemFragment.newInstanceEditItem(_shopItemId)
            MODE_ADD -> ShopItemFragment.newInstanceAddItem()
        else -> throw RuntimeException("Unknown screen mode $_screenMode")
        }
    supportFragmentManager.beginTransaction()
        .replace(R.id.shop_item_container, fragment)
        .commit()
    }
//
//    private fun _addTextChangeListeners(){
//        _etName.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                _viewModel.resetErrorInputData()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })
//        _etCount.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                _viewModel.resetErrorInputData()
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//
//            }
//
//        })
//    }
//    private fun _launchEditMode(){
//        _viewModel.getShopItem(_shopItemId)
//        _viewModel.shopItem.observe(this){
//            _etName.setText(it.name)
//            _etCount.setText(it.count.toString())
//        }
//        _buttonSave.setOnClickListener{
//            _viewModel.updateShopItem(_etName.text?.toString(),_etCount.text?.toString())
//        }
//    }
//
//    private fun _launchAddMode(){
//        _buttonSave.setOnClickListener{
//            _viewModel.addShopItem(_etName.text?.toString(), _etCount.text?.toString())
//        }
//    }
    private fun _parseIntent(){
        if(!intent.hasExtra(EXTRA_SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if(mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Unknown screen mode param $mode")
        }
        _screenMode = mode
        if(_screenMode == MODE_EDIT){
            if(!intent.hasExtra(EXTRA_SHOP_ITEM_ID)){
                throw RuntimeException("Param Shop Item Id is absent")
            }
           _shopItemId = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

//
//    private fun _initViews(){
//        _tilName = findViewById(R.id.tilName)
//        _tilCount = findViewById(R.id.tilCount)
//        _etName = findViewById(R.id.etName)
//        _etCount = findViewById(R.id.etCount)
//        _buttonSave = findViewById(R.id.saveButton)
//
//    }
    companion object{
        private const val EXTRA_SCREEN_MODE = "extra_mode"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val UNDEFINED_MODE = ""

        /*Fabric methods of create Intents for adding or editing elements*/
        fun newIntentAddItem(context: Context): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }
        fun newIntentEditItem(context: Context, shopItemId: Int): Intent{
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }

    override fun onEditingFinished() {
        finish()
    }
}