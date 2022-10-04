package DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;
import utils.Constants;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductOrderDetailDTO {
   @JsonProperty(Constants._ID)
   private String order_id;
   @JsonFormat(pattern=Constants.FORMAT_DATE_JSON)
   private Date date_order;
   private String user_name;
   private String customer_name;
   private String phone;
   private List<ProductOrderDTO> product_order;
   private int total_value;
   private int total_price;
}
