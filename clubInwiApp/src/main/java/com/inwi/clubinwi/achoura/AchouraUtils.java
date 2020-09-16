package com.inwi.clubinwi.achoura;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;

import com.inwi.clubinwi.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rubikstudio.library.model.LuckyItem;

public class AchouraUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getAppLocale(@NonNull Context context) {
        String strLocale = "fr";
        if (context != null) {
            Configuration configuration = context.getResources().getConfiguration();
            Locale locale;
            if (Build.VERSION.SDK_INT < 24) {
                locale = configuration.locale;
            } else {
                locale = configuration.getLocales().get(0);
            }
            if (locale != null) {
                strLocale = locale.getLanguage();
            }
        }
        return strLocale;
    }
    
    public static List<LuckyItem> populateLuckyWheel(Context context) {
        List<LuckyItem> data = new ArrayList<>(16);

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.secondaryText = "Samsung Galaxy Watch R810";
        luckyItem1.color = context.getResources().getColor(R.color.darkViolet);
        luckyItem1.icon = R.drawable.galaxy_watch;
        data.add(luckyItem1);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.secondaryText = "Huawei Watch GT";
        luckyItem3.color = context.getResources().getColor(R.color.green);
        luckyItem3.icon = R.drawable.huawei_watch;
        data.add(luckyItem3);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.secondaryText = "Pass de communication";
        luckyItem2.color = context.getResources().getColor(R.color.lightBlue);;
        luckyItem2.icon = R.drawable.passcom;
        data.add(luckyItem2);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.secondaryText = "Samsung Galaxy-BUDS";
        luckyItem5.color = context.getResources().getColor(R.color.violet);
        luckyItem5.icon = R.drawable.galaxybuds;
        data.add(luckyItem5);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.secondaryText = "Bluetooth AKG S30";
        luckyItem4.color = context.getResources().getColor(R.color.yellow);
        luckyItem4.icon = R.drawable.bluetoothakg;
        data.add(luckyItem4);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.secondaryText = "Pass RS";
        luckyItem8.color = context.getResources().getColor(R.color.darkViolet);
        luckyItem8.icon = R.drawable.pass_rs;
        data.add(luckyItem8);

        LuckyItem luckyItem10 = new LuckyItem();
        luckyItem10.secondaryText = "Casque Audio Bluetooth AKG Y50BT";
        luckyItem10.color = context.getResources().getColor(R.color.lightBlue);
        luckyItem10.icon = R.drawable.casque;
        data.add(luckyItem10);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.secondaryText = "Tonalité d'acceuil";
        luckyItem6.color = context.getResources().getColor(R.color.yellow);
        luckyItem6.icon = R.drawable.tonalaccueil;
        data.add(luckyItem6);

        LuckyItem luckyItem9 = new LuckyItem();
        luckyItem9.secondaryText = "Pass de communication Inwi";
        luckyItem9.color = context.getResources().getColor(R.color.green);
        luckyItem9.icon = R.drawable.passcominwi;
        data.add(luckyItem9);

        LuckyItem luckyItem11 = new LuckyItem();
        luckyItem11.secondaryText = "Pass Youtube";
        luckyItem11.color = context.getResources().getColor(R.color.violet);
        luckyItem11.icon = R.drawable.youtube;
        data.add(luckyItem11);

        LuckyItem luckyItem16 = new LuckyItem();
        luckyItem16.secondaryText = "Pass Internet";
        luckyItem16.color = context.getResources().getColor(R.color.lightBlue);
        luckyItem16.icon = R.drawable.passinternet;
        data.add(luckyItem16);

        LuckyItem luckyItem14 = new LuckyItem();
        luckyItem14.secondaryText = "Huawei Band pro 3";
        luckyItem14.color = context.getResources().getColor(R.color.yellow);
        luckyItem14.icon = R.drawable.watchband;
        data.add(luckyItem14);

        ////////////////////////////////////////////////////////////////////////////////////////////

        return data;
    }

    public static String compensateWithZero(long number) {
        if (number > 9) {
            return String.valueOf(number);
        } else {
            return "0" + number;
        }
    }
    
}
