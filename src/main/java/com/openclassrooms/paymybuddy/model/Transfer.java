package com.openclassrooms.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "transfer")
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Double amount;

	private String description;

	@ManyToOne
	User sender;

	@ManyToOne
	User receiver;

	public Transfer(int i, String spring_in_action) {
	}
}
