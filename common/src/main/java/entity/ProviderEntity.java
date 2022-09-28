package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class ProviderEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String name;
  private String address;
  private String email;
}
