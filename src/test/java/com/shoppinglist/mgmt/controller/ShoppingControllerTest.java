package com.shoppinglist.mgmt.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppinglist.mgmt.dto.ShopListItemDto;
import com.shoppinglist.mgmt.dto.ShoppingListRequestDto;
import com.shoppinglist.mgmt.dto.ShoppingListResponseDto;
import com.shoppinglist.mgmt.dto.ShoppingListUpdateRequestDto;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.security.JwtUtil;
import com.shoppinglist.mgmt.security.config.TestSecurityConfig;
import com.shoppinglist.mgmt.service.ShoppingListService;

@WebMvcTest(controllers = ShoppingController.class)
@Import(TestSecurityConfig.class)
class ShoppingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingListService shoppingListService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void testCreateShoppingList() throws Exception {
    	
    	ShopListItemDto itemDto = createShopListItemDto("itm112", 3);
    	ShoppingListRequestDto request = createShoppingListRequestDto("Weekly Groceries", itemDto);
    	

        ShoppingListResponseDto response = createShoppingListResponseDto("Weekly Groceries", "WEEK123");

        Mockito.when(shoppingListService.createShoppingList(Mockito.any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/shoppinglist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Weekly Groceries"))
                .andExpect(jsonPath("$.code").value("WEEK123"));
    }

    @Test
    void testGetShoppingList() throws Exception {
        String code = "WEEK123";
    	ShoppingListResponseDto response = createShoppingListResponseDto("Weekly Groceries", code);

        Mockito.when(shoppingListService.findByCode(code)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/shoppinglist/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Weekly Groceries"))
                .andExpect(jsonPath("$.code").value(code));
    }

    @Test
    void testUpdateShoppingList() throws Exception {
    	ShopListItemDto itemDto = createShopListItemDto("itm112", 3);
        ShoppingListUpdateRequestDto request = createShoppingListUpdateRequestDto("WEEK123", itemDto);

        ShoppingListResponseDto response = createShoppingListResponseDto("Updated List", "WEEK123");

        Mockito.when(shoppingListService.updateShoppingList(Mockito.any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/shoppinglist")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated List"))
                .andExpect(jsonPath("$.code").value("WEEK123"));
    }
    
    @Test
    void testInvalidRequest() throws JsonProcessingException, Exception {
    	ShopListItemDto itemDto = createShopListItemDto(null, 3);
    	ShoppingListRequestDto request = createShoppingListRequestDto("Weekly Groceries", itemDto);
    	
    	mockMvc.perform(post("/api/v1/shoppinglist")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest());
    	
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
