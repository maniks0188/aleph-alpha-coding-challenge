package com.shoppinglist.mgmt.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ShopListItemDto;
import com.shoppinglist.mgmt.dto.ShoppingListRequestDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.dto.ShoppingListUpdateRequestDto;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.model.ShoppingList;
import com.shoppinglist.mgmt.model.ShoppingListItem;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.repository.ShoppingListRepository;
import com.shoppinglist.mgmt.util.AppUtils;

/**
 * ShoppingListService provides methods implementation
 * - to create new Shoppinglist in DB 
 * - to find existing Shoppinglist in the DB
 * - to update existing Shoppinglist with new items in the DB
 */
@Service
public class ShoppingListService {

	private static final Logger logger = LoggerFactory.getLogger(ShoppingListService.class);
	
	private ShoppingListRepository shoppingListRepository;
	
	private ItemRepository itemRepository;

	private AppUtils utils;
	
	/**
	 * @param shoppingListRepository
	 * @param itemRepository
	 * @param utils
	 */
	public ShoppingListService(ShoppingListRepository shoppingListRepository, ItemRepository itemRepository, AppUtils utils) {
		this.shoppingListRepository = shoppingListRepository;
		this.itemRepository = itemRepository;
		this.utils = utils;
	}
	
	/**
	 * @param code
	 * @return
	 */
	public ShoppingListResponseDto findByCode(String code) {
		logger.info("Getting Shopping list by code...");
		Optional<ShoppingList> optional = shoppingListRepository.findByCode(code);
		if(optional.isPresent()) {
			return utils.entityToDtoShoppinglist(optional.get());
		}else {
			throw new CustomApplicationException(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
		}
		
	}
	
	/**
	 * @param request
	 * @return
	 */
	public ShoppingListResponseDto createShoppingList(ShoppingListRequestDto request) {
		logger.info("Creating Shopping list ...");
		ShoppingList shopList = new ShoppingList();
		shopList.setName(request.name());
		shopList.setCode(utils.generateShopListCode());
		
		Set<ShoppingListItem> dbItems = new HashSet<>();
		for(ShopListItemDto dtoObj: request.items()) {
			Optional<Item> optItem = itemRepository.findByCode(dtoObj.itemCode());
			if(optItem.isPresent()) {
			dbItems.add(
					ShoppingListItem.builder().itemCode(dtoObj.itemCode()).itemQuantity(dtoObj.itemQuantity()).build());
			}
		}
		if(dbItems.size()<1) {
			throw new CustomApplicationException(Constants.NO_MATCHING_ITEM_IN_DB);
		}
		shopList.setItems(dbItems);
		
		return  utils.entityToDtoShoppinglist(shoppingListRepository.save(shopList));
	}

	/**
	 * @param request
	 * @return
	 */
	public ShoppingListResponseDto updateShoppingList(ShoppingListUpdateRequestDto request) {
		Optional<ShoppingList> optional = shoppingListRepository.findByCode(request.code());
		if(optional.isPresent()) {
			ShoppingList dbShopList = optional.get();
			Set<ShoppingListItem> dbItems = new HashSet<>();
			for(ShopListItemDto dtoObj: request.items()) {
				Optional<Item> optItem = itemRepository.findByCode(dtoObj.itemCode());
				if(optItem.isPresent()) {
					dbItems.add(
							ShoppingListItem.builder().itemCode(dtoObj.itemCode()).itemQuantity(dtoObj.itemQuantity()).build());
				}
			}
			dbShopList.setItems(dbItems);
			return utils.entityToDtoShoppinglist(shoppingListRepository.save(dbShopList));
		}else {
			throw new CustomApplicationException(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
		}
	}
}
