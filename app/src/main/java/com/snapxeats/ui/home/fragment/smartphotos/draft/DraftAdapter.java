package com.snapxeats.ui.home.fragment.smartphotos.draft;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.R;
import com.snapxeats.common.model.smartphotos.SnapXDraftPhoto;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;

/**
 * Created by Snehal Tembare on 16/5/18.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.ViewHolder> {

    private Context mContext;
    private List<SnapXDraftPhoto> mDraftPhotoList;
    private OnItemClickListener onItemClickListener;

    DraftAdapter(Context context,
                 List<SnapXDraftPhoto> mDraftPhotoList,
                 OnItemClickListener onItemClickListener) {
        mContext = context;
        this.mDraftPhotoList = mDraftPhotoList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.item_draft_photo, parent,
                false);
        return new DraftAdapter.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(mDraftPhotoList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDraftPhotoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_restaurant)
        ImageView mImgView;

        @BindView(R.id.txt_rest_name)
        protected TextView mTxtRestName;

        @BindView(R.id.img_share)
        ImageView mImgShare;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(SnapXDraftPhoto snapXDraftPhoto) {

            Glide.with(mContext)
                    .load(snapXDraftPhoto.getImageURL())
                    .asBitmap()
                    .placeholder(R.drawable.ic_rest_info_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).thumbnail(THUMBNAIL)
                    .into(mImgView);

            mTxtRestName.setText(snapXDraftPhoto.getRestaurantName());
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onClick(mDraftPhotoList.get(getAdapterPosition()), v);
        }
    }

    interface OnItemClickListener {
        void onClick(SnapXDraftPhoto snapXDraftPhoto, View view);
    }
}
