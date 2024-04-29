package com.licious.oms.dto.request;

import com.licious.oms.common.AppConstants;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

import static com.licious.oms.common.AppConstants.InfoMessage.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UpdateItemRequest {
    @NotNull(message = NAME_REQUIRED)
    @NotBlank(message = NAME_BLANK)
    @Pattern(regexp = AppConstants.Regex.REGEX_ITEM_NAME, message = NAME_REGEX_NOT_MATCH)
    private String name;

    @Pattern(regexp = AppConstants.Regex.REGEX_ITEM_DESCRIPTION, message = DESCRIPTION_REGEX_NOT_MATCH)
    private String description;

    @NotNull(message = WEIGHT_REQUIRED)
    @Min(value = 1, message = MINIMUM_WEIGHT_REQUIRED)
    private Integer weight;

    @NotNull(message = PRICE_REQUIRED)
    @DecimalMin(value = "1.0", message = MINIMUM_PRICE_REQUIRED)
    private BigDecimal price;

    @NotNull(message = AVAILABLE_STOCK_REQUIRED)
    @Min(value = 1, message = MINIMUM_AVAILABLE_STOCK_REQUIRED)
    private Long availableStockUnits;
}
