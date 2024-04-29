package com.licious.oms.service.impl;

import com.licious.oms.common.UUIDService;
import com.licious.oms.dto.request.CreateNewItemRequest;
import com.licious.oms.dto.request.UpdateItemRequest;
import com.licious.oms.dto.response.ItemDto;
import com.licious.oms.entity.ItemEntity;
import com.licious.oms.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles(profiles = "unit-test")
class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private UUIDService uuidService;
    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    @DisplayName("should create new Item successfully")
    void testCreateNewItem() {
        UUID itemId = UUID.fromString("0c4e3cf1-2fe9-4871-9b62-95f429b275b9");

        CreateNewItemRequest createNewItemRequest = new CreateNewItemRequest();
        createNewItemRequest.setDescription("description");
        createNewItemRequest.setName("name");
        createNewItemRequest.setWeight(1000);
        createNewItemRequest.setPrice(new BigDecimal("100.00"));
        createNewItemRequest.setAvailableStockUnits(100L);

        ItemEntity mockItemEntity = new ItemEntity();
        mockItemEntity.setId(itemId);
        mockItemEntity.setDescription("description");
        mockItemEntity.setName("name");
        mockItemEntity.setWeight(1000);
        mockItemEntity.setPrice(new BigDecimal("100.00"));
        mockItemEntity.setAvailableStockUnits(100L);

        ItemDto mockResponseItemDto = new ItemDto();
        mockResponseItemDto.setId(itemId);
        mockResponseItemDto.setDescription("description");
        mockResponseItemDto.setName("name");
        mockResponseItemDto.setWeight(1000);
        mockResponseItemDto.setPrice(new BigDecimal("100.00"));

        when(modelMapper.map(ArgumentMatchers.any(CreateNewItemRequest.class), eq(ItemEntity.class)))
                .thenReturn(mockItemEntity);
        when(uuidService.generateUUID()).thenReturn(itemId);
        when(itemRepository.save(Mockito.any())).thenReturn(mockItemEntity);

        when(modelMapper.map(ArgumentMatchers.any(ItemEntity.class), eq(ItemDto.class)))
                .thenReturn(mockResponseItemDto);

        ItemDto newlyCreatedItem = itemService.createNewItem(createNewItemRequest);

        assertEquals(itemId, newlyCreatedItem.getId());
        assertEquals("name", newlyCreatedItem.getName());
        assertEquals("description", newlyCreatedItem.getDescription());
        assertEquals(1000, newlyCreatedItem.getWeight());
        assertEquals(new BigDecimal("100.00"), newlyCreatedItem.getPrice());
    }

    @Test
    void shouldGetAllItems() {
        UUID itemId1 = UUID.randomUUID();
        UUID itemId2 = UUID.randomUUID();

        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setId(itemId1);
        itemEntity1.setName("Item 1");
        itemEntity1.setDescription("Description 1");
        itemEntity1.setPrice(new BigDecimal("10.00"));
        itemEntity1.setWeight(100);
        itemEntity1.setAvailableStockUnits(50L);

        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setId(itemId2);
        itemEntity2.setName("Item 2");
        itemEntity2.setDescription("Description 2");
        itemEntity2.setPrice(new BigDecimal("20.00"));
        itemEntity2.setWeight(200);
        itemEntity2.setAvailableStockUnits(100L);

        List<ItemEntity> itemEntityList = new ArrayList<>();
        itemEntityList.add(itemEntity1);
        itemEntityList.add(itemEntity2);

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(itemId1);
        itemDto1.setName("Item 1");
        itemDto1.setDescription("Description 1");
        itemDto1.setPrice(new BigDecimal("10.00"));
        itemDto1.setWeight(100);
        itemDto1.setAvailableStockUnits(50L);

        ItemDto itemDto2 = new ItemDto();
        itemDto2.setId(itemId2);
        itemDto2.setName("Item 2");
        itemDto2.setDescription("Description 2");
        itemDto2.setPrice(new BigDecimal("20.00"));
        itemDto2.setWeight(200);
        itemDto2.setAvailableStockUnits(100L);

        when(itemRepository.findAll()).thenReturn(itemEntityList);
        when(modelMapper.map(itemEntity1, ItemDto.class)).thenReturn(itemDto1);
        when(modelMapper.map(itemEntity2, ItemDto.class)).thenReturn(itemDto2);

        List<ItemDto> allItems = itemService.getAllItems();

        assertEquals(2, allItems.size());

        ItemDto returnedItem1 = allItems.get(0);
        assertEquals(itemDto1.getId(), returnedItem1.getId());
        assertEquals(itemDto1.getName(), returnedItem1.getName());
        assertEquals(itemDto1.getDescription(), returnedItem1.getDescription());
        assertEquals(itemDto1.getPrice(), returnedItem1.getPrice());
        assertEquals(itemDto1.getWeight(), returnedItem1.getWeight());
        assertEquals(itemDto1.getAvailableStockUnits(), returnedItem1.getAvailableStockUnits());

        ItemDto returnedItem2 = allItems.get(1);
        assertEquals(itemDto2.getId(), returnedItem2.getId());
        assertEquals(itemDto2.getName(), returnedItem2.getName());
        assertEquals(itemDto2.getDescription(), returnedItem2.getDescription());
        assertEquals(itemDto2.getPrice(), returnedItem2.getPrice());
        assertEquals(itemDto2.getWeight(), returnedItem2.getWeight());
        assertEquals(itemDto2.getAvailableStockUnits(), returnedItem2.getAvailableStockUnits());
    }

    @Test
    void shouldGetItemById() {
        UUID itemId = UUID.randomUUID();

        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(itemId);
        itemEntity.setName("Test Item");
        itemEntity.setDescription("Test Description");
        itemEntity.setPrice(new BigDecimal("10.00"));
        itemEntity.setWeight(100);
        itemEntity.setAvailableStockUnits(50L);

        ItemDto expectedItemDto = new ItemDto();
        expectedItemDto.setId(itemId);
        expectedItemDto.setName("Test Item");
        expectedItemDto.setDescription("Test Description");
        expectedItemDto.setPrice(new BigDecimal("10.00"));
        expectedItemDto.setWeight(100);
        expectedItemDto.setAvailableStockUnits(50L);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(itemEntity));
        when(modelMapper.map(itemEntity, ItemDto.class)).thenReturn(expectedItemDto);

        ItemDto returnedItemDto = itemService.getItemById(itemId);

        assertEquals(expectedItemDto.getId(), returnedItemDto.getId());
        assertEquals(expectedItemDto.getName(), returnedItemDto.getName());
        assertEquals(expectedItemDto.getDescription(), returnedItemDto.getDescription());
        assertEquals(expectedItemDto.getPrice(), returnedItemDto.getPrice());
        assertEquals(expectedItemDto.getWeight(), returnedItemDto.getWeight());
        assertEquals(expectedItemDto.getAvailableStockUnits(), returnedItemDto.getAvailableStockUnits());
    }

    @Test
    void shouldUpdateItem() {
        UUID itemId = UUID.randomUUID();

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Updated Name");
        updateItemRequest.setDescription("Updated Description");
        updateItemRequest.setPrice(new BigDecimal("20.00"));
        updateItemRequest.setWeight(200);
        updateItemRequest.setAvailableStockUnits(100L);

        ItemEntity existingItemEntity = new ItemEntity();
        existingItemEntity.setId(itemId);
        existingItemEntity.setName("Original Name");
        existingItemEntity.setDescription("Original Description");
        existingItemEntity.setPrice(new BigDecimal("10.00"));
        existingItemEntity.setWeight(100);
        existingItemEntity.setAvailableStockUnits(50L);

        ItemEntity updatedItemEntity = new ItemEntity();
        updatedItemEntity.setId(itemId);
        updatedItemEntity.setName("Updated Name");
        updatedItemEntity.setDescription("Updated Description");
        updatedItemEntity.setPrice(new BigDecimal("20.00"));
        updatedItemEntity.setWeight(200);
        updatedItemEntity.setAvailableStockUnits(100L);

        ItemDto expectedUpdatedItemDto = new ItemDto();
        expectedUpdatedItemDto.setId(itemId);
        expectedUpdatedItemDto.setName("Updated Name");
        expectedUpdatedItemDto.setDescription("Updated Description");
        expectedUpdatedItemDto.setPrice(new BigDecimal("20.00"));
        expectedUpdatedItemDto.setWeight(200);
        expectedUpdatedItemDto.setAvailableStockUnits(100L);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItemEntity));
        when(itemRepository.save(existingItemEntity)).thenReturn(updatedItemEntity);
        when(modelMapper.map(updatedItemEntity, ItemDto.class)).thenReturn(expectedUpdatedItemDto);
        
        ItemDto updatedItemDto = itemService.updateItem(itemId, updateItemRequest);

        assertEquals(expectedUpdatedItemDto.getId(), updatedItemDto.getId());
        assertEquals(expectedUpdatedItemDto.getName(), updatedItemDto.getName());
        assertEquals(expectedUpdatedItemDto.getDescription(), updatedItemDto.getDescription());
        assertEquals(expectedUpdatedItemDto.getPrice(), updatedItemDto.getPrice());
        assertEquals(expectedUpdatedItemDto.getWeight(), updatedItemDto.getWeight());
        assertEquals(expectedUpdatedItemDto.getAvailableStockUnits(), updatedItemDto.getAvailableStockUnits());

        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(existingItemEntity);
        verify(modelMapper).map(updatedItemEntity, ItemDto.class);
    }

    @Test
    void updateItemShouldThrowEntityNotFoundException() {
        UUID itemId = UUID.randomUUID();

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Updated Name");
        updateItemRequest.setDescription("Updated Description");
        updateItemRequest.setPrice(new BigDecimal("20.00"));
        updateItemRequest.setWeight(200);
        updateItemRequest.setAvailableStockUnits(100L);

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        
        assertThrows(EntityNotFoundException.class, () -> itemService.updateItem(itemId, updateItemRequest));

        verify(itemRepository).findById(itemId);
        verify(itemRepository, never()).save(Mockito.any());
        verify(modelMapper, never()).map(Mockito.any(), Mockito.eq(ItemDto.class));
    }

    @Test
    void shouldUpdateAvailableStockOfAnItem() {
        UUID itemId = UUID.randomUUID();
        Long newStock = 200L;

        ItemEntity existingItemEntity = new ItemEntity();
        existingItemEntity.setId(itemId);
        existingItemEntity.setName("Test Item");
        existingItemEntity.setDescription("Test Description");
        existingItemEntity.setPrice(new BigDecimal("10.00"));
        existingItemEntity.setWeight(100);
        existingItemEntity.setAvailableStockUnits(100L);

        ItemEntity updatedItemEntity = new ItemEntity();
        updatedItemEntity.setId(itemId);
        updatedItemEntity.setName("Test Item");
        updatedItemEntity.setDescription("Test Description");
        updatedItemEntity.setPrice(new BigDecimal("10.00"));
        updatedItemEntity.setWeight(100);
        updatedItemEntity.setAvailableStockUnits(newStock);

        ItemDto expectedUpdatedItemDto = new ItemDto();
        expectedUpdatedItemDto.setId(itemId);
        expectedUpdatedItemDto.setName("Test Item");
        expectedUpdatedItemDto.setDescription("Test Description");
        expectedUpdatedItemDto.setPrice(new BigDecimal("10.00"));
        expectedUpdatedItemDto.setWeight(100);
        expectedUpdatedItemDto.setAvailableStockUnits(newStock);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItemEntity));
        when(itemRepository.save(existingItemEntity)).thenReturn(updatedItemEntity);
        when(modelMapper.map(updatedItemEntity, ItemDto.class)).thenReturn(expectedUpdatedItemDto);

        
        ItemDto updatedItemDto = itemService.updateAvailableStockOfAnItem(itemId, newStock);

        
        assertEquals(expectedUpdatedItemDto.getId(), updatedItemDto.getId());
        assertEquals(expectedUpdatedItemDto.getName(), updatedItemDto.getName());
        assertEquals(expectedUpdatedItemDto.getDescription(), updatedItemDto.getDescription());
        assertEquals(expectedUpdatedItemDto.getPrice(), updatedItemDto.getPrice());
        assertEquals(expectedUpdatedItemDto.getWeight(), updatedItemDto.getWeight());
        assertEquals(expectedUpdatedItemDto.getAvailableStockUnits(), updatedItemDto.getAvailableStockUnits());

        verify(itemRepository).findById(itemId);
        verify(itemRepository).save(existingItemEntity);
        verify(modelMapper).map(updatedItemEntity, ItemDto.class);
    }

    @Test
    void updateAvailableStockOfAnItemShouldThrowsEntityNotFoundException() {
        UUID itemId = UUID.randomUUID();
        Long newStock = 200L;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> itemService.updateAvailableStockOfAnItem(itemId, newStock));

        verify(itemRepository).findById(itemId);
        verify(itemRepository, never()).save(Mockito.any());
        verify(modelMapper, never()).map(Mockito.any(), Mockito.eq(ItemDto.class));
    }
}