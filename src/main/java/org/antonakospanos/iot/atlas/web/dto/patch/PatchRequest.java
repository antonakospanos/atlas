package org.antonakospanos.iot.atlas.web.dto.patch;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.Valid;
import java.util.List;

public class PatchRequest {

	@ApiModelProperty(example = "2017-11-19T16:52:40.000 UTC")
	private String timestamp;

	@ApiModelProperty(required = true)
	@Valid
	private List<PatchDto> patches;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public List<PatchDto> getPatches() {
		return patches;
	}

	public void setPatches(List<PatchDto> patches) {
		this.patches = patches;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PatchRequest)) return false;

		PatchRequest that = (PatchRequest) o;

		if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;
		return patches != null ? patches.equals(that.patches) : that.patches == null;
	}

	@Override
	public int hashCode() {
		int result = timestamp != null ? timestamp.hashCode() : 0;
		result = 31 * result + (patches != null ? patches.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
