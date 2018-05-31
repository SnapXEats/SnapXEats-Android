package com.snapxeats.ui.home.fragment.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.R;
import com.snapxeats.common.model.preference.Cuisines;
import com.snapxeats.ui.cuisinepreference.OnDoubleTapListenr;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.snapxeats.common.constants.UIConstants.SELECT_OPACITY;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.UNSELECT_OPACITY;

/**
 * Created by Snehal Tembare on 7/3/18.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Cuisines> cuisineArrayList;
    private Context mContext;
    private OnDoubleTapListenr onDoubleTapListenr;

    HomeAdapter(Context mContext,
                List<Cuisines> cuisineArrayList,
                OnDoubleTapListenr onDoubleTapListenr) {
        this.cuisineArrayList = cuisineArrayList;
        this.mContext = mContext;
        this.onDoubleTapListenr = onDoubleTapListenr;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.cuisine_recycler_view, parent,
                false);
        return new HomeAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cuisines cuisines = cuisineArrayList.get(position);
        holder.setItem(cuisines);
    }

    @Override
    public int getItemCount() {
        return cuisineArrayList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.txt_cuisine_name)
        TextView txtCuisineName;

        @BindView(R.id.img_cuisine)
        ImageView imgCuisinePref;


        @BindView(R.id.img_cuisine_selected)
        ImageView imgCuisineSelected;

        @BindView(R.id.layout_cuisine_cardview)
        LinearLayout linearLayoutCuisine;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (cuisineArrayList.get(position).is_cuisine_like() ||
                    cuisineArrayList.get(position).is_cuisine_favourite()) {
                onDoubleTapListenr.onSingleTap(position, false);
            } else {
                onDoubleTapListenr.onSingleTap(position, true);
            }
        }

        public void setItem(Cuisines cuisines) {

            Glide.with(mContext)
                    .load(cuisines.getCuisine_image_url())
                    .placeholder(R.drawable.ic_pref_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(THUMBNAIL)
                    .into(imgCuisinePref);

            txtCuisineName.setText(cuisines.getCuisine_name());

            if (cuisines.is_cuisine_like()) {
                imgCuisineSelected.setVisibility(View.VISIBLE);
                linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
            } else if (cuisines.is_cuisine_favourite()) {
                imgCuisineSelected.setVisibility(View.VISIBLE);
                linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
            } else if (!cuisines.is_cuisine_like()) {
                imgCuisineSelected.setVisibility(View.GONE);
                linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
            } else if (!cuisines.is_cuisine_favourite()) {
                imgCuisineSelected.setVisibility(View.GONE);
                linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
            }
        }
    }
}
