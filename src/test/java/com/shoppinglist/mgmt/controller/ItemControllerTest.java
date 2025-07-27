package com.shoppinglist.mgmt.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shoppinglist.mgmt.dto.ItemRequest;
import com.shoppinglist.mgmt.dto.ItemRequestDto;
import com.shoppinglist.mgmt.dto.ItemResponse;
import com.shoppinglist.mgmt.dto.ItemResponseDto;
import com.shoppinglist.mgmt.model.Item;
import com.shoppinglist.mgmt.security.JwtUtil;
import com.shoppinglist.mgmt.security.config.TestSecurityConfig;
import com.shoppinglist.mgmt.service.ItemService;

@WebMvcTest(ItemController.class)
@Import(TestSecurityConfig.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @Test
    void getAllItems_ShouldReturn200WithItemList() throws Exception {
        List<ItemResponseDto> items = List.of(createItemResponseDto("Milk", "itm112", 1.09));
        ItemResponse response = new ItemResponse(items);

        when(itemService.findAll()).thenReturn(response);

        mockMvc.perform(get("/api/v1/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].itemName").value("Milk"));

        verify(itemService).findAll();
    }

    @Test
    void getItemByCode_ShouldReturn200WithItem() throws Exception {
        String code = "itm112";
        List<ItemResponseDto> items = List.of(createItemResponseDto("Milk", "itm112", 1.09));
        ItemResponse response = new ItemResponse(items);

        when(itemService.getItemByCode(code)).thenReturn(response);

        mockMvc.perform(get("/api/v1/items/{code}", code))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].itemName").value("Milk"));

        verify(itemService).getItemByCode(code);
    }

    // ===== POST /items =====
    @Test
    void createItem_ShouldReturn200WhenRequestIsValid() throws Exception {
        ItemRequestDto itemDto = createItemRequestDto("Bread", 2.0); 
        ItemRequest request = new ItemRequest(List.of(itemDto));
        ItemResponse response = new ItemResponse(List.of(createItemResponseDto("Bread", "itm112", 2.0)));

        when(itemService.saveItems(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].itemName").value("Bread"));

        verify(itemService).saveItems(request);
    }

    @Test
    void createItem_ShouldReturn400WhenRequestIsInvalid() throws Exception {
        // Missing item name/price
        ItemRequestDto item = createItemRequestDto(null, null);
        ItemRequest request = new ItemRequest(List.of(item));

        mockMvc.perform(post("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
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
