package com.criptografia;

import com.criptografia.inter.Cifrado;
import com.criptografia.factory.CifradoFactory;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AppView extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Controles Superiores
        ComboBox<String> comboCifrados = new ComboBox<>();
        comboCifrados.getItems().addAll("Cifrado César", "Cifrado Atbash", "Cifrado Vigenere", "Cifrado Rail Fence", "Cifrado Playfair");
        comboCifrados.setValue("César");

        TextField campoClave = new TextField();
        campoClave.setPromptText("Ingresa un número (ej. 3)");

        Label lblDescripcion = new Label("Desplaza las letras un número fijo de posiciones en el abecedario.");
        lblDescripcion.setWrapText(true);
        lblDescripcion.setStyle("-fx-font-style: italic; -fx-text-fill: gray;");

        comboCifrados.getSelectionModel().selectedItemProperty().addListener((obs, viejo, nuevo) -> {
            campoClave.clear();
            campoClave.setDisable(false);

            switch (nuevo) {
                case "César":
                    lblDescripcion.setText("Desplaza las letras un número fijo de posiciones en el abecedario.");
                    campoClave.setPromptText("Ingresa un número (ej. 3)");
                    break;
                case "Atbash":
                    lblDescripcion.setText("Invierte el abecedario (A->Z, B->Y). No requiere clave.");
                    campoClave.setPromptText("Este cifrado no usa clave");
                    campoClave.setDisable(true);
                    break;
                case "Vigenère":
                    lblDescripcion.setText("Usa una palabra clave para aplicar múltiples cifrados César.");
                    campoClave.setPromptText("Ingresa una palabra clave (solo letras)");
                    break;
                case "Rail Fence":
                    lblDescripcion.setText("Escribe el texto en zigzag sobre varios rieles imaginarios.");
                    campoClave.setPromptText("Ingresa el número de rieles (ej. 3)");
                    break;
                case "Playfair":
                    lblDescripcion.setText("Cifra pares de letras usando una matriz 5x5 basada en una clave.");
                    campoClave.setPromptText("Ingresa una palabra clave");
                    break;
            }
        });

        TextArea txtOrigen = new TextArea();
        txtOrigen.setPromptText("Escribe aquí el texto...");
        txtOrigen.setPrefRowCount(10);

        TextArea txtDestino = new TextArea();
        txtDestino.setPromptText("Aquí aparecerá el resultado...");
        txtDestino.setPrefRowCount(10);
        txtDestino.setEditable(false);

        // Layout de los textos uno al lado del otro
        HBox panelTextos = new HBox(15, txtOrigen, txtDestino);
        HBox.setHgrow(txtOrigen, Priority.ALWAYS);
        HBox.setHgrow(txtDestino, Priority.ALWAYS);

        // Botones
        Button btnCifrar = new Button("Cifrar Texto");
        Button btnDescifrar = new Button("Descifrar Texto");

        // Damos un poco de estilo a los botones para que se vean modernos
        btnCifrar.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        btnDescifrar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox panelBotones = new HBox(15, btnCifrar, btnDescifrar);

        // Eventos de los Botones
        btnCifrar.setOnAction(e -> procesarTexto(comboCifrados, campoClave, txtOrigen, txtDestino, true));
        btnDescifrar.setOnAction(e -> procesarTexto(comboCifrados, campoClave, txtOrigen, txtDestino, false));

        // Contenedor Principal
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                new Label("Selecciona el Algoritmo:"), comboCifrados,
                lblDescripcion, campoClave,
                panelTextos, panelBotones
        );

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setTitle("Laboratorio de Criptografía Clásica");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Método auxiliar para no repetir código en los dos botones
    private void procesarTexto(ComboBox<String> combo, TextField clave, TextArea origen, TextArea destino, boolean esCifrado) {
        String algoritmoSeleccionado = combo.getValue();
        String texto = origen.getText();
        String llave = clave.getText();

        if (texto == null || texto.trim().isEmpty()) {
            destino.setText("Por favor, ingresa un texto para procesar.");
            return;
        }

        try {
            Cifrado cifrado = CifradoFactory.obtenerCifrado(algoritmoSeleccionado);

            String resultado;
            if (esCifrado) {
                resultado = cifrado.cifrar(texto, llave);
            } else {
                resultado = cifrado.descifrar(texto, llave);
            }

            destino.setText(resultado);

        } catch (Exception ex) {
            destino.setText("Error al procesar: " + ex.getMessage() + "\nVerifica que la clave sea correcta para este algoritmo.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}