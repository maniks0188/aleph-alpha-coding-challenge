package com.shoppinglist.mgmt.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@Schema(description = "Request structure to create an item")
public record ItemRequest(
		@Valid
		@Schema(description = "Items to be saved in the system.")
		List<ItemRequestDto> items) {
}
