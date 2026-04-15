package com.criptografia.cifrados;

import com.criptografia.inter.Cifrado;

public class CifradoPlayfair implements Cifrado {

    @Override
    public String cifrar(String texto, String clave) {
        if (clave == null || clave.trim().isEmpty())
            throw new IllegalArgumentException("Playfair necesita una palabra clave (solo letras).");
        char[][] matriz = generarMatriz(clave);
        String textoPreparado = prepararTexto(texto);
        return procesar(textoPreparado, matriz, true);
    }

    @Override
    public String descifrar(String texto, String clave) {
        if (clave == null || clave.trim().isEmpty())
            throw new IllegalArgumentException("Playfair necesita una palabra clave (solo letras).");
        char[][] matriz = generarMatriz(clave);
        // Para descifrar, asumimos que el texto ya está limpio y en pares
        String textoLimpio = texto.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        return procesar(textoLimpio, matriz, false);
    }

    // 1. Genera la matriz 5x5 con la clave
    private char[][] generarMatriz(String clave) {
        char[][] matriz = new char[5][5];
        String abecedario = "ABCDEFGHIKLMNOPQRSTUVWXYZ"; // Nota: No hay 'J'
        String claveLimpia = clave.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

        StringBuilder textoMatriz = new StringBuilder();

        // Primero metemos la clave (sin repetir letras)
        for (char c : claveLimpia.toCharArray()) {
            if (textoMatriz.indexOf(String.valueOf(c)) == -1) {
                textoMatriz.append(c);
            }
        }

        // Luego rellenamos con el resto del abecedario
        for (char c : abecedario.toCharArray()) {
            if (textoMatriz.indexOf(String.valueOf(c)) == -1) {
                textoMatriz.append(c);
            }
        }

        // Pasamos el string a la matriz 5x5
        int index = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matriz[i][j] = textoMatriz.charAt(index++);
            }
        }
        return matriz;
    }

    // 2. Prepara el texto en pares y agrega 'X' si es necesario
    private String prepararTexto(String texto) {
        String limpio = texto.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");
        StringBuilder preparado = new StringBuilder();

        for (int i = 0; i < limpio.length(); i++) {
            preparado.append(limpio.charAt(i));
            // Si la siguiente letra es igual (ej. LL), metemos una X
            if (i + 1 < limpio.length() && limpio.charAt(i) == limpio.charAt(i + 1)) {
                preparado.append('X');
            } else if (i + 1 < limpio.length()) {
                preparado.append(limpio.charAt(i + 1));
                i++; // Saltamos la siguiente porque ya la metimos al par
            }
        }

        // Si quedó impar, añadimos una X al final
        if (preparado.length() % 2 != 0) {
            preparado.append('X');
        }
        return preparado.toString();
    }

    // 3. Aplica las reglas de movimiento en la matriz
    private String procesar(String texto, char[][] matriz, boolean cifrar) {
        StringBuilder resultado = new StringBuilder();
        int direccion = cifrar ? 1 : -1; // Cifrar suma 1 (derecha/abajo), descifrar resta 1 (izq/arriba)

        for (int i = 0; i < texto.length(); i += 2) {
            char a = texto.charAt(i);
            char b = texto.charAt(i + 1);
            int[] posA = buscarEnMatriz(a, matriz);
            int[] posB = buscarEnMatriz(b, matriz);

            if (posA[0] == posB[0]) {
                // Regla 1: Misma fila -> Mover a la derecha (o izquierda)
                resultado.append(matriz[posA[0]][(posA[1] + direccion + 5) % 5]);
                resultado.append(matriz[posB[0]][(posB[1] + direccion + 5) % 5]);
            } else if (posA[1] == posB[1]) {
                // Regla 2: Misma columna -> Mover hacia abajo (o arriba)
                resultado.append(matriz[(posA[0] + direccion + 5) % 5][posA[1]]);
                resultado.append(matriz[(posB[0] + direccion + 5) % 5][posB[1]]);
            } else {
                // Regla 3: Rectángulo -> Intercambiar columnas
                resultado.append(matriz[posA[0]][posB[1]]);
                resultado.append(matriz[posB[0]][posA[1]]);
            }
        }
        return resultado.toString();
    }

    // Encuentra la coordenada [fila, columna] de una letra
    private int[] buscarEnMatriz(char letra, char[][] matriz) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matriz[i][j] == letra) return new int[]{i, j};
            }
        }
        return new int[]{0, 0};
    }
}
