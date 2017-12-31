package org.antonakospanos.iot.atlas.web.dto.accounts;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * AccountBaseDto
 */
@JsonPropertyOrder({ "username", "password", "name", "email", "cellphone", "devices" })
public class AccountBaseDto {

	@NotEmpty
	@ApiModelProperty(example = "ckar", required = true)
	private String username;

	/**
	 * Hashed password
	 */
	@NotEmpty
	@ApiModelProperty(example = "password", required = true)
	private String password;

	@NotEmpty
	@ApiModelProperty(example = "kostas@carouzos.com", required = true)
	private String email;

	@ApiModelProperty(example = "Kostas Carouzos")
	private String name;

	@ApiModelProperty(example = "00306941234567")
	private String cellphone;

	@ApiModelProperty(allowableValues = "deviceId")
	private List<String> devices;


	public AccountBaseDto() {
	}

	public AccountBaseDto(String username, String password, String name, String email, String cellphone, List<String> devices) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.cellphone = cellphone;
		this.devices = devices;
	}

	// Factory methods
	public AccountBaseDto username(String username) {
		this.username = username;
		return this;
	}

	public AccountBaseDto password(String password) {
		this.password = password;
		return this;
	}

	public AccountBaseDto name(String name) {
		this.name = name;
		return this;
	}

	public AccountBaseDto email(String email) {
		this.email = email;
		return this;
	}

	public AccountBaseDto cellphone(String cellphone) {
		this.cellphone = cellphone;
		return this;
	}

	public AccountBaseDto devices(List<String> devices) {
		this.devices= devices;
		return this;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public List<String> getDevices() {
		return devices;
	}

	public void setDevices(List<String> devices) {
		this.devices = devices;
	}
}
