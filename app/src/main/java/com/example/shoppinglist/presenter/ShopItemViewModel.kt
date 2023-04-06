package com.example.shoppinglist.presenter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddShopItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.UpdateShopItemUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.lang.Exception

class ShopItemViewModel(application: Application) : AndroidViewModel(application) {
    private val _repository = ShopListRepositoryImpl(application)
    private val _getShopItemUseCase = GetShopItemUseCase(_repository)
    private val _updateShopItemUseCase = UpdateShopItemUseCase(_repository)
    private val _addShopItemUseCase = AddShopItemUseCase(_repository)

    private val _errorInputData = MutableLiveData<Boolean>()
    val errorInputData: LiveData<Boolean>
        get() = _errorInputData

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    private fun _parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun _parseCount(inputCount: String?): Int {
        return try {
            inputCount?.trim()?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun _validateInput(name: String, count: Int): Boolean {
        var res = true
        if (name.isBlank() || count <= 0) {
            _errorInputData.postValue(true)
            res = false
        }
        return res
    }

    fun resetErrorInputData(){
        _errorInputData.postValue(false)
    }

    fun updateShopItem(inputName: String?, inputCount: String?) {
        viewModelScope.launch {
            val name = _parseName(inputName)
            val count = _parseCount(inputCount)
            val fieldsValid = _validateInput (name, count)
            if (fieldsValid) {
                _shopItem.value?.let {
                    val item = it.copy(name = name, count = count)
                    _updateShopItemUseCase.updateShopItem(item)
                    _shouldCloseScreen.postValue(Unit)
                }
            } }

    }

    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            val item = _getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.postValue(item)
        }

    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        viewModelScope.launch{
            val name = _parseName(inputName)
            val count = _parseCount(inputCount)
            val fieldsValid = _validateInput(name, count)
            if (fieldsValid) {
                val shopItem = ShopItem(name, count, true)
                _addShopItemUseCase.addShopItem(shopItem)
                _shouldCloseScreen.postValue(Unit)
            }
        }

    }


}