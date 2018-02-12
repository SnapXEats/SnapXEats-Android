package com.snapxeats.ui.location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.snapxeats.R;
import com.snapxeats.common.model.Prediction;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Snehal Tembare on 31/1/18.
 */

public class LocationAdapter extends ArrayAdapter<String> {

    private Context mContext;
    public List<String> resultList;
    public List<Prediction> predictionList;
    private PlaceAPI placeAPI;

    LocationAdapter(@NonNull Context context,
                    int resource) {
        super(context, resource);
        mContext = context;
        placeAPI = new PlaceAPI();
        resultList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return resultList != null ? resultList.size() : 0;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    predictionList = placeAPI.autocomplete(constraint.toString());
                    for (int index = 0; index < predictionList.size(); index++) {
                        resultList.add(predictionList.get(index).getDescription());
                    }
                    if (resultList != null) {
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }

                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_prediction_layout, null);
        } else {
            view = convertView;
        }
        viewHolder = new ViewHolder(view);

        if (resultList != null) {
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