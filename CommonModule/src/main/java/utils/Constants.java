package utils;

public class Constants {
  public static final String CONFIG_MONGO_HOST_KEY = "connection_string";
  public static final String CONFIG_MONGO_HOST_VALUE = "mongodb://localhost:27017";
  public static final String CONFIG_MONGO_DB_NAME_KEY = "db_name";
  public static final String CONFIG_MONGO_DB_NAME_VALUE = "milk_tea";
  public static final String COLLECTION_CUSTOMER = "customer";
  public static final String COLLECTION_ORDERDETAIL = "orderdetail";
  public static final String COLLECTION_ORDER = "order";
  public static final String COLLECTION_PRODUCTCATEGORY = "productcategory";
  public static final String COLLECTION_PRODUCT = "product";
  public static final String COLLECTION_PROVIDER = "provider";
  public static final String COLLECTION_USER = "user";
  public static final String _ID = "_id";
  public static final String ID = "id";
  public static final String PATH_ID="/:id";

  // message
  public static final String MESSAGE_INSERT_SUCCESS = "Insert Success";
  public static final String MESSAGE_DELETE_SUCCESS = "Delete Success";
  public static final String MESSAGE_UPDATE_SUCCESS = "Update Success";
  public static final String MESSAGE_INSERT_FAIL = "Insert Fail";
  public static final String MESSAGE_DELETE_FAIL = "Delete Fail";
  public static final String MESSAGE_UPDATE_FAIL = "Update Fail";

  // eventBus address
  public static final String ADDRESS_EB_GET_CUSTOMER =  "get.customer";
  public static final String ADDRESS_EB_GET_ORDER_DETAIL =  "get.order_detail";
  public static final String ADDRESS_EB_GET_ORDER =  "get.order";
  public static final String ADDRESS_EB_GET_PRODUCT_CATEGORY =  "get.product_category";
  public static final String ADDRESS_EB_GET_PRODUCT =  "get.product";
  public static final String ADDRESS_EB_GET_PROVIDER =  "get.provider";
  public static final String ADDRESS_EB_GET_USER =  "get.user";
}
