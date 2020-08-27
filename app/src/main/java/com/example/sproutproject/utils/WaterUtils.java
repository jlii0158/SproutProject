package com.example.sproutproject.utils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sproutproject.R;

import java.util.ArrayList;
import java.util.List;

public class WaterUtils {


    public void createWaterNeed(LinearLayout continer, int icon_array, int waterNeed) {

        for (int i = 0; i < waterNeed; i++) {

            TextView tv = (TextView) View.inflate(continer.getContext(), R.layout.inflate_water_need, null);

            tv.setCompoundDrawablesWithIntrinsicBounds(0, icon_array, 0, 0);
            //分配每个图标的空间
            int width = 100;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            continer.addView(tv, width,height);
        }

    }
}
