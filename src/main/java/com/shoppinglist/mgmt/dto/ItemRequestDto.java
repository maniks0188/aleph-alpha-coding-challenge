package com.shoppinglist.mgmt.dto;

import com.shoppinglist.mgmt.constants.Constants;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record ItemRequestDto(
		@NotBlank(message = Constants.INVALID_ITEM_NAME) 
		@Schema(description = "Item name")
		String name,
		@NotNull(message = Constants.INVALID_ITEM_PRICE) 
		@Schema(description = "Item price", defaultValue = "0.0")
		Double price	
		) {
}
