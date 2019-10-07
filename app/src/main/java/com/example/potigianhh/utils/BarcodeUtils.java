package com.example.potigianhh.utils;

import com.example.potigianhh.MainActivity;
import com.honeywell.aidc.AidcManager;
import com.honeywell.aidc.BarcodeReader;

public class BarcodeUtils {
    public static void initialize(final MainActivity activity) {
        try {
            AidcManager.create(activity, aidcManager -> {
                BarcodeReader barcodeReader = aidcManager.createBarcodeReader();
                try {
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_128_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_GS1_128_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_ISBT_128_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_39_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_UPC_A_ENABLE, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_UPC_E_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_UPC_E_E1_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_8_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_EAN_13_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODABAR_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_11_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_CODE_93_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_IATA_25_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_MSI_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_STANDARD_25_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TELEPEN_ENABLED, true);
                    barcodeReader.setProperty(BarcodeReader.PROPERTY_TELEPEN_OLD_STYLE_ENABLED, true);
                    barcodeReader.claim();
                    barcodeReader.addBarcodeListener(activity);

                    activity.setManager(aidcManager);
                    activity.setReader(barcodeReader);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void claim(MainActivity activity) {
        if (activity.getReader() != null) {
            try {
                activity.getReader().claim();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void release(MainActivity activity) {
        if (activity.getReader() != null)
            activity.getReader().release();
    }
}
