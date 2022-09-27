package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class OrderEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private Date date;
  private String user_id;
  private String customer_id;
  private int total_value;
  private int total_price;
}
