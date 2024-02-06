/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.talabarteria;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 *
 * @author usuario
 */
public class Alertas {
    //alertas varias
    public void mostrarAlerta(String titulo, String contenido, String tipoAlerta) {
        Alert.AlertType alertType = Alert.AlertType.valueOf(tipoAlerta);
        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    public boolean mostrarAlertaConfirmacion(String titulo, String contenido, String tipoAlerta) {
        Alert.AlertType alertType = Alert.AlertType.valueOf(tipoAlerta);
        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);

        ButtonType buttonTypeAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alerta.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

        Optional<ButtonType> resultado = alerta.showAndWait();

        return resultado.orElse(buttonTypeCancelar) == buttonTypeAceptar;
    }
}
