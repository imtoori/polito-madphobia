package com.mad.delivery.biker_app;

import android.view.View;

import com.mad.delivery.biker_app.R;

import java.util.ArrayList;
import java.util.List;

public class SettingItem {

    private String title;
    private int imageId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public static List<SettingItem> getData(View view) {
        List<SettingItem> dataList =new ArrayList<>();

        int[] imageIds = getImages();
        String[] titles = getTitles(view);

        for(int i=0; i< titles.length; i++){
            SettingItem sI= new SettingItem();
            sI.setTitle(titles[i]);
            sI.setImageId(imageIds[i]);
            dataList.add(sI);
        }
        return dataList;
    }

    private static String[] getTitles(View view) {
        return new String[]{view.getResources().getString(R.string.setting_profile),
                view.getResources().getString(R.string.setting_openinghours),
                view.getResources().getString(R.string.setting_privacy)
        };
    }

    private static int[] getImages() {
        return new int[]{
                R.drawable.ic_user, R.drawable.ic_clock, R.drawable.ic_edit
        };

    }



}
