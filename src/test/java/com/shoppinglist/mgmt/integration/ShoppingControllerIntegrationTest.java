package com.shoppinglist.mgmt.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppinglist.mgmt.dto.ShopListItemDto;
import com.shoppinglist.mgmt.dto.ShoppingListRequestDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.dto.ShoppingListUpdateRequestDto;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.repository.ItemRepository;
import com.shoppinglist.mgmt.repository.ShoppingListRepository;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ShoppingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListRepository shoppingListRepository;
    
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;
    List<Item> items = new ArrayList<Item>();
    
    
    @BeforeEach
    void setUp() {
    	itemRepository.deleteAll();
    	
    	Item item = Item.builder()
    	.itemCode("itm121")
    	.itemName("Milk")
    	.itemPrice(1.29)
    	.active(true)
    	.deleted(false)
    	.bought(false).build();
    	
    	items.add(item);
    	
    	Item item2 = Item.builder()
    	    	.itemCode("itm231")
    	    	.itemName("Butter")
    	    	.itemPrice(2.29)
    	    	.active(true)
    	    	.deleted(false)
    	    	.bought(false).build();
    	
    	items.add(item2);
    	itemRepository.saveAll(items);
    }
    
    
    @Test
    @Order(1)
    void testCreateAndFetchShoppingList() throws Exception {
        // --- Step 1: Create DTO ---
        ShoppingListRequestDto requestDto = createShoppingListRequestDto("shoplist-1", createShopListItemDto("itm121", 3));

        // --- Step 2: Create Shopping List ---
        String responseJson = mockMvc.perform(post("/api/v1/shoppinglist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("shoplist-1")))
                .andExpect(jsonPath("$.code").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ShoppingListResponseDto created = objectMapper.readValue(responseJson, ShoppingListResponseDto.class);

        // --- Step 3: Fetch by Code ---
        mockMvc.perform(get("/api/v1/shoppinglist/" + created.code()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(created.code())))
                .andExpect(jsonPath("$.name", is("shoplist-1")));
    }

    @Test
    @Order(2)
    void testUpdateShoppingList() throws Exception {
        // Step 1: Create a shopping list
        ShoppingListRequestDto createRequest = createShoppingListRequestDto("updateshoplist-1", createShopListItemDto("itm121", 3));

        String responseJson = mockMvc.perform(post("/api/v1/shoppinglist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ShoppingListResponseDto original = objectMapper.readValue(responseJson, ShoppingListResponseDto.class);

        // Step 2: Update list
        ShoppingListUpdateRequestDto updateRequest = createShoppingListUpdateRequestDto(original.code(), createShopListItemDto("itm231", 5));

        mockMvc.perform(put("/api/v1/shoppinglist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is(original.code())))
                .andExpect(jsonPath("$.items[0].itemCode", is("itm231")))
                .andExpect(jsonPath("$.items[0].itemQuantity", is(5)));
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
