package com.licious.oms.service.impl;

import com.licious.oms.common.UUIDService;
import com.licious.oms.dto.request.CreateNewItemRequest;
import com.licious.oms.dto.request.UpdateItemRequest;
import com.licious.oms.dto.response.ItemDto;
import com.licious.oms.entity.ItemEntity;
import com.licious.oms.repository.ItemRepository;
import com.licious.oms.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;
    private final UUIDService uuidService;

    public ItemDto createNewItem(CreateNewItemRequest item) {
        log.info("ItemService::createNewItem");
        ItemEntity itemEntity = modelMapper.map(item, ItemEntity.class);
        itemEntity.setId(uuidService.generateUUID());
        return modelMapper.map(itemRepository.save(itemEntity), ItemDto.class);
    }

    public List<ItemDto> getAllItems() {
        log.info("ItemService::getAllItems");
        return itemRepository.findAll().stream().map(item -> modelMapper.map(item, ItemDto.class)).toList();
    }

    public ItemDto getItemById(UUID id) {
        log.info("ItemService::getItemById");
        return modelMapper.map(itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("no item found with id: " + id)),
                ItemDto.class
        );
    }

    public ItemDto updateItem(UUID itemId, UpdateItemRequest updateItemRequest) {
        log.info("ItemService::updateItem");
        AtomicReference<ItemEntity> itemEntity = new AtomicReference<>();
        itemRepository.findById(itemId).ifPresentOrElse(
                item -> {
                    item.setName(updateItemRequest.getName());
                    item.setDescription(updateItemRequest.getDescription());
                    item.setPrice(updateItemRequest.getPrice());
                    item.setWeight(updateItemRequest.getWeight());
                    item.setAvailableStockUnits(updateItemRequest.getAvailableStockUnits());
                    itemEntity.set(itemRepository.save(item));
                },
                () -> {
                    throw new EntityNotFoundException("no item found with id: " + itemId);
                }
        );
        return modelMapper.map(itemEntity.get(), ItemDto.class);
    }

    public ItemDto updateAvailableStockOfAnItem(UUID itemId, Long newStock) {
        AtomicReference<ItemEntity> updatedItem = new AtomicReference<>();
        itemRepository.findById(itemId).ifPresentOrElse(
                (item) -> {
                    item.setAvailableStockUnits(newStock);
                    updatedItem.set(itemRepository.save(item));
                },
                () -> {
                    throw new EntityNotFoundException("no item found with id: " + itemId);
                }
        );
        return modelMapper.map(updatedItem.get(), ItemDto.class);
    }
}
