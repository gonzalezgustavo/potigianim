package com.example.potigianim.utils;

public class ProvinceUtils {
    // Information obtained from T101_PROVINCIA
    public static String getById(Integer id) {
        if (id == null) {
            return "Desconocida";
        }

        switch (id) {
            case 1: return "Buenos Aires";
            case 2: return "Catamarca";
            case 3: return "Chaco";
            case 4: return "Chubut";
            case 5: return "Córdoba";
            case 6: return "Corrientes";
            case 7: return "Entre Ríos";
            case 8: return "Formosa";
            case 9: return "Jujuy";
            case 10: return "La Pampa";
            case 11: return "La Rioja";
            case 12: return "Mendoza";
            case 13: return "Misiones";
            case 14: return "Neuquén";
            case 15: return "Río Negro";
            case 16: return "Salta";
            case 17: return "San Juan";
            case 18: return "San Luis";
            case 19: return "Santa Cruz";
            case 20: return "Santa Fe";
            case 21: return "Sgo. del Estero";
            case 22: return "Tierra del Fuego";
            case 23: return "Tucumán";
            case 24: return "C.A.B.A.";
            default: return "Desconocida";
        }
    }
}
