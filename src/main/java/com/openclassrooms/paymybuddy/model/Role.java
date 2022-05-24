package com.openclassrooms.paymybuddy.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table (name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;


	public Role(String roleName) {
		this.name = roleName;
	}
}
