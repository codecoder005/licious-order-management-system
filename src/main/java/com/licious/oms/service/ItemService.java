package com.licious.oms.service;

import com.licious.oms.dto.request.CreateNewItemRequest;
import com.licious.oms.dto.request.UpdateItemRequest;
import com.licious.oms.dto.response.ItemDto;

import java.util.List;
import java.util.UUID;

public interface ItemService {
    /**
     * Creates a new item based on the provided item request.
     *
     * @param item The request containing details of the item to be created.
     * @return A DTO representing the newly created item.
     */
    ItemDto createNewItem(CreateNewItemRequest item);

    /**
     * Retrieves a list of all items.
     *
     * @return A list of ItemDto objects representing all items.
     */
    List<ItemDto> getAllItems();

    /**
     * Retrieves an item by its unique identifier.
     *
     * @param id The unique identifier of the item to retrieve.
     * @return The ItemDto object representing the item with the specified ID.
     * @throws jakarta.persistence.EntityNotFoundException    If no item with the provided ID is found.
     */
    ItemDto getItemById(UUID id);

    /**
     * Updates an existing item with the provided details.
     *
     * @param itemId            The unique identifier of the item to update.
     * @param updateItemRequest The request containing updated details of the item.
     * @return The ItemDto object representing the updated item.
     * @throws IllegalArgumentException If the provided item ID is null or if the update request is invalid.
     * @throws jakarta.persistence.EntityNotFoundException   If no item with the provided ID is found.
     */
    ItemDto updateItem(UUID itemId, UpdateItemRequest updateItemRequest);

    /**
     * Updates the available stock of an existing item with the provided new stock value.
     *
     * @param itemId   The unique identifier of the item to update.
     * @param newStock The new available stock value for the item.
     * @return The ItemDto object representing the item with the updated available stock.
     * @throws jakarta.persistence.EntityNotFoundException   If no item with the provided ID is found.
     */
    ItemDto updateAvailableStockOfAnItem(UUID itemId, Long newStock);
}
