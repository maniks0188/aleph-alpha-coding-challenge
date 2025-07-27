package com.shoppinglist.mgmt.dto;

import java.util.List;

import com.shoppinglist.mgmt.constants.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record ShoppingListUpdateRequestDto(
		@NotBlank(message = Constants.INVALID_SHOPLIST_CODE) 
		@Schema(description = "Shopping list code")
		String code,
		@Valid
		@Schema(description = "Items for the shopping list")
		List<ShopListItemDto> items	
		) {
	
}
