package com.meli.Models;

import lombok.Getter;

@Getter
public enum EnumError {
	
	ERROR_M0000("ERROR M0000: Error inesperado."), 
	ERROR_M0001("ERROR M0001: No se pudo generar la URL corta."),
	ERROR_M0002("ERROR M0002: La url ya fue acortada."),
	ERROR_M0003("ERROR M0003: Ocurrió un error de base de datos."), 
	ERROR_M0004("ERROR M0004: La URL ingresada no es válida."), 
	ERROR_M0005("ERROR M0005: La URL corta no fue encontrada."), 
	ERROR_M0006("ERROR M0006: La URL ya fue acortada y dadad de baja, se procede a habilitaarla nuevamente."), 
	ERROR_M0007("ERROR M0007: La URL ya fue dada de baja antes."), 
	ERROR_M0008("ERROR M0008: Hubo un error parseando la fecha."), 
	ERROR_M0009("ERROR M0009: El ID está repetido.");

	EnumError(String string) {
		// TODO Auto-generated constructor stub
	}
	
}
