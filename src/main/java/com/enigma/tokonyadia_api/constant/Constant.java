package com.enigma.tokonyadia_api.constant;

public class Constant {
    public static final String APP_NAME = "Tokonyadia";
    public static final String API = "/api";

    // Customer related APIs
    public static final String CUSTOMER_API = API + "/customers";

    // Product related APIs
    public static final String PRODUCT_API = API + "/products";
    public static final String PRODUCT_CATEGORIES_API = PRODUCT_API + "/categories";

    // Store related APIs
    public static final String STORE_API = API + "/stores";

    // Authentication and User related APIs
    public static final String AUTH_API = API + "/auth";
    public static final String USER_API = API + "/users";

    // Seller related APIs
    public static final String SELLER_API = API + "/seller";

    // Order related APIs
    public static final String ORDER_API = API + "/orders";

    // Payment related APIs
    public static final String PAYMENT_API = API + "/payments";

    // Image and File related APIs
    public static final String IMAGE_API = API + "/images";


    public static final String SUCCESS_CREATE_PRODUCT = "Successfully created new product";
    public static final String SUCCESS_GET_ALL_PRODUCT = "Successfully retrieved all products";
    public static final String SUCCESS_UPDATE_PRODUCT = "Successfully update product";
    public static final String SUCCESS_GET_PRODUCT_BY_ID = "Successfully retrieve product by id";
    public static final String SUCCESS_DELETE_PRODUCT = "Successfully delete product";

    public static final String SUCCESS_CREATE_STORE = "Successfully created new store";
    public static final String SUCCESS_GET_ALL_STORE = "Successfully retrieved all store";
    public static final String SUCCESS_UPDATE_STORE = "Successfully update store";
    public static final String SUCCESS_GET_STORE_BY_ID = "Successfully retrieve store by id";
    public static final String SUCCESS_DELETE_STORE = "Successfully delete store";

    public static final String SUCCESS_CREATE_CUSTOMER = "Successfully created new customer";
    public static final String SUCCESS_GET_ALL_CUSTOMER = "Successfully retrieved all customer";
    public static final String SUCCESS_UPDATE_CUSTOMER = "Successfully update customer";
    public static final String SUCCESS_GET_CUSTOMER_BY_ID = "Successfully retrieve customer by id";
    public static final String SUCCESS_DELETE_CUSTOMER = "Successfully delete customer";

    public static final String SUCCESS_GET_ORDER_BY_ID = "Successfully retrieve order";
    public static final String SUCCESS_GET_ALL_ORDER = "Successfully retrieved all order";
    public static final String SUCCESS_UPDATE_ORDER = "Successfully update order";
    public static final String SUCCESS_REMOVE_ORDER_DETAIL = "Successfully remove order details";
    public static final String SUCCESS_GET_ORDER_DETAIL = "Successfully retrieve order detail";
    public static final String SUCCESS_CHECKOUT_ORDER = "Successfully checkout order";
    public static final String SUCCESS_UPDATE_ORDER_STATUS = "Successfully update order status";
    public static final String SUCCESS_ADD_ITEMS_TO_ORDER = "Successfully add items to order";

    public static final String ERROR_ORDER_NOT_FOUND = "Error order Not Found";
    public static final String ERROR_ORDER_DETAIL_NOT_FOUND = "Error order detail not found";

    public static final String SUCCESS_REGISTER_USER = "Successfully register user";
    public static final String SUCCESS_GET_CURRENT_USER_INFO = "Successfully retrieve current user info";

    public static final String SUCCESS_AUTH_LOGIN_USER = "Successfully logged in";

    public static final String SUCCESS_CREATE_SELLER = "Successfully create seller";
    public static final String SUCCESS_GET_ALL_SELLER = "Successfully retrieve all seller";
    public static final String SUCCESS_UPDATE_SELLER = "Successfully update seller";
    public static final String SUCCESS_DELETE_SELLER = "Successfully delete seller";
    public static final String ERROR_SELLER_NOT_FOUND = "Error seller is not found";

    public static final String ERROR_CATEGORY_NOT_FOUND = "Error category is not found";
    public static final String ERROR_STORE_NOT_FOUND = "Error store is not found";

    public static final String ERROR_CREATE_JWT = "Error creating JWT";
    public static final String ERROR_USERNAME_DUPLICATE = "Error username is already exist";

    public static final String ERROR_ROLE_NOT_FOUND = "Error role is not found";
    public static final String ERROR_USER_NOT_FOUND = "Error user is not found";
    public static final String ERROR_USERNAME_NOT_FOUND = "Error username is not found";
    public static final String ERROR_PRODUCT_NOT_FOUND = "Error product is not found";

    public static final String ERROR_CUSTOMER_NOT_FOUND = "Error customer is not found";

    public static final String ERROR_ADD_ITEMS_NON_DRAFT = "Can only add items to draft order";
    public static final String ERROR_UPDATE_ITEMS_NON_DRAFT = "Error update items non draft";
    public static final String ERROR_REMOVE_ITEMS_FROM_NON_DRAFT = "Error remove items from items non draft";
    public static final String ERROR_CHECKOUT_ITEM_FROM_NON_DRAFT = "Error checkout items non draft";
    public static final String ERROR_INSUFFICIENT_STOCK = "Error insufficient stock";

    public static final String SUCCESS_UPDATE_PASSWORD = "Successfully updated password";
    public static final String ERROR_INVALID_CREDENTIAL = "Invalid credential";
    public static final String ERROR_INVALID_JWT_TOKEN = "Invalid JWT token. Please provide a valid token.";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh token is required";
    public static final String SUCCESS_REFRESH_TOKEN = "Successfully refresh token generated";


    public static final String SUCCESS_DELETE_PRODUCT_IMAGE = "Successfully deleted product image";
    public static final String SUCCESS_UPDATE_PRODUCT_IMAGE = "Successfully updated product image";
    public static final String ERROR_SAVE_IMAGE = "Error while save image";
    public static final String ERROR_IMAGE_NOT_FOUND = "Error image not found";
    public static final String ERROR_IMAGE_CANNOT_BE_EMPTY  = "image cannot be empty";
    public static final String ERROR_FILENAME_CANNOT_BE_EMPTY = "filename cannot be empty";
    public static final String ERROR_FILE_LIMIT = "file size exceed limit";
    public static final String ERROR_INVALID_FILE_EXTENSION = "invalid extensions type";
    public static final String ERROR_WHILE_INIT_DIRECTORY = "Error while init directory";

    public static final String ERROR_PAYMENT_NOT_FOUND = "Error payment order not found";
    public static final String ERROR_INVALID_SIGNATURE_KEY_MIDTRANS = "invalid signature key";

    public static final String OK = "OK";
    public static final String SYSTEM = "SYSTEM";

}
