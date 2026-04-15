package com.criptografia.cifrados;

import com.criptografia.inter.Cifrado;

public class CifradoAtbash implements Cifrado {
    @Override
    public String cifrar(String texto, String clave) {
        return procesar(texto);
    }

    //En este cifrado, con aplicarlo dos veces ya nos devuelve el texto original
    @Override
    public String descifrar(String texto, String clave) {
        return procesar(texto);
    }

    private String procesar(String texto) {
        StringBuilder resultado = new StringBuilder();

        for (char c : texto.toUpperCase().toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                // Restamos a 'Z' la distancia que hay entre la letra actual y la 'A'
                char letraEspejo = (char) ('Z' - (c - 'A'));
                resultado.append(letraEspejo);
            } else {
                resultado.append(c);
            }
        }
        return resultado.toString();
    }
}
