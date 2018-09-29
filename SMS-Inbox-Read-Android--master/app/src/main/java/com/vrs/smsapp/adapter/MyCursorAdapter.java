package com.vrs.smsapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.vrs.smsapp.MainClassApplication;
import com.vrs.smsapp.R;
import com.vrs.smsapp.Utility.CalculateTime;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class MyCursorAdapter extends CursorAdapter implements StickyListHeadersAdapter {
    private LayoutInflater cursorInflater;
    private String timeAgoString = "";
    private int count = 0;
    private long timeStamp =0;

    // Default constructor
    public MyCursorAdapter(Context context, Cursor cursor, int flags, long timeStamp) {
        super(context, cursor, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        this.timeStamp = timeStamp;
    }

    public void bindView(View view, Context context, Cursor cursor) {

        mCursor = cursor;

        TextView textViewTitle = (TextView) view.findViewById(R.id.lblMsg);
        TextView textViewNumber = (TextView) view.findViewById(R.id.lblNumber);
        textViewTitle.setText(cursor.getString(2));
        textViewNumber.setText(cursor.getString(1));

        if (MainClassApplication.GetrowCount(context) < cursor.getInt(0)) {
            MainClassApplication.SetRowCount(context,cursor.getInt(0));
        }
        if (cursor.getInt(0) == timeStamp)
        {
            Animation animation = null;
            animation = AnimationUtils.loadAnimation(context, R.anim.shake);
            animation.setDuration(2000);
            view.startAnimation(animation);
            animation = null;

            MainClassApplication.SetRowCount(context,cursor.getInt(0));
        }


    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return cursorInflater.inflate(R.layout.row, parent, false);
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;

        if (convertView == null) {
            headerViewHolder = new HeaderViewHolder();
            convertView = cursorInflater.inflate(R.layout.item_header_layout_follow_team, parent, false);
            headerViewHolder.headerText = (TextView) convertView.findViewById(R.id.date_txt);

            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        String textStr = CalculateTime.getTimeAgo(mCursor.getLong(4));

     if (!timeAgoString.equalsIgnoreCase(textStr))
     {
         headerViewHolder.headerText.setVisibility(View.VISIBLE);
         headerViewHolder.headerText.setText(CalculateTime.getTimeAgo(mCursor.getLong(4)));
         timeAgoString = textStr;
     }

     else
     {
         headerViewHolder.headerText.setVisibility(View.GONE);

     }

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {

//        String time = CalculateTime.getTimeAgo(mCursor.getLong(4));
//        if (!timeAgoString.equalsIgnoreCase(time))
//        {
//            timeAgoString = time;
//            count++;
//            Log.e("gourav" , count+"");
//            return count-1;
//        }
//        Log.e("gourav" , count+"");
        Log.e("pos", position+"");
        return position;
    }

    class HeaderViewHolder {
        TextView headerText;
    }

    class ViewHolder {
        private TextView smsMsg, smsNumber;
    }
}