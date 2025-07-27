package com.shoppinglist.mgmt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemRequest;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * 
 * <p>Item Controller is used to manage the Items in the system.<p>
 * - It performs following operations:
 * - Fetches all the items from DB
 * - Fetches active items by item code
 * - Creates new items
 *
 */
@RestController
@RequestMapping(Constants.API_BASE_PATH)
public class ItemController {

	private static final Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	private ItemService itemService;
	
	@Autowired
	public ItemController(ItemService itemService) {
		this.itemService = itemService;
	}

	@Operation(summary = "Returns all the items from the system", description = "")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully returns items from the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "204", description = "No item is available in the system", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json"))})
	@GetMapping("items")
	public ResponseEntity<ItemResponse> getAllItems(){
		logger.info("Fetching all the items...");
		return ResponseEntity.ok(itemService.findAll());
	}
	
	@Operation(summary = "Return the item by code from the system", description = "")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully returns item from the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "204", description = "No item is available in the system", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json"))})
	@GetMapping("items/{code}")
	public ResponseEntity<ItemResponse> findItemByCode(@PathVariable String code){
		logger.info("Fetching the items by code...");
		return ResponseEntity.ok(itemService.getItemByCode(code));
	}
	
	@Operation(summary = "Creates items in the system", description = "Creates one or more items in the system as per provided data")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully creates items in the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "400", description = "Invalid request passed", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "500", description = "Error while saving the items", content = @Content(mediaType = "application/json"))
	      })
	@PostMapping("items")
	public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest request) {
		 logger.info("Creating the items in system...");
		 ItemResponse response = itemService.saveItems(request);
		 return ResponseEntity.ok(response);
	}
}
