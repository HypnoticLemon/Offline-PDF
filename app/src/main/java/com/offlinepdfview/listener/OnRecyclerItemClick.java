package com.offlinepdfview.listener;

import android.view.View;

/**
 * Created by sit78 on 02-11-2017.
 */

public interface OnRecyclerItemClick {
    void ItemClick(int position, String data, View view, int id);
}
