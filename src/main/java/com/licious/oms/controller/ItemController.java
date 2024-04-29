package com.licious.oms.controller;

import com.licious.oms.dto.request.CreateNewItemRequest;
import com.licious.oms.dto.request.UpdateItemRequest;
import com.licious.oms.dto.response.ItemDto;
import com.licious.oms.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ItemDto> createNewItem(@Valid @RequestBody CreateNewItemRequest item) {
        log.info("ItemController::createNewItem {}", item);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.createNewItem(item));
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<ItemDto>> getAllItems() {
        log.info("ItemController::getAllItems");
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.getAllItems());
    }

    @GetMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<ItemDto> getItemById(@PathVariable UUID id) {
        log.info("ItemController::getItemById");
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.getItemById(id));
    }

    @PutMapping(value = "/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateItemRequest updateItemRequest
    ) {
        log.info("ItemController::updateItem {}", itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.updateItem(itemId, updateItemRequest));
    }

    @PatchMapping(value = "/{itemId}/update-stock/{newStock}")
    public ResponseEntity<ItemDto> updateAvailableStockOfAnItem(
            @PathVariable UUID itemId,
            @PathVariable Long newStock
    ) {
        log.info("ItemController::updateAvailableStockOfAnItem {}", itemId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(itemService.updateAvailableStockOfAnItem(itemId, newStock));
    }

}
