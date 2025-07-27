package com.shoppinglist.mgmt.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ItemResponseDto(
		@Schema(description = "Saved item name")
		String itemName,
		@Schema(description = "Saved item price")
		Double itemPrice,
		@Schema(description = "Saved item unique code")
		String itemCode,
		@Schema(description = "Saved item status(deleted)",defaultValue = "false")
		boolean deleted,
		@Schema(description = "Saved item status", defaultValue = "true")
		boolean active,
		@Schema(description = "Saved item status(bought)", defaultValue = "false")
		boolean bought
		) {
	
	public static class Builder{
		String itemName;
		Double itemPrice;
		String itemCode;
		boolean deleted;
		boolean active;
		boolean bought;
		
		public Builder itemName(String name) {
			this.itemName = name;
			return this;
		}
		public Builder itemPrice(Double price) {
			this.itemPrice = price;
			return this;
		}
		public Builder itemCode(String code) {
			this.itemCode = code;
			return this;
		}
		public Builder deleted(boolean deleted) {
			this.deleted = deleted;
			return this;
		}
		public Builder active(boolean active) {
			this.active = active;
			return this;
		}
		public Builder bought(boolean bought) {
			this.bought = bought;
			return this;
		}
		
		public ItemResponseDto build() {
			return new ItemResponseDto(itemName, itemPrice, itemCode, deleted, active,bought);
		}
	}
}
