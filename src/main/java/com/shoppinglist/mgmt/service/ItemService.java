package com.shoppinglist.mgmt.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemRequest;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.util.AppUtils;

/**
 * ItemService provides methods implementation
 * to create new items in DB and to find existing items in the DB.
 */
@Service
public class ItemService {
 
	private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
	
	private ItemRepository itemRepository;
	
	private AppUtils utils;
	
	/**
	 * @param itemRepository
	 * @param utils
	 */
	public ItemService(ItemRepository itemRepository,AppUtils utils) {
		this.itemRepository = itemRepository;
		this.utils = utils;
	}

	public ItemResponse findAll(){
		logger.info("Getting all the Items from DB...");
		return new ItemResponse(utils.entityToDtoItem(itemRepository.findAll()));
	}
	
	
	/**
	 * @param code
	 * @return
	 */
	public ItemResponse getItemByCode(String code) {
		logger.info("Getting the Item by code from DB...");
		Optional<Item> optItem = itemRepository.findByCode(code);
		if(optItem.isPresent()) {
			return new ItemResponse(Arrays.asList(utils.toDto(optItem.get())));
		}else {
			logger.info("No Item found for the code.");
			throw new CustomApplicationException(Constants.NO_ITEM_FOUND_FOR_CODE);
		}
	}
	
	
	/**
	 * @param request
	 * @return
	 */
	public ItemResponse saveItems(ItemRequest request) {
		logger.info("Saving the Items in DB...");
		if(null == request.items() || request.items().isEmpty()) {
			logger.info(Constants.ITEM_REQUIRED_ERROR);
			throw new CustomApplicationException(Constants.ITEM_REQUIRED_ERROR);
		}
		List<Item> savedItems = itemRepository.saveAll(utils.dtoToEntity(request.items()));
		return new ItemResponse(utils.entityToDtoItem(savedItems));
	}
	
}
