package com.kcdeveloperss.wallpapers.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kcdeveloperss.wallpapers.R;

public class AppUtils {

    public static void shortToast(Context context, String msg) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View customView  = inflater.inflate(R.layout.customtoast, null);
        Toast toast = new Toast(context);
        TextView TvMsg = customView.findViewById(R.id.TvMsg);
        TvMsg.setText(msg);
        // Set layout to toast
        toast.setView(customView);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 300);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

}
