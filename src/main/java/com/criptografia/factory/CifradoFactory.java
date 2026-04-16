package com.criptografia.factory;

import com.criptografia.cifrados.*;
import com.criptografia.inter.Cifrado;

public class CifradoFactory {

    public static Cifrado obtenerCifrado(String tipo) {
        return switch (tipo) {
            case "César" -> new CifradoCesar();
            case "Atbash" -> new CifradoAtbash();
            case "Vigenère" -> new CifradoVigenere();
            case "Rail Fence" -> new CifradoRailFence();
            case "Playfair" -> new CifradoPlayfair();

            default -> throw new IllegalArgumentException("Cifrado no soportado: " + tipo);
        };
    }
}
