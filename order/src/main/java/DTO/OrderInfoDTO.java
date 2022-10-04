package DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderInfoDTO {
    private String user_id;
    private String customer_id;
    private List<ProductOrderDTO> product_order;
}
