package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@JsonIgnoreProperties(ignoreUnknown=true)
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ProductEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String name;
  private String product_category_id;
  private String provider_id;
  private int cost;
  private int price;
  private int value;
}
