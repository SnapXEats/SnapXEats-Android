package com.snapxeats.ui.home.fragment.smartphotos.smart;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.snapxeats.BaseFragment;
import com.snapxeats.R;
import com.snapxeats.common.DbHelper;
import com.snapxeats.common.constants.UIConstants;
import com.snapxeats.common.model.smartphotos.RestaurantAminities;
import com.snapxeats.common.model.smartphotos.RestaurantAminitiesDao;
import com.snapxeats.common.model.smartphotos.SmartPhoto;
import com.snapxeats.common.utilities.AppUtility;
import com.snapxeats.dagger.AppContract;
import com.snapxeats.ui.home.fragment.smartphotos.AminityAdapter;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.snapxeats.common.constants.UIConstants.THUMBNAIL;
import static com.snapxeats.common.constants.UIConstants.ZERO;

/**
 * Created by Snehal Tembare on 15/5/18.
 */

public class SmartFragment extends BaseFragment implements SmartContract.SmartView,
        AppContract.SnapXResults, View.OnClickListener, MediaPlayer.OnCompletionListener {

    @Inject
    SmartContract.SmartPresenter mSmartPresenter;
    private List<SmartPhoto> mSmartPhotoList;
    private SmartAdapter mSmartAdapter;
    private SmartPhoto mSmartPhoto;
    private Dialog mDialog;

    private LinearLayout mLayoutControls;
    private LinearLayout mLayoutDescription;
    private LinearLayout mLayoutInfo;
    private LinearLayout mLayoutReview;
    private LinearLayout mLayoutAudio;
    private ListView mListAminities;

    private ImageView mImg;
    private ImageView mImgClose;
    private ImageView mImgInfo;
    private ImageView mImgAudioReview;
    private ImageView mImgTextReview;

    private ImageView mImgPlayAudio;

    private TextView mTxtRestName;
    private TextView mTxtTimeOfAudio;
    private TextView mTxtRestAddress;
    private TextView mTxtRestReviewContents;

    private boolean isImageTap;
    private boolean isInfoTap;
    private boolean isReviewTap;
    private boolean isAudioViewTap;
    private boolean isAudioPlayTap;
    private MediaPlayer mMediaPlayer;
    private Handler mHandler = new Handler();
    private List<String> mAminitiesList;

    @BindView(R.id.smartlistview)
    protected RecyclerView mRecyclerview;

    @Inject
    DbHelper dbHelper;

    @Inject
    AppUtility utility;

    @Inject
    public SmartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = null;
        try {
            view = inflater.inflate(R.layout.fragment_smart, container, false);
            ButterKnife.bind(this, view);
        } catch (InflateException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        mSmartPhotoList = dbHelper.getSmartPhotoDao().loadAll();
        if (null != mSmartPhotoList && ZERO != mSmartPhotoList.size()){
            mSmartAdapter = new SmartAdapter(getActivity(), mSmartPhotoList, (smartPhoto, photoItemView) -> {
                mSmartPhoto = smartPhoto;
                QueryBuilder<RestaurantAminities> queryBuilder = dbHelper.getRestaurantAminitiesDao().queryBuilder();
                List<RestaurantAminities> aminitiesList = queryBuilder
                        .where(RestaurantAminitiesDao.Properties.PhotoIdFk.eq(mSmartPhoto.getSmartPhoto_Draft_Stored_id())).list();
                mAminitiesList = new ArrayList<>();
                for (RestaurantAminities aminity : aminitiesList) {
                    mAminitiesList.add(aminity.getAminity());
                }

                showSmartPhotoDialog();
            });
            mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerview.setAdapter(mSmartAdapter);
        }
    }

    private void showSmartPhotoDialog() {
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(R.layout.draft_dialog_layout);
        Window window = mDialog.getWindow();
        if (null != window) {
            window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.START);
        }

        initViewsForSmartPhotoDialog();
        if (null == mSmartPhoto.getTextReview() || mSmartPhoto.getTextReview().isEmpty())
            mImgTextReview.setVisibility(View.GONE);

        if (null == mSmartPhoto.getAudioURL() || mSmartPhoto.getAudioURL().isEmpty())
            mImgAudioReview.setVisibility(View.GONE);

        Glide.with(getActivity())
                .load(mSmartPhoto.getDishImageURL())
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .dontAnimate()
                        .dontTransform())
                .thumbnail(THUMBNAIL)
                .into(mImg);

        //Register listeners
        registerListeners();
        mDialog.show();
    }

    private void registerListeners() {
        mImg.setOnClickListener(this);
        mImgClose.setOnClickListener(this);
        mImgInfo.setOnClickListener(this);
        mImgTextReview.setOnClickListener(this);
        mImgAudioReview.setOnClickListener(this);
        mImgPlayAudio.setOnClickListener(this);
    }

    private void initViewsForSmartPhotoDialog() {
        mLayoutDescription = mDialog.findViewById(R.id.layout_description);
        mLayoutControls = mDialog.findViewById(R.id.layout_controls);
        mLayoutInfo = mDialog.findViewById(R.id.layout_info);
        mLayoutReview = mDialog.findViewById(R.id.layout_review);
        mLayoutAudio = mDialog.findViewById(R.id.layout_audio);

        mTxtRestName = mDialog.findViewById(R.id.txt_rest_name);
        mTxtRestAddress = mDialog.findViewById(R.id.txt_rest_address);
        mTxtRestReviewContents = mDialog.findViewById(R.id.txt_review_contents);
        mTxtTimeOfAudio = mDialog.findViewById(R.id.timer_play);

        mImg = mDialog.findViewById(R.id.image_view);
        mImgClose = mDialog.findViewById(R.id.img_close);
        mImgInfo = mDialog.findViewById(R.id.img_info);
        mImgTextReview = mDialog.findViewById(R.id.img_text_review);
        mImgAudioReview = mDialog.findViewById(R.id.img_audio);
        ImageView imgShare = mDialog.findViewById(R.id.img_share);
        imgShare.setVisibility(View.GONE);

        mImgPlayAudio = mDialog.findViewById(R.id.img_play_audio);
        mListAminities = mDialog.findViewById(R.id.list_aminities);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void initView() {
        dbHelper.setContext(getActivity());
        mSmartPresenter.addView(this);
        utility.setContext(getActivity());
    }

    @Override
    public DialogInterface.OnClickListener setListener(AppContract.DialogListenerAction button) {
        return null;
    }

    @Override
    public void success(Object value) {

    }

    @Override
    public void error(Object value) {

    }

    @Override
    public void noNetwork(Object value) {

    }

    @Override
    public void networkError(Object value) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view:
                isImageTap = !isImageTap;
                if (isImageTap) {
                    mLayoutControls.setVisibility(View.VISIBLE);
                } else {
                    mLayoutControls.setVisibility(View.GONE);
                    mLayoutDescription.setVisibility(View.GONE);
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_close:
                utility.resetMediaPlayer(mMediaPlayer);
                if (null != mMediaPlayer) {
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                }
                mDialog.dismiss();
                break;

            case R.id.img_info:
                isInfoTap = !isInfoTap;
                if (isInfoTap) {
                    utility.resetMediaPlayer(mMediaPlayer);
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info_selected));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);
                    mListAminities.setAdapter(new AminityAdapter(getActivity(), mAminitiesList));

                    mTxtRestName.setText(mSmartPhoto.getRestaurantName());
                    mTxtRestAddress.setText(mSmartPhoto.getRestaurantAddress());
                } else {
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    mLayoutInfo.setVisibility(View.GONE);
                }
                break;

            case R.id.img_text_review:
                isReviewTap = !isReviewTap;
                if (isReviewTap) {
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review_selected));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutReview.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mTxtRestReviewContents.setText(mSmartPhoto.getTextReview());
                    utility.resetMediaPlayer(mMediaPlayer);

                } else {
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mLayoutReview.setVisibility(View.GONE);
                }
                break;

            case R.id.img_audio:
                isAudioViewTap = !isAudioViewTap;
                if (isAudioViewTap) {
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker_selected));
                    mImgTextReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_text_review));
                    mImgInfo.setImageDrawable(getActivity().getDrawable(R.drawable.ic_info));

                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);

                    mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSmartPhoto.getAudioURL()));
                    mMediaPlayer.setOnCompletionListener(this);
                    mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));

                } else {
                    mImgAudioReview.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_speaker));
                    mLayoutAudio.setVisibility(View.GONE);
                    utility.resetMediaPlayer(mMediaPlayer);
                }
                break;

            case R.id.img_play_audio:
                isAudioPlayTap = !isAudioPlayTap;

                if (isAudioPlayTap) {
                    mLayoutDescription.setVisibility(View.VISIBLE);
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_audio_pause));
                    mLayoutAudio.setVisibility(View.VISIBLE);
                    mLayoutInfo.setVisibility(View.GONE);
                    mLayoutReview.setVisibility(View.GONE);

                    mMediaPlayer.start();
                    mUpdateTimeTask.run();
                } else {
                    mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    }
                }
                break;
        }
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (null != mMediaPlayer) {
                long currentDuration = mMediaPlayer.getCurrentPosition();

                // Displaying time completed playing
                mTxtTimeOfAudio.setText("" + utility.milliSecondsToTimer(currentDuration));

                // Running this thread after 100 milliseconds
                mHandler.postDelayed(this, UIConstants.PERCENTAGE);
            }
        }
    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        mImgPlayAudio.setImageDrawable(getActivity().getDrawable(R.drawable.ic_play_review));
        mMediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(mSmartPhoto.getAudioURL()));
        mMediaPlayer.setOnCompletionListener(this);
        mTxtTimeOfAudio.setText(utility.milliSecondsToTimer(mMediaPlayer.getCurrentPosition()));
    }
}
