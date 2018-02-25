package com.snapxeats.ui.home.fragment.home;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.snapxeats.R;
import com.snapxeats.common.model.Cuisines;
import com.snapxeats.common.model.RootCuisine;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prajakta Patil on 19/1/18.
 */
public class HomeFgmtAdapter extends RecyclerView.Adapter<HomeFgmtAdapter.ViewHolder> {

    private RootCuisine rootCuisine;
    private List<Cuisines> cuisineArrayList;
    private Context mContext;
    private double UNSELECT_OPACITY = 1.0;
    private double SELECT_OPACITY = 0.4;
    private long mLastClickTime = 0;
    private List<String> selectedCuisineList;
    private static RecyclerViewClickListener itemListener;

    public HomeFgmtAdapter(Context mContext, List<Cuisines> cuisineArrayList,
                           RootCuisine rootCuisine, RecyclerViewClickListener itemListener) {
        this.mContext = mContext;
        this.cuisineArrayList = cuisineArrayList;
        this.rootCuisine = rootCuisine;
        this.selectedCuisineList=selectedCuisineList;
        HomeFgmtAdapter.itemListener = itemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.cuisine_recycler_view, parent, false);
        return new ViewHolder(itemView, itemListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        selectedCuisineList = new ArrayList<>();
        Cuisines cuisines = cuisineArrayList.get(position);

        viewHolder.txtCuisineName.setText(cuisineArrayList.get(position).getCuisine_name());
        Picasso.with(mContext).load(cuisineArrayList.get(position).getCuisine_image_url())
                .placeholder(R.drawable.ic_cuisine_placeholder).into(viewHolder.imgCuisine);

        viewHolder.cardView.setOnClickListener(v -> {

            itemListener.recyclerViewListClicked(selectedCuisineList);
            if (SystemClock.elapsedRealtime() - mLastClickTime < 500) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();

            cuisines.setSelected(!cuisines.isSelected());

            if (cuisines.isSelected()) {
                viewHolder.imgCuisineSelected.setVisibility(View.VISIBLE);
                viewHolder.linearLayoutCuisine.setAlpha((float) SELECT_OPACITY);
                selectedCuisineList.add(cuisines.getCuisine_name());

            } else {
                viewHolder.imgCuisineSelected.setVisibility(View.GONE);
                viewHolder.linearLayoutCuisine.setAlpha((float) UNSELECT_OPACITY);
                selectedCuisineList.remove(cuisines.getCuisine_name());
            }
        });
    }

    List<String> getSelectedItems() {
        return selectedCuisineList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtCuisineName;
        private ImageView imgCuisine, imgCuisineSelected;
        private CardView cardView;
        private LinearLayout linearLayoutCuisine;
        private RecyclerViewClickListener mListener;

        ViewHolder(View view,RecyclerViewClickListener listener) {
            super(view);
            mListener = listener;
            view.setOnClickListener(this);
            txtCuisineName = view.findViewById(R.id.txt_cuisine_name);
            imgCuisine = view.findViewById(R.id.img_cuisine);
            cardView = view.findViewById(R.id.cardview_cuisine);
            imgCuisineSelected = view.findViewById(R.id.img_cuisine_selected);
            linearLayoutCuisine = view.findViewById(R.id.layout_cuisine_cardview);
        }

        @Override
        public void onClick(View v) {
            mListener.recyclerViewListClicked(selectedCuisineList);
        }
    }

    @Override
    public int getItemCount() {
        return rootCuisine.getCuisineList().size();
    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(List<String> selectedCuisineList);
    }
}