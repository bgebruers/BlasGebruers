package com.mycompany.porkycakes.model;
import java.util.Map;

import lombok.Data;

@Data
public class Presupuesto {
    private String titulo;
    private String descripcion;
    private Map<String, String> productos; // (k: v) = (nombre: cantidad)
    private String fechaEntrega;

    public boolean isValid() {
        boolean tituloValido = titulo != null && !titulo.trim().isEmpty();
        boolean descripcionValida = descripcion != null && descripcion.length() > 20; 
        boolean productosValidos = !productos.isEmpty();
        boolean fechaEntregaValida = fechaEntrega.length() == 10;

        return tituloValido && descripcionValida && productosValidos && fechaEntregaValida;
    }
}
