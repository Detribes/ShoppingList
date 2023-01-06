package com.example.shoppinglist.presenter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemFragment() : Fragment() {
    private lateinit var _onEditingFinishedListener: OnEditingFinishedListener

    private lateinit var _viewModel: ShopItemViewModel
    private lateinit var _tilName: TextInputLayout
    private lateinit var _tilCount: TextInputLayout
    private lateinit var _etName: EditText
    private lateinit var _etCount: EditText
    private lateinit var _buttonSave: Button

    private var _screenMode: String = UNDEFINED_MODE
    private var _shopItemId: Int = ShopItem.UNDEFINED_ID

    /*
    the fragment is attached to the activity, then the view is created from the layout.
    The moment when the view will be created is described in onViewCreated
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener){
            _onEditingFinishedListener = context
        } else{
            throw RuntimeException("Activity must implement EditingFinishedListener")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        _initViews(view)
        _addTextChangeListeners()
        _launchRightMode()
        _observeViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _parseParams()
    }

    private fun _observeViewModel(){
        _viewModel.errorInputData.observe(viewLifecycleOwner){
            val message = if(it){
                getString(R.string.error_input_data)
            } else {
                null
            }
            _tilName.error = message
        }
        _viewModel.shouldCloseScreen.observe(viewLifecycleOwner){
            _onEditingFinishedListener.onEditingFinished() // same action as click on back button
        }
    }

    private fun _launchRightMode(){
        when(_screenMode){
            MODE_EDIT -> _launchEditMode()
            MODE_ADD -> _launchAddMode()
        }
    }

    private fun _addTextChangeListeners(){
        _etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _viewModel.resetErrorInputData()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        _etCount.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _viewModel.resetErrorInputData()
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }
    private fun _launchEditMode(){
        _viewModel.getShopItem(_shopItemId)
        _viewModel.shopItem.observe(viewLifecycleOwner){
            _etName.setText(it.name)
            _etCount.setText(it.count.toString())
        }
        _buttonSave.setOnClickListener{
            _viewModel.updateShopItem(_etName.text?.toString(),_etCount.text?.toString())
        }
    }

    private fun _launchAddMode(){
        _buttonSave.setOnClickListener{
            _viewModel.addShopItem(_etName.text?.toString(), _etCount.text?.toString())
        }
    }
    private fun _parseParams(){
        val args = requireArguments()
        if(!args.containsKey(SCREEN_MODE)){
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if(mode != MODE_ADD && mode != MODE_EDIT){
            throw RuntimeException("Unknown screen mode param $mode")
        }
        _screenMode = mode
        if(_screenMode == MODE_EDIT){
            if(!args.containsKey(SHOP_ITEM_ID)){
                throw RuntimeException("Param Shop Item Id is absent")
            }
            _shopItemId = args.getInt(SHOP_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun _initViews(view: View){
        _tilName = view.findViewById(R.id.tilName)
        _tilCount = view.findViewById(R.id.tilCount)
        _etName = view.findViewById(R.id.etName)
        _etCount = view.findViewById(R.id.etCount)
        _buttonSave = view.findViewById(R.id.saveButton)

    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }
    companion object{
        private const val SCREEN_MODE = "extra_mode"
        private const val SHOP_ITEM_ID = "extra_shop_item_id"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_ADD = "mode_add"
        private const val UNDEFINED_MODE = ""

        /*Fabric methods of create Intents for adding or editing elements*/
//        fun newIntentAddItem(context: Context): Intent {
//            val intent = Intent(context, ShopItemActivity::class.java)
//            intent.putExtra(SCREEN_MODE, MODE_ADD)
//            return intent
//        }
//        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
//            val intent = Intent(context, ShopItemActivity::class.java)
//            intent.putExtra(SCREEN_MODE, MODE_EDIT)
//            intent.putExtra(SHOP_ITEM_ID, shopItemId)
//            return intent
//        }
        fun newInstanceAddItem(): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_ADD)
                }
            }
        }
        fun newInstanceEditItem(shopItemId: Int): ShopItemFragment{
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, MODE_EDIT)
                    putInt(SHOP_ITEM_ID,shopItemId)
                }
            }
        }
    }
}