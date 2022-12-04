package com.example.shoppinglist.data

import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

object ShopListRepositoryImpl : ShopListRepository{
    private val _shopList = mutableListOf<ShopItem>()
    private val _shopListLiveData = MutableLiveData<List<ShopItem>>()
    private var _autoIncrementedID = 0

    init{
        for(i in 0 until 10){
            val item = ShopItem("Name $i", i, true)
            addShopItem(item)
        }
    }

    override fun addShopItem(shopItem: ShopItem) {
        if(shopItem.id == ShopItem.UNDEFINED_ID) shopItem.id = _autoIncrementedID++
        shopItem.id = _autoIncrementedID++
        _shopList.add(shopItem)
        _updateList()
    }

    override fun deleteShopItem(shopItem: ShopItem) {
        _shopList.remove(shopItem)
        _updateList()
    }

    override fun getShopItem(shopItemId: Int): ShopItem {
        return _shopList.find { it.id == shopItemId } ?:
        throw RuntimeException("Element with $shopItemId ID not found.")
    }

    override fun updateShopItem(shopItem: ShopItem) {
        val oldElement = getShopItem(shopItem.id)
        _shopList.remove(oldElement)
        addShopItem(shopItem)
    }

    override fun getShopList(): MutableLiveData<List<ShopItem>> {
        return _shopListLiveData
    }
    private fun _updateList() {
        _shopListLiveData.value = _shopList.toList()
    }
}