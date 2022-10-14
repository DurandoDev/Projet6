package com.openclassrooms.paymybuddy.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "balance")
public class Balance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int amount = 0;

}
