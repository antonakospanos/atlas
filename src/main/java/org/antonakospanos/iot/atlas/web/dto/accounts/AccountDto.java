package org.antonakospanos.iot.atlas.web.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.web.dto.Dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * AccountDto
 */
@JsonPropertyOrder({ "username", "password", "name", "email", "cellphone" })
public class AccountDto implements Dto<Account> {

	@JsonProperty("username")
	@NotEmpty
	private String username;

	/**
	 * Hashed password
	 */
	@JsonProperty("password")
	@NotEmpty
	private String password;

	@JsonProperty("name")
	private String name;

	@JsonProperty("email")
	@NotEmpty
	private String email;

	@JsonProperty("cellphone")
	private String cellphone;

	private List<String> devices;


	public AccountDto() {
	}

	public AccountDto(String username, String password, String name, String email, String cellphone, List<String> devices) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.cellphone = cellphone;
		this.devices = devices;
	}

	// Factory methods
	public AccountDto username(String username) {
		this.username = username;
		return this;
	}

	public AccountDto password(String password) {
		this.password = password;
		return this;
	}

	public AccountDto name(String name) {
		this.name = name;
		return this;
	}

	public AccountDto email(String email) {
		this.email = email;
		return this;
	}

	public AccountDto cellphone(String cellphone) {
		this.cellphone = cellphone;
		return this;
	}

	public AccountDto devices(List<String> devices) {
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

	@Override
	public AccountDto fromEntity(Account account) {
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.name = account.getName();
		this.email = account.getEmail();
		this.cellphone = account.getCellphone();
		this.devices = account.getDevices()
				.stream()
				.map(device -> device.getExternalId())
				.collect(Collectors.toList());

		return this;
	}

	@Override
	public Account toEntity() {
		Account account = new Account();

		return toEntity(account);
	}

	@Override
	public Account toEntity(Account account) {
		account.setUsername(this.getUsername());
		account.setPassword(this.getPassword());
		account.setName(this.getName());
		account.setEmail(this.getEmail());
		account.setCellphone(this.getCellphone());
		// DAO: Add devices relationship

		return account;
	}
}