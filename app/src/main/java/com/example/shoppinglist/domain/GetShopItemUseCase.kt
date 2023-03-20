package com.example.shoppinglist.domain

class GetShopItemUseCase(private val _shopListRepository: ShopListRepository) {
    suspend fun getShopItem(shopItemId: Int) :ShopItem {
        return _shopListRepository.getShopItem(shopItemId)
    }
}