package com.snapxeats.ui.home.fragment.smartphotos;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.snapxeats.R;
import com.snapxeats.common.model.smartphotos.RestaurantAminities;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 23/5/18.
 */

public class AminityAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> amenityList;

    public AminityAdapter(Context context, List<String> amenityList) {
        mContext = context;
        this.amenityList = amenityList;
    }

    @Override
    public int getCount() {
        return amenityList.size();
    }

    @Override
    public Object getItem(int position) {
        return amenityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.item_amenities, null);
            new AminityAdapter.ViewHolder(convertView);
        }

      AminityAdapter.ViewHolder holder = (AminityAdapter.ViewHolder) convertView.getTag();
        holder.mTxtAmenity.setText(amenityList.get(position));
        return convertView;
    }

    class ViewHolder {

        @BindView(R.id.txt_amenity)
        TextView mTxtAmenity;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
