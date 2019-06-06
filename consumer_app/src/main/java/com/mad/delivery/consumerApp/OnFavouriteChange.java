package com.mad.delivery.consumerApp;

import android.widget.ToggleButton;

import com.mad.delivery.resources.PreviewInfo;

public interface OnFavouriteChange {
    void onOpen(PreviewInfo p);
    void onRemoved(String id);
}
