package com.shoppinglist.mgmt.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.ShoppingList;
import com.shoppinglist.mgmt.model.ShoppingListItem;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.repository.ShoppingListRepository;
import com.shoppinglist.mgmt.suggestionengine.RecommendationEngineContext;
import com.shoppinglist.mgmt.suggestionengine.RecommendationStrategyType;
import com.shoppinglist.mgmt.util.AppUtils;

/**
 * 
 * This Service class acts as the central implementation for different types of recommendation
 * engine strategies(BASIC,RULE_BASED,LLM)
 *
 */
@Service
public class RecommendationService {
	
	private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

	private ItemRepository itemRepository;
	
	private ShoppingListRepository shoppingListRepository;
	
	private AppUtils utils;
	
	private RecommendationEngineContext recommendationEngine;
	
	/**
	 * @param itemRepository
	 * @param shoppingListRepository
	 * @param utils
	 * @param recommendationEngine
	 */
	public RecommendationService(ItemRepository itemRepository, ShoppingListRepository shoppingListRepository, AppUtils utils, RecommendationEngineContext recommendationEngine) {
		super();
		this.itemRepository = itemRepository;
		this.shoppingListRepository = shoppingListRepository;
		this.utils = utils;
		this.recommendationEngine = recommendationEngine;
	}
	
	/**
	 * @param prefix
	 * @param limit
	 * @return
	 */
	public ItemResponse findItemsStartingWith(String prefix,int limit) {
		logger.info("Suggesting items that starts with {}", prefix);
		if(prefix == null || prefix.length()<3) {
			throw new CustomApplicationException(Constants.INVALID_PREFIX);
		}
		Pageable page = PageRequest.of(0, limit);
		return new ItemResponse(utils.entityToDtoItem(itemRepository.findItemsByNameStartingWith(prefix.trim().toLowerCase(),page)));
	}
	
	/**
	 * @param code
	 * @param strategy
	 * @return
	 */
	public ItemResponse findSuggestionsForShoppingList(String code, RecommendationStrategyType strategy) {
		logger.info("Suggesting items for the existing shopping list");
		Optional<ShoppingList> optional = shoppingListRepository.findByCode(code);
		if(optional.isPresent()) {
			ShoppingList shopList = optional.get();
			List<String> existingItems = shopList.getItems().stream().map(ShoppingListItem::getItemCode)
					.collect(Collectors.toList());
			
			return new ItemResponse(utils.entityToDtoItem(itemRepository.findByItemCodeIn(recommendationEngine.recommend(existingItems,strategy))));
		}else {
			throw new CustomApplicationException(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
		}
	}
}
