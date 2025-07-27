package com.shoppinglist.mgmt.service;

import com.shoppinglist.mgmt.constants.Constants;
import com.shoppinglist.mgmt.dto.ItemRequest;
import com.shoppinglist.mgmt.dto.ItemRequestDto;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.dto.ItemResponseDto;
import com.shoppinglist.mgmt.exception.CustomApplicationException;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.util.AppUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private AppUtils utils;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllItems() {
        List<Item> items = List.of(createItemEntity("Milk","itm1211",1.09));
        List<ItemResponseDto> itemDtos = List.of(createItemResponseDto("Milk","itm1211",1.09));

        when(itemRepository.findAll()).thenReturn(items);
        when(utils.entityToDtoItem(items)).thenReturn(itemDtos);

        ItemResponse response = itemService.findAll();

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).itemName()).isEqualTo("Milk");

        verify(itemRepository, times(1)).findAll();
        verify(utils, times(1)).entityToDtoItem(items);
    }

    @Test
    void testGetItemByCode_Success() {
        Item item = createItemEntity("Milk","itm1211",1.09);
        ItemResponseDto dto = createItemResponseDto("Milk","itm1211",1.09);

        when(itemRepository.findByCode("itm1211")).thenReturn(Optional.of(item));
        when(utils.toDto(item)).thenReturn(dto);

        ItemResponse response = itemService.getItemByCode("itm1211");

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).itemCode()).isEqualTo("itm1211");

        verify(itemRepository).findByCode("itm1211");
    }

    @Test
    void testGetItemByCode_NotFound() {
        when(itemRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemService.getItemByCode("UNKNOWN"))
                .isInstanceOf(CustomApplicationException.class)
                .hasMessage(Constants.NO_ITEM_FOUND_FOR_CODE);
    }

    @Test
    void testSaveItems() {
        List<ItemRequestDto> inputDtos = List.of(createItemRequestDto("Bread",1.59));
        List<Item> inputEntities = List.of(createItemEntity("Bread", "itm1009", 1.59));
        List<Item> savedEntities = List.of(createItemEntity("Bread", "itm1009", 1.59));
        List<ItemResponseDto> savedDtos = List.of(createItemResponseDto("Bread","itm1009",1.59));

        when(utils.dtoToEntity(inputDtos)).thenReturn(inputEntities);
        when(itemRepository.saveAll(inputEntities)).thenReturn(savedEntities);
        when(utils.entityToDtoItem(savedEntities)).thenReturn(savedDtos);

        ItemRequest request = new ItemRequest(inputDtos);
        ItemResponse response = itemService.saveItems(request);

        assertThat(response.items()).hasSize(1);
        assertThat(response.items().get(0).itemCode()).isEqualTo("itm1009");

        verify(itemRepository).saveAll(inputEntities);
        verify(utils).dtoToEntity(inputDtos);
        verify(utils).entityToDtoItem(savedEntities);
    }
    
    @Test
    void testGetItemByCode_NullCode_ThrowsException() {
        assertThatThrownBy(() -> itemService.getItemByCode(null))
            .isInstanceOf(CustomApplicationException.class)
            .hasMessage(Constants.NO_ITEM_FOUND_FOR_CODE); 
    }
    
    @Test
    void testSaveItems_NullItem_ThrowsException() {

    	assertThatThrownBy(() -> itemService.saveItems(new ItemRequest(null)))
    			.isInstanceOf(CustomApplicationException.class)
    			.hasMessage(Constants.ITEM_REQUIRED_ERROR);

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
    
    Item createItemEntity(String name, String code,Double price) {
    	return Item.builder()
    			.itemCode(code)
    			.itemName(name)
    			.itemPrice(price)
    			.active(true)
    			.deleted(false)
    			.bought(false).build();
    }
    
    ItemRequestDto createItemRequestDto(String name, Double price) {
    	return new ItemRequestDto(name, price);
    }
}
