package com.example.shoppinglist.presenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteShopItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.UpdateShopItemUseCase

class MainViewModel : ViewModel() {

    private val _repository = ShopListRepositoryImpl

    private val _getShopListUseCase = GetShopListUseCase(_repository)
    private val _deleteShopItemUseCase = DeleteShopItemUseCase(_repository)
    private val _updateShopItemUseCase = UpdateShopItemUseCase(_repository)

    val shopList = _getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem){
        _deleteShopItemUseCase.deleteShopItem(shopItem)
    }
    fun changeActiveState(shopItem: ShopItem){
        val newShopItem = shopItem.copy(active = !shopItem.active)
        _updateShopItemUseCase.updateShopItem(newShopItem)
    }
}