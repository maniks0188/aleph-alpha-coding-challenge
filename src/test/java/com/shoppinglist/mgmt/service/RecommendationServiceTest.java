package com.shoppinglist.mgmt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.dto.ItemResponseDto;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.model.ShoppingList;
import com.shoppinglist.mgmt.model.ShoppingListItem;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.repository.ShoppingListRepository;
import com.shoppinglist.mgmt.suggestionengine.RecommendationEngineContext;
import com.shoppinglist.mgmt.suggestionengine.RecommendationStrategyType;
import com.shoppinglist.mgmt.util.AppUtils;

class RecommendationServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private AppUtils appUtils;

    @Mock
    private RecommendationEngineContext recommendationEngine;

    @InjectMocks
    private RecommendationService recommendationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindItemsStartingWith_ValidPrefix() {
        String prefix = "mil";
        int limit = 3;
        List<Item> items = List.of(createItemEntity("Milk", "itm112", 10.9));
        List<ItemResponseDto> dtos = List.of(createItemResponseDto("Milk", "itm112", 10.9));

        when(itemRepository.findItemsByNameStartingWith(eq(prefix), any(Pageable.class))).thenReturn(items);
        when(appUtils.entityToDtoItem(items)).thenReturn(dtos);

        ItemResponse response = recommendationService.findItemsStartingWith(prefix, limit);

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).itemName()).isEqualTo("Milk");

        verify(itemRepository).findItemsByNameStartingWith(eq(prefix), any(Pageable.class));
    }

    @Test
    void testFindItemsStartingWith_InvalidPrefix() {
        assertThatThrownBy(() -> recommendationService.findItemsStartingWith("mi", 3))
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage(Constants.INVALID_PREFIX);
    }

    @Test
    void testFindSuggestionsForShoppingList_Success() {
        String code = "SHOP123";
        RecommendationStrategyType strategy = RecommendationStrategyType.BASIC;

        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setCode(code);

        ShoppingListItem sli = ShoppingListItem.builder().itemCode("gin").itemQuantity(1).build();
        shoppingList.setItems(Set.of(sli));

        // Mock recommendation engine returns related codes
        List<String> recommendedCodes = List.of("crodino", "sanbitter");

        // Mock repository returns items for those codes
        List<Item> recommendedItems = List.of(createItemEntity("Crodino", "itm112", 2.5));
        List<ItemResponseDto> dtoList = List.of(createItemResponseDto("Crodino","itm112", 2.5));

        when(shoppingListRepository.findByCode(code)).thenReturn(Optional.of(shoppingList));
        when(recommendationEngine.recommend(List.of("gin"), strategy)).thenReturn(recommendedCodes);
        when(itemRepository.findByItemCodeIn(recommendedCodes)).thenReturn(recommendedItems);
        when(appUtils.entityToDtoItem(recommendedItems)).thenReturn(dtoList);

        ItemResponse response = recommendationService.findSuggestionsForShoppingList(code, strategy);

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).itemName()).isEqualTo("Crodino");
    }

    @Test
    void testFindSuggestionsForShoppingList_NotFound() {
        when(shoppingListRepository.findByCode("NOT_FOUND")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recommendationService.findSuggestionsForShoppingList("NOT_FOUND", RecommendationStrategyType.BASIC))
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
    }
    
    Item createItemEntity(String name, String code,Double price) {
    	return Item.builder()
    			.itemCode(code)
    			.itemName(name)
    			.itemPrice(price)
    			.active(true)
    			.deleted(false)
    			.bought(false).build();
    }
    
    ItemResponseDto createItemResponseDto(String name, String code,Double price) {
    	return new ItemResponseDto.Builder()
    			.itemCode(code)
    			.itemName(name)
    			.itemPrice(price)
    			.active(true)
    			.deleted(false)
    			.bought(false).build();
    }
    
}
