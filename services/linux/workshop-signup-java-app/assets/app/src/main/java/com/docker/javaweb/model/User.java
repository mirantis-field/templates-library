package com.docker.javaweb.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;

	@NotEmpty
	@Email
	private String emailAddress;

	@NotNull
	@Future
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date workshopDate;

	@NotEmpty
	@Size(min = 4, max = 32)
	private String userName;

	@NotEmpty
	@Size(min = 4, max = 32)
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Date getWorkshopDate() {
		return workshopDate;
	}

	public void setWorkshopDate(Date workshopDate) {
		this.workshopDate = workshopDate;
	}

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", userName='" + userName + '\'' + ", firstName='" + firstName + '\''
				+ ", lastName='" + lastName + '\'' + ", password='" + password + '\'' + ", emailAddress='"
				+ emailAddress + '\'' + ", workshopDate='" + workshopDate + '\'' + '}';
	}
}
