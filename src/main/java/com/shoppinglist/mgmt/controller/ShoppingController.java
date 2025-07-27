package com.shoppinglist.mgmt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ShoppingListRequestDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.dto.ShoppingListUpdateRequestDto;
import com.shoppinglist.mgmt.service.ShoppingListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 *  <p>This Controller is responsible for managing the shopping list in the system.</p>
 *  - It performs following operations:
 *  - Fetches active shopping list by item code
 *  - Creates new shopping list
 *  - Updates existing shopping list with new items
 */
@RestController
@RequestMapping(Constants.API_BASE_PATH)
public class ShoppingController {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingController.class);
	
	private ShoppingListService shoppingListService;

	public ShoppingController(ShoppingListService shoppingListService) {
		super();
		this.shoppingListService = shoppingListService;
	}
	
	@Operation(summary = "Creates a shopping list in the system", description = "Creates a shopping list with items in the system as per provided data")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully creates shopping list in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingListResponseDto.class))),
	      @ApiResponse(responseCode = "400", description = "Invalid request passed", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "500", description = "Error while saving the shoppinglist", content = @Content(mediaType = "application/json"))
	      })
	@PostMapping("shoppinglist")
	public ResponseEntity<ShoppingListResponseDto> createShoppingList(@Valid @RequestBody ShoppingListRequestDto request){
		logger.info("Creating a shopping list...");
		return ResponseEntity.ok(shoppingListService.createShoppingList(request));
		
	}
	
	@Operation(summary = "Fetch a shopping list from the system", description = "Gets a shopping list with items from the system as per provided code")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully fetches shopping list from the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingListResponseDto.class))),
	      @ApiResponse(responseCode = "204", description = "No item is available in the system", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json"))
	      })
	@GetMapping("shoppinglist/{code}")
	public ResponseEntity<ShoppingListResponseDto> getShoppingList(@PathVariable String code){
		logger.info("Fetch a shopping list...");
		return ResponseEntity.ok(shoppingListService.findByCode(code));
		
	}
	
	@Operation(summary = "Updates a shopping list with items in the system", description = "Updates a shopping list with items in the system as per provided data")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully updates shopping list in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShoppingListResponseDto.class))),
	      @ApiResponse(responseCode = "400", description = "Invalid request passed", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "500", description = "Error while updating the shoppinglist", content = @Content(mediaType = "application/json"))
	      })
	@PutMapping("shoppinglist")
	public ResponseEntity<ShoppingListResponseDto> updateShoppingList(@Valid @RequestBody ShoppingListUpdateRequestDto request){
		logger.info("Updating a shopping list...");
		return ResponseEntity.ok(shoppingListService.updateShoppingList(request));
		
	}
}
