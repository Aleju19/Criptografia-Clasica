package com.criptografia.factory;

import com.criptografia.cifrados.*;
import com.criptografia.inter.Cifrado;

public class CifradoFactory {

    public static Cifrado obtenerCifrado(String tipo) {
        return switch (tipo) {
            case "Cifrado César" -> new CifradoCesar();
            case "Cifrado Atbash" -> new CifradoAtbash();
            case "Cifrado Vigenere" -> new CifradoVigenere();
            case "Cifrado Rail Fence" -> new CifradoRailFence();
            case "Cifrado Playfair" -> new CifradoPlayfair();

            default -> throw new IllegalArgumentException("Cifrado no soportado: " + tipo);
        };
    }
}
