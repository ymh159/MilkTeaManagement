package DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import utils.Constants;

@JsonIgnoreProperties(ignoreUnknown=true)
@Data
public class ProductDetailDTO {
  @JsonProperty(Constants._ID)
  private String id;
  private String name;
  private String category_name;
  private String brand;
  private int cost;
  private int price;
  private int quantity;
  private String description;
  private String provider_name;
  private String provider_address;
  private String provider_email;
}
