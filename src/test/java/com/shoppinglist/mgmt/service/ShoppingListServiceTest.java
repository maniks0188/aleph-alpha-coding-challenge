package com.shoppinglist.mgmt.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ShopListItemDto;
import com.shoppinglist.mgmt.dto.ShoppingListRequestDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.dto.ShoppingListUpdateRequestDto;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.model.ShoppingList;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.repository.ShoppingListRepository;
import com.shoppinglist.mgmt.util.AppUtils;

class ShoppingListServiceTest {

    @Mock
    private ShoppingListRepository shoppingListRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AppUtils appUtils;

    @InjectMocks
    private ShoppingListService shoppingListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByCode_Success() {
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setCode("CODE123");

        ShoppingListResponseDto dto = createShoppingListResponseDto("shoplist-1", "CODE123");

        when(shoppingListRepository.findByCode("CODE123")).thenReturn(Optional.of(shoppingList));
        when(appUtils.entityToDtoShoppinglist(shoppingList)).thenReturn(dto);

        ShoppingListResponseDto result = shoppingListService.findByCode("CODE123");

        assertThat(result.code()).isEqualTo("CODE123");
    }

    @Test
    void testFindByCode_NotFound() {
        when(shoppingListRepository.findByCode("NOT_FOUND")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shoppingListService.findByCode("NOT_FOUND"))
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
    }

    @Test
    void testCreateShoppingList_Success() {
        // Request
        ShopListItemDto itemDto = createShopListItemDto("itm121", 2);
        ShoppingListRequestDto requestDto = createShoppingListRequestDto("shoplist-1", itemDto);

        // Mock item exists
        when(itemRepository.findByCode("itm121")).thenReturn(Optional.of(new Item()));

        // Mock utility
        when(appUtils.generateShopListCode()).thenReturn("CODE123");

        // Mock repository save
        ArgumentCaptor<ShoppingList> captor = ArgumentCaptor.forClass(ShoppingList.class);
        ShoppingList savedList = new ShoppingList();
        savedList.setCode("CODE123");
        when(shoppingListRepository.save(any())).thenReturn(savedList);
        when(appUtils.entityToDtoShoppinglist(any())).thenReturn(createShoppingListResponseDto("shoplist-1", "CODE123"));

        ShoppingListResponseDto response = shoppingListService.createShoppingList(requestDto);

        assertThat(response.code()).isEqualTo("CODE123");
        verify(shoppingListRepository).save(captor.capture());
        assertThat(captor.getValue().getItems()).hasSize(1);
    }

    @Test
    void testCreateShoppingList_ItemNotFound() {
        // Item not found in DB, so it should skip adding it
        ShopListItemDto itemDto = new ShopListItemDto("INVALID", 1);
        ShoppingListRequestDto requestDto = createShoppingListRequestDto("shoplist-1", itemDto);

        when(itemRepository.findByCode("INVALID")).thenReturn(Optional.empty());
        when(appUtils.generateShopListCode()).thenReturn("CODE321");

        ShoppingList saved = new ShoppingList();
        saved.setCode("CODE321");
        saved.setItems(Set.of());

        when(shoppingListRepository.save(any())).thenReturn(saved);
        when(appUtils.entityToDtoShoppinglist(any())).thenReturn(createShoppingListResponseDto("shoplist-1", "CODE321"));

        assertThatThrownBy(() -> shoppingListService.createShoppingList(requestDto))
        .isInstanceOf(CustomApplicationException.class)
        .hasMessage(Constants.NO_MATCHING_ITEM_IN_DB);
    }


    @Test
    void testUpdateShoppingList_Success() {
        ShopListItemDto itemDto = createShopListItemDto("ITEMX", 3);
        ShoppingListUpdateRequestDto requestDto = createShoppingListUpdateRequestDto("CODE999", itemDto);

        ShoppingList existingList = new ShoppingList();
        existingList.setCode("CODE999");

        when(shoppingListRepository.findByCode("CODE999")).thenReturn(Optional.of(existingList));
        when(itemRepository.findByCode("ITEMX")).thenReturn(Optional.of(new Item()));
        when(shoppingListRepository.save(any())).thenReturn(existingList);
        when(appUtils.entityToDtoShoppinglist(any())).thenReturn(createShoppingListResponseDto("shoplist-1", "CODE999"));

        ShoppingListResponseDto response = shoppingListService.updateShoppingList(requestDto);

        assertThat(response.code()).isEqualTo("CODE999");
        verify(shoppingListRepository).save(existingList);
    }

    @Test
    void testUpdateShoppingList_NotFound() {
        when(shoppingListRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        ShoppingListUpdateRequestDto requestDto = createShoppingListUpdateRequestDto("UNKNOWN", null);	

        assertThatThrownBy(() -> shoppingListService.updateShoppingList(requestDto))
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage(Constants.NO_SHOPLIST_FOUND_FOR_CODE);
    }
    
    ShoppingListResponseDto createShoppingListResponseDto(String name,String code) {
    	return new ShoppingListResponseDto.Builder()
    			.name(name)
    			.code(code)
    			.items(List.of(createShopListItemDto("itm12112", 2)))
    			.build();
    }
    
    ShoppingListRequestDto createShoppingListRequestDto(String name, ShopListItemDto item) {
    	if(item == null)
    		return new ShoppingListRequestDto(name, List.of(createShopListItemDto("itm121", 2)));
    	else
    		return new ShoppingListRequestDto(name, List.of(item));
    }
    
    ShoppingListUpdateRequestDto createShoppingListUpdateRequestDto(String code, ShopListItemDto item) {
    	if(item == null)
    		return new ShoppingListUpdateRequestDto(code, List.of(createShopListItemDto("itm121", 2)));
    	else
    		return new ShoppingListUpdateRequestDto(code, List.of(item));
    }
    
    ShopListItemDto createShopListItemDto(String itemCode, int quantity) {
    	return new ShopListItemDto(itemCode, quantity);
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
}
