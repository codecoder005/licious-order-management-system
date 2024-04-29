package com.licious.oms.common;

public class AppConstants {
    public static class Regex {
        public static final String REGEX_ITEM_NAME = "^[a-zA-Z0-9\\s]{2,50}$";
        public static final String REGEX_ITEM_DESCRIPTION = "^[\\w\\s\\.,\\-\\/]*$";
        public static final String REGEX_PHONE_NUMBER = "^((\\+91)?(0)?)([789]\\d{9})$";
        public static final String REGEX_PINCODE = "^[1-9][0-9]{5}$";

    }
    public static class ErrorMessage {
        public static final String VALIDATION_CRITERIA_FAILED = "One or more fields are failing validation criteria";
    }
    public static class InfoMessage {
        public static final String NAME_REQUIRED = "'name' is required";
        public static final String NAME_BLANK = "'name' can not be blank";
        public static final String NAME_REGEX_NOT_MATCH = "'name' must be between 2 and 50 characters and contain only letters, numbers, and spaces";


        public static final String DESCRIPTION_REGEX_NOT_MATCH = "'description' must contain only letters, numbers, spaces, and common punctuation marks";

        public static final String WEIGHT_REQUIRED = "'weight' is required";
        public static final String MINIMUM_WEIGHT_REQUIRED = "'minimum' weight of an item required is 1g";

        public static final String PRICE_REQUIRED = "'price' is required";
        public static final String MINIMUM_PRICE_REQUIRED = "minimum value of 'price' is 1.0 INR";

        public static final String AVAILABLE_STOCK_REQUIRED = "'availableStockUnits' is required";
        public static final String MINIMUM_AVAILABLE_STOCK_REQUIRED = "minimum value of 'availableStockUnits' is 1";

        public static final String NO_ITEMS_SELECTED = "no items selected to place order";

        public static final String DELIVERY_ADDRESS_REQUIRED = "'deliveryAddress' is required to place order";

        public static final String ADDRESS_LINE1_REQUIRED = "'addressLine1' is required";
        public static final String ADDRESS_LINE1_BLANK = "'addressLine1' can not be blank";

        public static final String CITY_REQUIRED = "'city' is required";
        public static final String CITY_BLANK = "'city' can not be blank";


        public static final String STATE_REQUIRED = "'state' is required";
        public static final String STATE_BLANK = "'state' can not be blank";

        public static final String PINCODE_REQUIRED = "'pinCode' is required";
        public static final String PINCODE_BLANK = "'pinCode' can not be blank";
        public static final String PINCODE_INVALID = "'pinCode' is invalid";

        public static final String PHONE_NUMBER_REQUIRED = "'phoneNumber' is required";
        public static final String INVALID_PHONE_NUMBER = "'phoneNumber' is not valid";
    }
}
