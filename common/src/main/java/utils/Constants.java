package utils;

public class Constants {
  public static final String CONFIG_MONGO_HOST_KEY = "connection_string";
  public static final String CONFIG_MONGO_HOST_VALUE = "mongodb://localhost:27017";
  public static final String CONFIG_MONGO_DB_NAME_KEY = "db_name";
  public static final String CONFIG_MONGO_DB_NAME_VALUE = "milk_tea";

  // collection name
  public static final String COLLECTION_CUSTOMER = "customer";
  public static final String COLLECTION_ORDER_DETAIL = "order_detail";
  public static final String COLLECTION_ORDER = "order";
  public static final String COLLECTION_PRODUCT_CATEGORY = "product_category";
  public static final String COLLECTION_PRODUCT = "product";
  public static final String COLLECTION_PROVIDER = "provider";
  public static final String COLLECTION_USER = "user";


  public static final String _ID = "_id";
  public static final String ID = "id";
  public static final String PROVIDER_ID = "provider_id";
  public static final String PRODUCT_CATEGORY_ID = "product_category_id";
  public static final String PATH_ID="/:id";
  public static final String BLANK = "";
  public static final String DOCUMENT_SET = "$set";
  public static final String JSON_UPDATE = "json_update";

  // message
  public static final String MESSAGE_INSERT_SUCCESS = "Insert Success";
  public static final String MESSAGE_DELETE_SUCCESS = "Delete Success";
  public static final String MESSAGE_UPDATE_SUCCESS = "Update Success";
  public static final String MESSAGE_INSERT_FAIL = "Insert Fail";
  public static final String MESSAGE_DELETE_FAIL = "Delete Fail";
  public static final String MESSAGE_UPDATE_FAIL = "Update Fail";
  public static final String MESSAGE_GET_FAIL = "Get Data Fail";

  // path
  public static final String PATH_CUSTOMER= "/api/customer";
  public static final String PATH_ORDER_DETAIL= "/api/order_detail";
  public static final String PATH_ORDER= "/api/order";
  public static final String PATH_PRODUCT_CATEGORY= "/api/product_category";
  public static final String PATH_PRODUCT= "/api/product";
  public static final String PATH_PROVIDER= "/api/provider";
  public static final String PATH_USER= "/api/user";
  public static final String PATH_GET_PRODUCT_DETAIL= "/api/product_detail";

  //content type
  public static final String CONTENT_TYPE = "content-type";
  public static final String CONTENT_VALUE_JSON = "application/json ; charset=utf-8";
  public static final String LOGGER_ADDRESS_AND_MESSAGE ="eb-address: {}, message: {}";
}
