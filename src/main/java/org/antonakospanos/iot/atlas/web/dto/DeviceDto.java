package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DeviceDto
 */
public class DeviceDto implements Dto {

  @JsonProperty("id")
  private String id = null;

  @JsonProperty("uptime")
  private Long uptime = null;

  @JsonProperty("version")
  private String version = null;

  @JsonProperty("modules")
  private List<ModuleDto> modules = null;

  public DeviceDto id(String id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "000000a12bcc", required = true, value = "")
  @NotNull
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public DeviceDto version(String version) {
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @ApiModelProperty(example = "1.0", required = true, value = "")
  @NotNull
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Get uptime
   * @return uptime
   **/
  @ApiModelProperty(example = "10", required = true, value = "")
  @NotNull
  public Long getUptime() {
    return uptime;
  }

  public void setUptime(Long uptime) {
    this.uptime = uptime;
  }

  public DeviceDto modules(List<ModuleDto> modules) {
    this.modules = modules;
    return this;
  }

  public DeviceDto addModulesItem(ModuleDto modulesItem) {
    if (this.modules == null) {
      this.modules = new ArrayList<ModuleDto>();
    }
    this.modules.add(modulesItem);
    return this;
  }

   /**
   * Get modules
   * @return modules
  **/
  @ApiModelProperty(value = "")
  @Valid
  public List<ModuleDto> getModules() {
    return modules;
  }

  public void setModules(List<ModuleDto> modules) {
    this.modules = modules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeviceDto device = (DeviceDto) o;
    return Objects.equals(this.id, device.id) &&
        Objects.equals(this.version, device.version) &&
        Objects.equals(this.modules, device.modules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, version, modules);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
  }

  @Override
  public Dto fromEntity(Object entity) {
    return null;
  }

  @Override
  public Object toEntity() {
    return null;
  }
}

