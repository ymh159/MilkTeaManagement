package entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderEntity {

  @JsonProperty(Constants._ID)
  private String id;
  @JsonFormat(pattern=Constants.FORMAT_DATE_JSON)
  private Date date_order;
  private String user_id;
  private String customer_id;
  private int total_quantity;
  private int total_price;

}
