package org.antonakospanos.iot.atlas.web.dto.devices;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({ "version", "modules" })
public class DeviceBaseDto {

	@NotNull
	@ApiModelProperty(example = "1.0", required = true)
	private String version;

	@ApiModelProperty(example = "1000", notes = "The milliseconds since startup")
	private Long uptime;

	@Valid
	private List<ModuleDto> modules;

	public DeviceBaseDto() {
	}

	public DeviceBaseDto(String version, Long uptime, List<ModuleDto> modules) {
		this.version = version;
		this.uptime = uptime;
		this.modules = modules;
	}

	// Factory methods
	public DeviceBaseDto version(String version) {
		this.version = version;
		return this;
	}

	public DeviceBaseDto version(Long uptime) {
		this.uptime = uptime;
		return this;
	}

	public DeviceBaseDto addModulesItem(ModuleDto modulesItem) {
		if (this.modules == null) {
			this.modules = new ArrayList<ModuleDto>();
		}
		this.modules.add(modulesItem);
		return this;
	}

	public DeviceBaseDto modules(List<ModuleDto> modules) {
		this.modules = modules;
		return this;
	}


	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Long getUptime() {
		return uptime;
	}

	public void setUptime(Long uptime) {
		this.uptime = uptime;
	}

	public List<ModuleDto> getModules() {
		return modules;
	}

	public void setModules(List<ModuleDto> modules) {
		this.modules = modules;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DeviceBaseDto deviceDto = (DeviceBaseDto) o;

		if (version != null ? !version.equals(deviceDto.version) : deviceDto.version != null) return false;
		return modules != null ? modules.equals(deviceDto.modules) : deviceDto.modules == null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(version, modules);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
