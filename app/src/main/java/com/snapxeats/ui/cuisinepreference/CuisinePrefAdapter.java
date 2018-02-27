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
import com.snapxeats.common.constants.SnapXToast;
import com.snapxeats.common.model.Cuisines;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefAdapter extends RecyclerView.Adapter<CuisinePrefAdapter.ViewHolder> {
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
        Picasso.with(mContext).load(cuisines.getCuisine_image_url()).into(holder.imgCuisinePref);
        holder.txtCuisineName.setText(cuisines.getCuisine_name());

        if (cuisines.is_cuisine_favourite()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_superlike_pref);

        } else if (cuisines.is_cuisine_like()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_like_pref);
        } else {
            holder.imgStatus.setImageResource(0);
        }

        holder.imgStatus.setOnClickListener(v -> {
            holder.imgStatus.setImageResource(0);
            cuisines.set_cuisine_favourite(false);
            cuisines.set_cuisine_like(false);
            CuisinePrefActivity.isCuisineSelecetionChange = true;

        });
    }

    @Override
    public int getItemCount() {
        return cuisineArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private static final long TIME_DELAY = 250;
        private TextView txtCuisineName;
        private ImageView imgCuisinePref;
        private ImageView imgStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtCuisineName = itemView.findViewById(R.id.txt_cuisine_pref_name);
            imgCuisinePref = itemView.findViewById(R.id.img_cuisine_pref);
            imgStatus = itemView.findViewById(R.id.img_status);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            tapCount++;
            if (tapCount == 1) {
                new Handler().postDelayed(() -> {
                    if (tapCount == 1) {
                        //Single click
                        SnapXToast.debug("Single");
                        onDoubleTapListenr.onSingleTap(position, true);

                    } else if (tapCount == 2) {
                        //Double click
                        onDoubleTapListenr.onDoubleTap(position, true);
                        tapCount = 0;
                        SnapXToast.debug("Double");
                    }
                    tapCount = 0;
                }, TIME_DELAY);
            }
        }
    }
}
