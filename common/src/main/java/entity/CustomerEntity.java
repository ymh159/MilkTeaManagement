package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class CustomerEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String customer_name;
  private String phone;
}
