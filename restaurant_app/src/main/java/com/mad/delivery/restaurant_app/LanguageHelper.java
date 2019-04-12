package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public  static void changeLocale(Context context, Resources res, String locale){
        Configuration config;
        config= new Configuration(res.getConfiguration());
        Locale local  = new Locale(locale);
        switch (locale){
            case "en":
                config.setLocale(Locale.ENGLISH);
                break;
            case "it":
                config.setLocale(Locale.ITALY);
                break;
            default:
                config.setLocale(Locale.ENGLISH);
                break;
        }
        config.setLayoutDirection(local);
        //res.updateConfiguration(config, res.getDisplayMetrics());
        Context ctxt = context.createConfigurationContext(config);

    }
}
