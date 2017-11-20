package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.web.enums.Result;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * HeartbeatSuccessResponse
 */
public class HeartbeatSuccessResponse   {

  @JsonProperty("result")
  private Result result = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("data")
  private Object data = null;

  public HeartbeatSuccessResponse result(Result result) {
    this.result = result;
    return this;
  }

   /**
   * Get result
   * @return result
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }

  public HeartbeatSuccessResponse description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public HeartbeatSuccessResponse data(Object data) {
    this.data = data;
    return this;
  }

   /**
   * Get data
   * @return data
  **/
  @ApiModelProperty(value = "")
  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeartbeatSuccessResponse heartbeatSuccessResponse = (HeartbeatSuccessResponse) o;
    return Objects.equals(this.result, heartbeatSuccessResponse.result) &&
        Objects.equals(this.description, heartbeatSuccessResponse.description) &&
        Objects.equals(this.data, heartbeatSuccessResponse.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(result, description, data);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}

