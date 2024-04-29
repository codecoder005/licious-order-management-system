package com.licious.oms.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Map;
import java.util.UUID;

import static com.licious.oms.common.AppConstants.InfoMessage.*;
import static com.licious.oms.common.AppConstants.Regex.REGEX_PHONE_NUMBER;
import static com.licious.oms.common.AppConstants.Regex.REGEX_PINCODE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PlaceOrderRequest {
    @NotNull(message = NO_ITEMS_SELECTED)
    @NotEmpty(message = NO_ITEMS_SELECTED)
    private Map<UUID, Integer> items;

    @NotNull(message = DELIVERY_ADDRESS_REQUIRED)
    private @Valid Address deliveryAddress;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static final class Address {
        @NotNull(message = ADDRESS_LINE1_REQUIRED)
        @NotBlank(message = ADDRESS_LINE1_BLANK)
        private String addressLine1;
        private String addressLine2;

        @NotNull(message = CITY_REQUIRED)
        @NotBlank(message = CITY_BLANK)
        private String city;

        @NotNull(message = STATE_REQUIRED)
        @NotBlank(message = STATE_BLANK)
        private String state;

        @NotNull(message = PINCODE_REQUIRED)
        @NotBlank(message = PINCODE_BLANK)
        @Pattern(regexp = REGEX_PINCODE, message = PINCODE_INVALID)
        private String pinCode;

        @NotNull(message = PHONE_NUMBER_REQUIRED)
        @Pattern(regexp = REGEX_PHONE_NUMBER, message = INVALID_PHONE_NUMBER)
        private String phoneNumber;

        private String email;
    }
}
