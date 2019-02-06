package com.apolongo.apolongo;

import android.view.View;

//Implement an onClick event for the Recycler list items
public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
