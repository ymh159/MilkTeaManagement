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
public class ProviderEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String provider_name;
  private String provider_address;
  private String provider_email;
}
