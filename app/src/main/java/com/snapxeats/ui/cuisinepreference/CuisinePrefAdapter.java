package com.snapxeats.ui.cuisinepreference;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.R;
import com.snapxeats.common.model.preference.Cuisines;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.snapxeats.common.constants.UIConstants.DOUBLE_TAP;
import static com.snapxeats.common.constants.UIConstants.SINGLE_TAP;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.TIME_DELAY;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 13/2/18.
 */

public class CuisinePrefAdapter extends RecyclerView.Adapter<CuisinePrefAdapter.ViewHolder> {

    private List<Cuisines> cuisineArrayList;
    private Context mContext;
    private OnDoubleTapListenr onDoubleTapListenr;
    private int tapCount = ZERO;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
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

            Glide.with(mContext)
                    .load(cuisines.getCuisine_image_url())
                    .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_pref_placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .override(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .dontTransform())
                    .thumbnail(THUMBNAIL)
                    .into(imgCuisinePref);

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
                cuisines.setSelected(true);
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
                        tapCount = ZERO;
                    }
                    tapCount = ZERO;
                }, TIME_DELAY);
            }
        }
    }
}
