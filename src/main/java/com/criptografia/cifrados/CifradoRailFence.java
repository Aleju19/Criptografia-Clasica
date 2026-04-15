package com.criptografia.cifrados;

import com.criptografia.inter.Cifrado;

import java.util.Arrays;

public class CifradoRailFence implements Cifrado {

    @Override
    public String cifrar(String texto, String clave) {
        int rieles = obtenerRieles(clave);
        if (rieles <= 1 || texto.isEmpty())
            throw new IllegalArgumentException("El Rail Fence necesita un número de rieles.");

        // Creamos un StringBuilder para cada "riel"
        StringBuilder[] arrayRieles = new StringBuilder[rieles];
        for (int i = 0; i < rieles; i++) {
            arrayRieles[i] = new StringBuilder();
        }

        int rielActual = 0;
        boolean bajando = false;

        // Recorremos el texto y lo repartimos en los rieles en zigzag
        for (char c : texto.toCharArray()) {
            arrayRieles[rielActual].append(c);

            // Si tocamos el riel superior (0) o el inferior, cambiamos de dirección
            if (rielActual == 0 || rielActual == rieles - 1) {
                bajando = !bajando;
            }

            rielActual += bajando ? 1 : -1;
        }

        // Unimos todos los rieles fila por fila
        StringBuilder resultado = new StringBuilder();
        for (StringBuilder riel : arrayRieles) {
            resultado.append(riel);
        }

        return resultado.toString();
    }

    @Override
    public String descifrar(String texto, String clave) {
        int rieles = obtenerRieles(clave);
        if (rieles <= 1 || texto.isEmpty())
            throw new IllegalArgumentException("El Rail Fence necesita un número de rieles.");
        char[][] matriz = new char[rieles][texto.length()];
        for (int i = 0; i < rieles; i++) {
            Arrays.fill(matriz[i], '\n'); // Llenamos con un salto de línea como "vacío"
        }

        // Paso 1: Marcar el camino del zigzag con un '*'
        boolean bajando = false;
        int fila = 0, col = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (fila == 0 || fila == rieles - 1) bajando = !bajando;
            matriz[fila][col++] = '*';
            fila += bajando ? 1 : -1;
        }

        // Paso 2: Llenar los '*' con las letras del texto cifrado (fila por fila)
        int indexLetra = 0;
        for (int i = 0; i < rieles; i++) {
            for (int j = 0; j < texto.length(); j++) {
                if (matriz[i][j] == '*' && indexLetra < texto.length()) {
                    matriz[i][j] = texto.charAt(indexLetra++);
                }
            }
        }

        // Paso 3: Leer la matriz siguiendo el zigzag para obtener el texto original
        StringBuilder resultado = new StringBuilder();
        bajando = false;
        fila = 0;
        col = 0;
        for (int i = 0; i < texto.length(); i++) {
            if (fila == 0 || fila == rieles - 1) bajando = !bajando;
            resultado.append(matriz[fila][col++]);
            fila += bajando ? 1 : -1;
        }

        return resultado.toString();
    }

    // Método auxiliar para validar que la clave sea un número válido
    private int obtenerRieles(String clave) {
        try {
            if (clave == null || clave.trim().isEmpty()) {
                throw new IllegalArgumentException("El Rail Fence necesita un número de rieles.");
            }
            int n = Integer.parseInt(clave.trim());
            if (n <= 1) {
                throw new IllegalArgumentException("El número de rieles debe ser mayor a 1.");
            }
            return n;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La clave para Rail Fence debe ser un número entero (ej. 3).");
        }
    }
}
