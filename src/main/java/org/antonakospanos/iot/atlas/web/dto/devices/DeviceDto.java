package org.antonakospanos.iot.atlas.web.dto.devices;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;
import org.antonakospanos.iot.atlas.dao.model.Device;
import org.antonakospanos.iot.atlas.dao.model.Module;
import org.antonakospanos.iot.atlas.web.dto.Dto;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DeviceDto
 */
@JsonPropertyOrder({ "id", "version", "uptime", "modules" })
public class DeviceDto extends DeviceBaseDto implements Dto<Device> {

	@JsonProperty("id") // access = JsonProperty.Access.READ_ONLY
	@ApiModelProperty(example = "deviceId")
	private String id = null;

	public DeviceDto() {
	}

	public DeviceDto(String id, DeviceBaseDto deviceBaseDto) {
		super(deviceBaseDto.getVersion(), deviceBaseDto.getUptime(), deviceBaseDto.getModules());
		this.id = id;
	}

	public DeviceDto(String id, String version, Long uptime, List<ModuleDto> modules) {
		super(version, uptime, modules);
		this.id = id;
	}

	// Factory methods
	public DeviceDto id(String id) {
		this.id = id;
		return this;
	}

	public DeviceDto version(String version) {
		setVersion(version);
		return this;
	}

	public DeviceDto addModulesItem(ModuleDto modulesItem) {
		if (getModules() == null) {
			setModules(new ArrayList<ModuleDto>());
		}
		getModules().add(modulesItem);
		return this;
	}

	public DeviceDto modules(List<ModuleDto> modules) {
		setModules(modules);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DeviceDto)) return false;
		if (!super.equals(o)) return false;

		DeviceDto deviceDto = (DeviceDto) o;

		return id != null ? id.equals(deviceDto.id) : deviceDto.id == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (id != null ? id.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public DeviceDto fromEntity(Device device) {
		this.id = device.getExternalId();
		setVersion(device.getVersion());
		setUptime(device.getUptime());
		setModules(device.getModules().stream().map(e -> new ModuleDto().fromEntity(e)).collect(Collectors.toList()));

		return this;
	}

	@Override
	public Device toEntity() {
		Device device = new Device();

		return toEntity(device);
	}

	@Override
	public Device toEntity(Device device) {

		device.setExternalId(this.getId());
		device.setVersion(this.getVersion());
		device.setUptime(this.getUptime());
		device.setLastContact(ZonedDateTime.now());

		List<Module> modules =
				this.getModules()
				.stream() //.map(dto -> dto.toEntity())
				.map(dto ->	{
					Module module = dto.toEntity();
					module.setDevice(device);

					return module;
				})
				.collect(Collectors.toList());


		device.setModules(modules);

		return device;
	}
}
