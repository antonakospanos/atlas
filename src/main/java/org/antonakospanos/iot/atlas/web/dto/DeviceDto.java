package org.antonakospanos.iot.atlas.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DeviceDto
 */
public class DeviceDto implements Dto<Device, String> {

	@JsonProperty("id")
	private String id = null;

	@JsonProperty("uptime")
	private Long uptime = null;

	@JsonProperty("version")
	private String version = null;

	@JsonProperty("modules")
	private List<ModuleDto> modules = null;

	public DeviceDto() {
	}

	public DeviceDto(String id, Long uptime, String version, List<ModuleDto> modules) {
		this.id = id;
		this.uptime = uptime;
		this.version = version;
		this.modules = modules;
	}

	// Factory methods
	public DeviceDto id(String id) {
		this.id = id;
		return this;
	}

	public DeviceDto version(String version) {
		this.version = version;
		return this;
	}

	public DeviceDto addModulesItem(ModuleDto modulesItem) {
		if (this.modules == null) {
			this.modules = new ArrayList<ModuleDto>();
		}
		this.modules.add(modulesItem);
		return this;
	}

	public DeviceDto modules(List<ModuleDto> modules) {
		this.modules = modules;
		return this;
	}

	/**
	 * Get id
	 *
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

	/**
	 * Get version
	 *
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
	 *
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

	/**
	 * Get modules
	 *
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
	public int hashCode() {
		return Objects.hash(id, version, modules);
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public DeviceDto fromEntity(Device device) {
		this.id = device.getExternalId();
		this.version = device.getVersion();
		this.modules = device.getModules().stream().map(e -> new ModuleDto().fromEntity(e)).collect(Collectors.toList());

		return this;
	}

	@Override
	public Device toEntity() {
		Device device = new Device();

		device.setExternalId(this.getId());
		device.setVersion(this.getVersion());
		device.setLastContact(ZonedDateTime.ofInstant(Instant.ofEpochMilli(this.getUptime().longValue()), ZoneId.systemDefault()));

		List<Module> modules = this.getModules()
				.stream()
				.map(dto -> {
					Module module = dto.toEntity();
					module.setDevice(device);

					return module;
				})
				.collect(Collectors.toList());

		device.setModules(modules);

		return device;
	}
}
