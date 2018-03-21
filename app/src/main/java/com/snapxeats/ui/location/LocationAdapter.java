package com.snapxeats.ui.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.snapxeats.R;

import java.util.List;


/**
 * Created by Snehal Tembare on 31/1/18.
 */

public class LocationAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> resultList;

    LocationAdapter(@NonNull Context context,
                    int resource, List<String> resultList) {
        super(context, resource);
        mContext = context;
        this.resultList = resultList;
    }

    @Override
    public int getCount() {
        return null != resultList ? resultList.size() : 0;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (null == convertView) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_prediction_layout, null);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder(view);

        if (null != resultList) {
            viewHolder.mTextView.setText(resultList.get(position));
        }
        return view;
    }

    class ViewHolder {
        TextView mTextView;

        ViewHolder(View view) {
            mTextView = view.findViewById(R.id.txt_searched_location);
        }
    }
}
