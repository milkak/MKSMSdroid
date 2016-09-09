package de.ub0r.android.smsdroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ArrayRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.List;

/**
 * Created by milos on 25.2.2015.
 */
public class MyAdapter extends ArrayAdapter<String>  {

    private int mResourceId = 0;
    private LayoutInflater mLayoutInflater;
    private RadioButton mSelectedRB;
    private int mSelectedPosition = -1;
    private  int mseleteditem;
    private ColorCallback RadioButtonListener;


    public MyAdapter(Context context,int seleteditem,int textViewResourceId,List<String> objects,ColorCallback RadioButtonListener  ) {
        super(context,seleteditem,textViewResourceId,objects);
        //mResourceId = resource;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mseleteditem = seleteditem;

    }

    final String[] colors = new String[]{
            "#F44336",
            "#e91e63",
            "#9c27b0",
            "#673ab7",
            "#3f51b5",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8bc34a",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#212121",
            "#607d8b",
            "#004d40"
    };



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if(view == null){

            view = mLayoutInflater.inflate(R.layout.dialog_customlistitem, parent, false);
            holder = new ViewHolder();

            holder.title = (TextView)view.findViewById(R.id.title);
            holder.radioBtn = (RadioButton)view.findViewById(R.id.radioButton);
            holder.mcolor = (ImageView) view.findViewById(R.id.imageview);
            if (position==mseleteditem){
                mSelectedRB = (RadioButton)view.findViewById(R.id.radioButton);
                mSelectedPosition = position;


            }
            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }




        holder.radioBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(position != mSelectedPosition && mSelectedRB != null){
                    mSelectedRB.setChecked(false);
                    notifyDataSetInvalidated();
                }

                mSelectedPosition = position;

                try {
                  RadioButtonListener.onColorSelection(colors[position]);
                  } catch (Exception e) {
                    e.printStackTrace();
                }

                mSelectedRB = (RadioButton)v;
            }
        });



        if(mSelectedPosition != position){
            holder.radioBtn.setChecked(false);

        }else{
            holder.radioBtn.setChecked(true);
            if(mSelectedRB != null && holder.radioBtn != mSelectedRB){
                mSelectedRB = holder.radioBtn;
            }
        }



        holder.mcolor.setBackgroundColor(Color.parseColor(colors[position]));

        holder.title.setText(getItem(position));


        return view;
    }

    public class ViewHolder{
        TextView title;
        RadioButton radioBtn;
        ImageView mcolor;

    }

    public  interface ColorCallback {
        void onColorSelection(String color);
    }


}