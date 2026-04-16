package com.criptografia;

import com.criptografia.factory.CifradoFactory;
import com.criptografia.inter.Cifrado;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class AppView extends Application {

    private Label lblDescripcion;
    private TextField campoClave;

    @Override
    public void start(Stage primaryStage) {
        // --- CONTENEDOR PRINCIPAL (FONDO BLANCO) ---
        VBox root = new VBox(25);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #ffffff; -fx-font-family: 'Arial';");

        // --- TÍTULO ---
        Label lblTitulo = new Label("SISTEMA DE CRIPTOGRAFÍA CLÁSICA");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        lblTitulo.setStyle("-fx-text-fill: #2c3e50;");

        // --- PANEL DE CONFIGURACIÓN ---
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        Label lblTipo = new Label("Seleccione Algoritmo:");
        lblTipo.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        ComboBox<String> comboCifrados = new ComboBox<>();
        comboCifrados.getItems().addAll("César", "Atbash", "Vigenère", "Rail Fence", "Playfair");
        comboCifrados.setValue("César");
        comboCifrados.setStyle("-fx-font-size: 14px; -fx-padding: 5;");
        comboCifrados.setPrefWidth(250);

        Label lblClave = new Label("Clave de Cifrado:");
        lblClave.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        campoClave = new TextField();
        campoClave.setPromptText("Ej: 3");
        campoClave.setFont(Font.font("Arial", 14));
        campoClave.setStyle("-fx-padding: 8; -fx-border-color: #bdc3c7; -fx-border-radius: 5;");

        grid.add(lblTipo, 0, 0);
        grid.add(comboCifrados, 1, 0);
        grid.add(lblClave, 0, 1);
        grid.add(campoClave, 1, 1);

        // --- ÁREA DE DESCRIPCIÓN ---
        lblDescripcion = new Label("El cifrado César desplaza cada letra un número fijo de posiciones en el abecedario.");
        lblDescripcion.setWrapText(true);
        lblDescripcion.setMaxWidth(800);
        lblDescripcion.setMinHeight(60);
        lblDescripcion.setAlignment(Pos.CENTER);
        lblDescripcion.setFont(Font.font("Arial", 15));
        lblDescripcion.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 15; -fx-border-color: #e9ecef; -fx-border-radius: 10; -fx-text-fill: #505e6c;");

        // Evento para cambiar descripción
        comboCifrados.setOnAction(e -> actualizarInfo(comboCifrados.getValue()));

        // --- ÁREAS DE TEXTO (TEXTO MÁS GRANDE) ---
        TextArea txtOrigen = crearTextArea("Escriba el texto original aquí...");
        TextArea txtDestino = crearTextArea("El resultado aparecerá aquí...");
        txtDestino.setEditable(false);
        txtDestino.setStyle(txtDestino.getStyle() + "-fx-background-color: #fdfdfd;");

        HBox panelTextos = new HBox(25,
                crearContenedorConEtiqueta("TEXTO DE ENTRADA", txtOrigen),
                crearContenedorConEtiqueta("RESULTADO", txtDestino)
        );
        panelTextos.setAlignment(Pos.CENTER);
        HBox.setHgrow(txtOrigen, Priority.ALWAYS);
        HBox.setHgrow(txtDestino, Priority.ALWAYS);

        // --- BOTONES ---
        Button btnCifrar = new Button("CIFRAR MENSAJE");
        Button btnDescifrar = new Button("DESCIFRAR MENSAJE");
        Button btnLimpiar = new Button("LIMPIAR");

        configurarBoton(btnCifrar, "#3498db");
        configurarBoton(btnDescifrar, "#2ecc71");
        configurarBoton(btnLimpiar, "#95a5a6");

        HBox panelBotones = new HBox(20, btnCifrar, btnDescifrar, btnLimpiar);
        panelBotones.setAlignment(Pos.CENTER);

        // --- LÓGICA ---
        btnCifrar.setOnAction(e -> procesarTexto(comboCifrados, campoClave, txtOrigen, txtDestino, true));
        btnDescifrar.setOnAction(e -> procesarTexto(comboCifrados, campoClave, txtOrigen, txtDestino, false));
        btnLimpiar.setOnAction(e -> {
            txtOrigen.clear();
            txtDestino.clear();
            campoClave.clear();
        });

        root.getChildren().addAll(lblTitulo, grid, lblDescripcion, panelTextos, panelBotones);

        Scene scene = new Scene(root, 1000, 750);
        primaryStage.setTitle("Herramienta de Criptografía Pro - David");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void actualizarInfo(String algoritmo) {
        campoClave.setDisable(false);
        switch (algoritmo) {
            case "César":
                lblDescripcion.setText("Cifrado César: Algoritmo de sustitución simple donde cada letra se desplaza N posiciones. Requiere un número entero como clave.");
                campoClave.setPromptText("Ej: 3");
                break;
            case "Atbash":
                lblDescripcion.setText("Cifrado Atbash: Método de sustitución donde la 'A' se convierte en 'Z', la 'B' en 'Y', etc. Es un cifrado inverso y no requiere clave.");
                campoClave.setPromptText("No requiere clave");
                campoClave.setDisable(true);
                break;
            case "Vigenère":
                lblDescripcion.setText("Cifrado Vigenère: Cifrado polialfabético que utiliza una palabra clave para aplicar diferentes desplazamientos César en cada letra.");
                campoClave.setPromptText("Ej: PALABRA");
                break;
            case "Rail Fence":
                lblDescripcion.setText("Cifrado Rail Fence (Zig-Zag): Cifrado de transposición que escribe el mensaje en forma de zigzag en N niveles o rieles.");
                campoClave.setPromptText("Número de rieles (Ej: 3)");
                break;
            case "Playfair":
                lblDescripcion.setText("Cifrado Playfair: Cifra pares de letras (digramas) usando una matriz de 5x5 construida a partir de una palabra clave.");
                campoClave.setPromptText("Ej: CLAVE");
                break;
        }
    }

    private VBox crearContenedorConEtiqueta(String titulo, TextArea ta) {
        Label lbl = new Label(titulo);
        lbl.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        lbl.setStyle("-fx-text-fill: #7f8c8d;");
        VBox vbox = new VBox(8, lbl, ta);
        VBox.setVgrow(ta, Priority.ALWAYS);
        return vbox;
    }

    private TextArea crearTextArea(String prompt) {
        TextArea ta = new TextArea();
        ta.setPromptText(prompt);
        ta.setWrapText(true);
        ta.setFont(Font.font("Monospaced", 16)); // Letra más grande y clara
        ta.setStyle("-fx-border-color: #dcdde1; -fx-border-radius: 5; -fx-background-radius: 5;");
        return ta;
    }

    private void configurarBoton(Button btn, String colorHex) {
        btn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        btn.setPrefHeight(45);
        btn.setPrefWidth(180);
        btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 25; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: derive(" + colorHex + ", -10%); -fx-text-fill: white; -fx-background-radius: 25;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: " + colorHex + "; -fx-text-fill: white; -fx-background-radius: 25;"));
    }

    private void procesarTexto(ComboBox<String> combo, TextField clave, TextArea origen, TextArea destino, boolean esCifrado) {
        String algoritmo = combo.getValue();
        String texto = origen.getText();
        String llave = clave.getText();

        if (texto.isEmpty()) {
            destino.setText("⚠️ Por favor, ingrese un texto para procesar.");
            return;
        }

        try {
            Cifrado cifrador = CifradoFactory.obtenerCifrado(algoritmo);
            String resultado = esCifrado ? cifrador.cifrar(texto, llave) : cifrador.descifrar(texto, llave);
            destino.setText(resultado);
        } catch (Exception ex) {
            destino.setText("❌ Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}