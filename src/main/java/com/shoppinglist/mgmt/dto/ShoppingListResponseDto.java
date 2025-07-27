package com.shoppinglist.mgmt.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ShoppingListResponseDto(
		@Schema(description = "Shopping list name")
		String name,
		@Schema(description = "Shopping list name")
		String code,
		@Schema(description = "Shopping list status")
		boolean deleted,
		@Schema(description = "Items in the shopping list")
		List<ShopListItemDto> items
		) {

	public static class Builder{
		String name;
		String code;
		boolean deleted;
		List<ShopListItemDto> items;
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		public Builder code(String code) {
			this.code = code;
			return this;
		}
		public Builder deleted(boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		public Builder items(List<ShopListItemDto> items) {
			this.items = items;
			return this;
		}
		
		public ShoppingListResponseDto build() {
			return new ShoppingListResponseDto(name, code, deleted, items);
		}
	}
}
