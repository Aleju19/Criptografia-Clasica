package com.criptografia.inter;

public interface Cifrado {
    String cifrar(String texto, String clave);
    String descifrar(String texto, String clave);
}
