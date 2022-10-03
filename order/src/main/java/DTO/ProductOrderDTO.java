package DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProductOrderDTO {
    private String product_id;
    private String name;
    private int value;
    private int price;
}
