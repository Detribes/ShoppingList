package com.example.shoppinglist.presenter

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.UpdateShopItemUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _repository = ShopListRepositoryImpl(application)

    private val _getShopListUseCase = GetShopListUseCase(_repository)
    private val _deleteShopItemUseCase = DeleteShopItemUseCase(_repository)
    private val _updateShopItemUseCase = UpdateShopItemUseCase(_repository)

    val shopList = _getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        viewModelScope.launch {
            _deleteShopItemUseCase.deleteShopItem(shopItem)
        }

    }
    fun changeActiveState(shopItem: ShopItem){
        viewModelScope.launch {
            val newShopItem = shopItem.copy(active = !shopItem.active)
            _updateShopItemUseCase.updateShopItem(newShopItem)
        }

    }

}