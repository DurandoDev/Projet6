package com.openclassrooms.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "connections")
public class Connections {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	User user;

	@ManyToOne
	User owner;

	public Connections(User user, User owner) {
		this.user = user;
		this.owner = owner;
	}

	public Connections() {

	}
}
