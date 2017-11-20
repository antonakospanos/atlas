package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * HeartbeatRequest
 */
public class HeartbeatRequest {

  @JsonProperty("timestamp")
  private String timestamp = null;

  @JsonProperty("device")
  private DeviceDto device = null;

  public HeartbeatRequest timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

   /**
   * Get timestamp
   * @return timestamp
  **/
  @ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC", value = "")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public HeartbeatRequest device(DeviceDto device) {
    this.device = device;
    return this;
  }

   /**
   * Get device
   * @return device
  **/
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public DeviceDto getDevice() {
    return device;
  }

  public void setDevice(DeviceDto device) {
    this.device = device;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HeartbeatRequest heartbeat = (HeartbeatRequest) o;
    return Objects.equals(this.timestamp, heartbeat.timestamp) &&
        Objects.equals(this.device, heartbeat.device);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, device);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}

