package com.mad.delivery.restaurant_app;

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

    public static List<SettingItem> getData() {
        List<SettingItem> dataList =new ArrayList<>();

        int[] imageIds = getImages();
        String[] titles = getTitles();

        for(int i=0; i< titles.length; i++){
            SettingItem sI= new SettingItem();
            sI.setTitle(titles[i]);
            sI.setImageId(imageIds[i]);
            dataList.add(sI);
        }
        return dataList;
    }

    private static String[] getTitles() {
        return new String[]{"Profile","Password","Language"};
    }

    private static int[] getImages() {
        return new int[]{
                R.drawable.ic_user, R.drawable.ic_edit, R.drawable.ic_language
        };

    }



}
