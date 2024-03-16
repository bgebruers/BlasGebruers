package com.mycompany.porkycakes.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Admin {
	private String cuil;
	private String nombre;
	private String mail;
	private String telefono;
	private String password;
}
