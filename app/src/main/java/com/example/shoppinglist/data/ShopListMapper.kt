package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem

class ShopListMapper {
    fun mapEntityToDbModel(shopItem: ShopItem) : ShopItemDbModel{
        return ShopItemDbModel(
            id=shopItem.id,
            name=shopItem.name,
            count=shopItem.count,
            active=shopItem.active
        )
    }
    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel): ShopItem{
        return ShopItem(
            id=shopItemDbModel.id,
            name=shopItemDbModel.name,
            count=shopItemDbModel.count,
            active=shopItemDbModel.active
        )
    }
    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>): List<ShopItem>{
        return list.map {
            mapDbModelToEntity(it)
        }
    }
}