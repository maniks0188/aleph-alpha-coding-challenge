package com.shoppinglist.mgmt.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.service.RecommendationService;
import com.shoppinglist.mgmt.suggestionengine.RecommendationStrategyType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * 
 * <p>This controller is responsible for providing suggestions for:</p>
 *  - Items(by passing first 3 characters).
 *  - Additional items suggestion based upon already existing items in the shopping list.
 *
 */
@RestController
@RequestMapping(Constants.API_BASE_PATH + "suggest/")
public class RecommendationController {

	private static final Logger logger = LoggerFactory.getLogger(RecommendationController.class);
	
	private RecommendationService recommendationService;

	public RecommendationController(RecommendationService recommendationService) {
		super();
		this.recommendationService = recommendationService;
	}
	
	@Operation(summary = "Returns list of items matching the prefix of the item name from the system", description = "")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully returns list of items from the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "204", description = "No matching items are available in the system", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json"))})
	@GetMapping("items")
	public ResponseEntity<ItemResponse> suggestItemsStartsWith(@RequestParam("startsWith") String prefix,
			@RequestParam(value =  "limit", defaultValue = "10") int limit){
		
		logger.info("Getting items suggestion that starts with {}", prefix);
		ItemResponse response = recommendationService.findItemsStartingWith(prefix,limit);
		if(!response.items().isEmpty()) {
			return ResponseEntity.ok(response);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
	
	@Operation(summary = "Returns additional suggestions of items based on existing items in the cart from the system", description = "")
	@ApiResponses(value = {
	      @ApiResponse(responseCode = "200", description = "Successfully returns list of items from the system", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemResponse.class))),
	      @ApiResponse(responseCode = "204", description = "No matching items are available in the system", content = @Content(mediaType = "application/json")),
	      @ApiResponse(responseCode = "401", description = "User not authorized", content = @Content(mediaType = "application/json"))})
	@GetMapping("shoppinglist/addons")
	public ResponseEntity<ItemResponse> suggestShoppingListAddOns(@RequestParam("shoplistid") String shoplistid,
			@RequestParam(value = "engine", defaultValue = "BASIC") RecommendationStrategyType strategy,
			@RequestParam(value =  "limit", defaultValue = "10") int limit){
		
		logger.info("Getting additional items suggestion for the shopping list");
		ItemResponse response = recommendationService.findSuggestionsForShoppingList(shoplistid,strategy);
		if(!response.items().isEmpty()) {
			return ResponseEntity.ok(response);
		}else {
			return ResponseEntity.noContent().build();
		}
	}
}
