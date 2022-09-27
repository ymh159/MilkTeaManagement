package entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import utils.Constants;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class UserEntity {

  @JsonProperty(Constants._ID)
  private String id;
  private String user_name;
  private String pass;
  private String role;
}
