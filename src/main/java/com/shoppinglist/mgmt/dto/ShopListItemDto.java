package com.shoppinglist.mgmt.dto;

import com.shoppinglist.mgmt.constants.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShopListItemDto(
		@NotBlank(message = Constants.INVALID_ITEM_CODE) 
		@Schema(description = "Item code")
		String itemCode,
		@NotNull(message = Constants.INVALID_ITEM_QUANTITY) 
		@Schema(description = "Item quantity", defaultValue = "0")
		int itemQuantity
		) {

}
