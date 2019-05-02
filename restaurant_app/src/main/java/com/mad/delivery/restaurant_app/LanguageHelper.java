package com.mad.delivery.restaurant_app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageHelper {

    public  static Context changeLocale(Context context, Resources res, String locale){
        Configuration config;
        config= new Configuration(res.getConfiguration());
        Locale local  = new Locale(locale);
        switch (locale){
            case "en":
                local=Locale.ENGLISH;
                break;
            case "it":
                local=Locale.ITALY;
                break;
            default:
                local=Locale.ENGLISH;
                break;
        }
       /* if (Build.VERSION.SDK_INT >= 17) {
            config.setLocale(local);
            context = context.createConfigurationContext(config);
            Log.i("MADD","versione >=17");
        } else {
            //config.locale = local;
            res.updateConfiguration(config, res.getDisplayMetrics());
            Log.i("MADD","versione <17");
        }
*/
        return context;
        //res.updateConfiguration(config, res.getDisplayMetrics());

    }
}
