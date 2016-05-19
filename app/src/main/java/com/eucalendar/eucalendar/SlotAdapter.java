package com.eucalendar.eucalendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Sandeep Kumar Padhi on 12/3/2015.
 */
public class SlotAdapter extends BaseAdapter {

    private Context m_cObjContext;
    private LayoutInflater mInflater;
    ArrayList<String> m_cListOfTime;
    private SlotSelectListner m_cObjListner;
    private int m_cSelectedPosition;
    public SlotAdapter(Context context, ArrayList<String> pList, SlotSelectListner pListner) {
        this.m_cObjContext = context;
        this.m_cListOfTime = pList;
        this.mInflater = LayoutInflater.from(context);
        this.m_cObjListner = pListner;
        m_cSelectedPosition = -1;
    }

    public int getCount() {
        return m_cListOfTime.size();
    }

    public Object getItem(int position) {
        return m_cListOfTime.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.screen_gridcell, null);
        CalenderSqureButton textview = (CalenderSqureButton) convertView.findViewById(R.id.CALENDAR_DAY_GRIDTEXT);
        final String[] lSlotTime = m_cListOfTime.get(position).split(":");
        textview.setText(lSlotTime[0]+":"+lSlotTime[1]);
        textview.setTag(position);
        if(m_cSelectedPosition == position) {
            textview.setTextColor(Color.WHITE);
            textview.setBackgroundResource(R.drawable.selectdatebg);
        } else {
            textview.setTextColor(m_cObjContext.getResources().getColor(R.color.colorPrimary));
            textview.setBackgroundResource(R.drawable.calendar_button_selector);
        }
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lPos = (int) v.getTag();
                m_cSelectedPosition = lPos;
                m_cObjListner.onSlotClickedListner(m_cListOfTime.get(lPos));
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
}
