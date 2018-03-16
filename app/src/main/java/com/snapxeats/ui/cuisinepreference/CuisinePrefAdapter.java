package com.snapxeats.ui.cuisinepreference;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.snapxeats.R;
import com.snapxeats.common.model.preference.Cuisines;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefAdapter extends RecyclerView.Adapter<CuisinePrefAdapter.ViewHolder> {

    private static final int SINGLE_TAP = 1;
    private static final int DOUBLE_TAP = 2;
    private List<Cuisines> cuisineArrayList;
    private Context mContext;
    private OnDoubleTapListenr onDoubleTapListenr;
    private int tapCount = 0;

    CuisinePrefAdapter(Context mContext,
                       List<Cuisines> cuisineArrayList,
                       OnDoubleTapListenr onDoubleTapListenr) {
        this.cuisineArrayList = cuisineArrayList;
        this.mContext = mContext;
        this.onDoubleTapListenr = onDoubleTapListenr;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_preference, parent,
                false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cuisines cuisines = cuisineArrayList.get(position);
        holder.setItem(position, cuisines);
    }

    @Override
    public int getItemCount() {
        return cuisineArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final long TIME_DELAY = 250;

        @BindView(R.id.txt_cuisine_pref_name)
        TextView txtCuisineName;

        @BindView(R.id.img_cuisine_pref)
        ImageView imgCuisinePref;

        @BindView(R.id.img_status)
        ImageView imgStatus;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setItem(int position, Cuisines cuisines) {

            Picasso.with(mContext).load(cuisines.getCuisine_image_url()).into(imgCuisinePref);
            txtCuisineName.setText(cuisines.getCuisine_name());

            if (cuisines.is_cuisine_favourite()) {
                imgStatus.setVisibility(View.VISIBLE);
                imgStatus.setImageResource(R.drawable.ic_superlike_pref);

            } else if (cuisines.is_cuisine_like()) {
                imgStatus.setVisibility(View.VISIBLE);
                imgStatus.setImageResource(R.drawable.ic_like_pref);
            } else {
                imgStatus.setVisibility(View.INVISIBLE);
            }

            imgStatus.setOnClickListener(v -> {
                CuisinePrefActivity.isDirty = true;
                imgStatus.setVisibility(View.INVISIBLE);
                cuisines.set_cuisine_favourite(false);
                cuisines.set_cuisine_like(false);
                notifyItemChanged(position);
            });
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            tapCount++;
            if (tapCount == SINGLE_TAP) {
                new Handler().postDelayed(() -> {
                    if (tapCount == SINGLE_TAP) {
                        //Single click
                        onDoubleTapListenr.onSingleTap(position, true);

                    } else if (tapCount == DOUBLE_TAP) {
                        //Double click
                        onDoubleTapListenr.onDoubleTap(position, true);
                        tapCount = 0;
                    }
                    tapCount = 0;
                }, TIME_DELAY);
            }
        }
    }
}
