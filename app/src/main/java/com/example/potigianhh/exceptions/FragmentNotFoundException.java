package com.example.potigianhh.exceptions;

import com.example.potigianhh.fragments.BaseFragment;

public class FragmentNotFoundException extends RuntimeException {
    public <T extends BaseFragment> FragmentNotFoundException(Class<T> fragmentKlazz) {
        super("No se ha encontrado el fragmento de la clase " + fragmentKlazz.getSimpleName());
    }
}
