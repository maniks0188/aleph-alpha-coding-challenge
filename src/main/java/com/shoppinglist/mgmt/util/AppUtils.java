package com.shoppinglist.mgmt.util;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.shoppinglist.mgmt.dto.ItemRequestDto;
import com.shoppinglist.mgmt.dto.ItemResponseDto;
import com.shoppinglist.mgmt.dto.ShopListItemDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.model.ShoppingList;
import com.shoppinglist.mgmt.model.ShoppingListItem;

@Component
public class AppUtils {

	public List<Item> dtoToEntity(List<ItemRequestDto> itemsFromRequest){
		
		return itemsFromRequest.stream().map(this::toEntity).toList();
		
	}
	
	private Item toEntity(ItemRequestDto dto) {
		return Item.builder()
				.itemName(dto.name())
				.itemPrice(dto.price())
				.itemCode(itemCode())
				.active(true)
				.deleted(false)
				.bought(false)
				.build();
	}
	
	private String itemCode() {
		return "item"+Instant.now().toEpochMilli();
	}
	
	public List<ItemResponseDto> entityToDtoItem(List<Item> dbItems){
		return dbItems.stream().map(this::toDto).toList();
	}
	
	public ItemResponseDto toDto(Item item) {
		return new ItemResponseDto.Builder()
				.itemName(item.getItemName())
				.itemPrice(item.getItemPrice())
				.itemCode(item.getItemCode())
				.deleted(item.isDeleted())
				.bought(item.isBought())
				.active(item.isActive()).build();
				
				
	}
	
	public String generateShopListCode() {
		return "shplst"+Instant.now().toEpochMilli();
	}
	
	public ShoppingListResponseDto entityToDtoShoppinglist(ShoppingList entity) {
		return new ShoppingListResponseDto.Builder()
				.name(entity.getName())
				.code(entity.getCode())
				.deleted(entity.isDeleted())
				.items(entity.getItems().stream().map(this::shoplistItemEntityToDto).toList())
				.build();
	}
	
	private ShopListItemDto shoplistItemEntityToDto(ShoppingListItem listItem) {
		return new ShopListItemDto(listItem.getItemCode(), listItem.getItemQuantity());
	}
}
