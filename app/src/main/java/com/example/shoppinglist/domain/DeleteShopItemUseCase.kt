package com.example.shoppinglist.domain

class DeleteShopItemUseCase(private val _shopListRepository: ShopListRepository) {
    suspend fun deleteShopItem(shopItem: ShopItem) {
        _shopListRepository.deleteShopItem(shopItem)
    }
}