package org.antonakospanos.iot.atlas.web.dto.accounts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.antonakospanos.iot.atlas.dao.model.Account;
import org.antonakospanos.iot.atlas.web.dto.Dto;

import javax.validation.constraints.NotEmpty;

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


	public AccountDto() {
	}

	public AccountDto(String username, String password, String name, String email, String cellphone) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.cellphone = cellphone;
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

	@Override
	public AccountDto fromEntity(Account account) {
		this.username = account.getUsername();
		this.password = account.getPassword();
		this.name = account.getName();
		this.email = account.getEmail();
		this.cellphone= account.getCellphone();

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

		return account;
	}
}
