package com.example.sproutproject.utils;


import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sproutproject.R;

import java.util.ArrayList;
import java.util.List;

public class ToolbarUtils {

    private List<TextView> mTextView = new ArrayList<TextView>();

    public void createToolBar(LinearLayout continer, String[] title, int[] icon_array) {

        for (int i = 0; i < title.length; i++) {

            TextView tv = (TextView) View.inflate(continer.getContext(), R.layout.inflate_toolbar_btn, null);

            //设置title
            tv.setText(title[i]);
            //设置title对应的图标
            tv.setCompoundDrawablesWithIntrinsicBounds(0, icon_array[i], 0, 0);
            //分配每个图标的空间
            int width = 0;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
            //保证每个图标分配的位置均匀
            params.weight = 1;
            continer.addView(tv, params);
            mTextView.add(tv);

            //在方法内部实现点击事件，和最下面两个方法是一起使用的，mOnToolBarClickListener是来自下面方法的实例
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnToolBarClickListener.OnToolBarClick(finalI);
                }
            });
        }

    }

    public void changeColor(int position) {

        for (TextView tv : mTextView) {
            tv.setSelected(false);
        }
        mTextView.get(position).setSelected(true);
    }





    //当点击按钮时 需要写一个接口去接收这个点击事件
    public interface OnToolBarClickListener {
        void OnToolBarClick(int position);
    }
    OnToolBarClickListener mOnToolBarClickListener;

    public void setOnToolBarClickListener(OnToolBarClickListener onToolBarClickListener) {
        mOnToolBarClickListener = onToolBarClickListener;
    }
}
