package com.criptografia.cifrados;

import com.criptografia.inter.Cifrado;

public class CifradoVigenere implements Cifrado {

    @Override
    public String cifrar(String texto, String clave) {
        return procesar(texto, clave, true);
    }

    @Override
    public String descifrar(String texto, String clave) {
        return procesar(texto, clave, false);
    }

    private String procesar(String texto, String clave, boolean esCifrado) {
        StringBuilder resultado = new StringBuilder();
        // Nos aseguramos de que la clave esté en mayúsculas y sin espacios
        String claveLimpia = clave.toUpperCase().replaceAll("[^A-Z]", "");

        if (claveLimpia.isEmpty()) {
            throw new IllegalArgumentException("Vigenère necesita una palabra clave (solo letras).");
        }

        int indiceClave = 0;

        for (char c : texto.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                // Posición de la letra del mensaje (0-25)
                int posLetra = c - 'A';
                // Posición de la letra de la clave (0-25) que dicta el desplazamiento
                int desplazamiento = claveLimpia.charAt(indiceClave % claveLimpia.length()) - 'A';

                int nuevaPos;
                if (esCifrado) {
                    nuevaPos = (posLetra + desplazamiento) % 26;
                } else {
                    // Para descifrar, restamos. Sumamos 26 para evitar números negativos en Java
                    nuevaPos = (posLetra - desplazamiento + 26) % 26;
                }

                resultado.append((char) ('A' + nuevaPos));
                indiceClave++; // Solo avanzamos en la clave si procesamos una letra válida
            } else {
                resultado.append(c); // Mantenemos espacios intactos
            }
        }
        return resultado.toString();
    }
}
