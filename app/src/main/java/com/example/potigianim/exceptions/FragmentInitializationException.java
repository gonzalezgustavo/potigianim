package com.example.potigianim.exceptions;

public class FragmentInitializationException extends RuntimeException {
    public FragmentInitializationException(String fragmentName) {
        super("No se pudo inicializar el fragmento " + fragmentName);
    }
}
