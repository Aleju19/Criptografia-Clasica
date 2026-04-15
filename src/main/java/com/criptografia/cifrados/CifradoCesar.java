package com.criptografia.cifrados;

import com.criptografia.inter.Cifrado;

public class CifradoCesar  implements Cifrado {

    //Método para cifrar el mensaje
    @Override
    public String cifrar(String texto, String clave) {
        int desplazamiento = Integer.parseInt(clave) %26;
        return procesar(texto, desplazamiento);
    }

    //Método para descifrar el mensaje
    @Override
    public String descifrar(String texto, String clave) {
        int desplazamiento = Integer.parseInt(clave) %26;
        return procesar(texto, 26 - desplazamiento);
    }


    private String procesar(String texto, int desplazamiento) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toUpperCase().toCharArray()) {
            // Verificamos si es una letra del abecedario inglés (sin ñ)
            if (c >= 'A' && c <= 'Z') {
                int posOriginal = c - 'A';
                int nuevaPos = (posOriginal + desplazamiento) % 26;
                resultado.append((char) ('A' + nuevaPos));
            } else {
                // Si es un espacio o número, lo dejamos igual
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
}