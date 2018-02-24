package com.snapxeats.ui.foodpreference;

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
import com.snapxeats.common.model.FoodPref;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class FoodPrefAdapter extends RecyclerView.Adapter<FoodPrefAdapter.ViewHolder> {
    private List<FoodPref> rootFoodPrefList;
    private Context mContext;
    private OnDoubleTapListenr onDoubleTapListenr;
    private int tapCount = 0;

    FoodPrefAdapter(Context mContext,
                    List<FoodPref> rootFoodPrefList,
                    OnDoubleTapListenr onDoubleTapListenr) {
        this.rootFoodPrefList = rootFoodPrefList;
        this.mContext = mContext;
        this.onDoubleTapListenr = onDoubleTapListenr;
        FoodPreferenceActivity.isFoodPrefSelected = false;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_preference, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodPref foodPref = rootFoodPrefList.get(position);
        Picasso.with(mContext).load(foodPref.getFood_image_url()).into(holder.imgCuisinePref);
        holder.txtCuisineName.setText(foodPref.getFood_name());

        if (foodPref.is_food_favourite()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_superlike_pref);
        } else if (foodPref.is_food_like()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setImageResource(R.drawable.ic_like_pref);
        } else {
            holder.imgStatus.setImageResource(0);
        }

        holder.imgStatus.setOnClickListener(v -> {
            holder.imgStatus.setImageResource(0);
            foodPref.set_food_like(false);
            foodPref.set_food_favourite(false);
            FoodPreferenceActivity.isFoodPrefSelected = true;
        });
    }

    @Override
    public int getItemCount() {
        return rootFoodPrefList.size();
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
