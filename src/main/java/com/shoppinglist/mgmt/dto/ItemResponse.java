package com.shoppinglist.mgmt.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response structure of the items")
public record ItemResponse(
		List<ItemResponseDto> items
		) {
}
