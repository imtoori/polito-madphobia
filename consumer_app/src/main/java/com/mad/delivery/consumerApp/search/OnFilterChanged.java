package com.mad.delivery.consumerApp.search;

import com.google.android.material.chip.Chip;

public interface OnFilterChanged {
    void changed(Chip chip, boolean checked);
}
