package com.example.potigianim.exceptions;

import com.example.potigianim.fragments.BaseFragment;

public class FragmentNotFoundException extends RuntimeException {
    public <T extends BaseFragment> FragmentNotFoundException(Class<T> fragmentKlazz) {
        super("No se ha encontrado el fragmento de la clase " + fragmentKlazz.getSimpleName());
    }
}
