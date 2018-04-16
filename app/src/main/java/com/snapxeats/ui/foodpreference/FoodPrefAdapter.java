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
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.preference.FoodPref;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_preference, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodPref foodPref = rootFoodPrefList.get(position);
        holder.setItem(position, foodPref);
    }

    @Override
    public int getItemCount() {
        return rootFoodPrefList.size();
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

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            tapCount++;
            if (tapCount == UIConstants.SINGLE_TAP) {
                new Handler().postDelayed(() -> {
                    if (tapCount == UIConstants.SINGLE_TAP) {
                        //Single click
                        onDoubleTapListenr.onSingleTap(position, true);

                    } else if (tapCount == UIConstants.DOUBLE_TAP) {
                        //Double click
                        onDoubleTapListenr.onDoubleTap(position, true);
                        tapCount = 0;
                    }
                    tapCount = 0;
                }, TIME_DELAY);
            }
        }

        void setItem(int position, FoodPref foodPref) {

            Picasso.with(mContext).load(foodPref.getFood_image_url()).into(imgCuisinePref);
            txtCuisineName.setText(foodPref.getFood_name());

            if (foodPref.is_food_favourite()) {
                imgStatus.setVisibility(View.VISIBLE);
                imgStatus.setImageResource(R.drawable.ic_superlike_pref);
            } else if (foodPref.is_food_like()) {
                imgStatus.setVisibility(View.VISIBLE);
                imgStatus.setImageResource(R.drawable.ic_like_pref);
            } else {
                imgStatus.setVisibility(View.INVISIBLE);
            }

            imgStatus.setOnClickListener(v -> {
                FoodPreferenceActivity.isDirty = true;
                imgStatus.setVisibility(View.INVISIBLE);
                foodPref.set_food_like(false);
                foodPref.set_food_favourite(false);
                notifyItemChanged(position);
            });
        }
    }
}
