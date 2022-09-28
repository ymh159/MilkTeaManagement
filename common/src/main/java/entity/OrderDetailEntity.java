package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OrderDetailEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String order_id;
  private String product_id;
  private String product_value;
  private String product_price;
}
