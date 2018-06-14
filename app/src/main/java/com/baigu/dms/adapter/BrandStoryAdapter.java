package com.baigu.dms.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.BaseApplication;
import com.baigu.dms.R;
import com.baigu.dms.activity.BrandCommentAddActivity;
import com.baigu.dms.activity.BrandStoryActivity;
import com.baigu.dms.activity.LoginActivity;
import com.baigu.dms.activity.SplashActivity;
import com.baigu.dms.common.utils.ImageUtil;
import com.baigu.dms.common.utils.StringUtils;
import com.baigu.dms.common.utils.ViewUtils;
import com.baigu.dms.common.utils.rxbus.EventType;
import com.baigu.dms.common.utils.rxbus.RxBus;
import com.baigu.dms.common.view.GlideCircleTransform;
import com.baigu.dms.common.view.MultiImageView;
import com.baigu.dms.common.view.circleimage.adapter.NineGridAdapter;
import com.baigu.dms.common.view.circleimage.view.NineGridImageView;
import com.baigu.dms.common.view.shinebutton.ShineButton;
import com.baigu.dms.domain.cache.UserCache;
import com.baigu.dms.domain.model.BrandStory;
import com.baigu.dms.domain.netservice.BrandStoryService;
import com.baigu.dms.domain.netservice.ServiceManager;
import com.baigu.dms.domain.netservice.UserService;
import com.baigu.dms.domain.netservice.common.token.TokenManager;
import com.baigu.dms.domain.netservice.response.BaseResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2017/5/29 0:28
 */
public class BrandStoryAdapter extends BaseRVAdapter<BrandStory> {

    public Activity mActivity;

    public BrandStoryAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand_story, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BrandStory brandStory = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//        itemViewHolder.spaceView.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        Context context = itemViewHolder.itemView.getContext();
        ImageUtil.loadImage(context, brandStory.getBrand_ctx_img(), itemViewHolder.ivHead);
//        Glide.with(context).load(brandStory.getBrand_ctx_img()).diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(context)).into(itemViewHolder.ivHead);
        itemViewHolder.tvUsername.setText(brandStory.getBrand_title());
        itemViewHolder.tvTime.setText(StringUtils.getTimeLabelStr(brandStory.getCreate_time()));
        if (TextUtils.isEmpty(brandStory.getBrand_brief())) {
            itemViewHolder.tvContent.setText("暂时还没有内容哦");
        } else {
            itemViewHolder.tvContent.setText(brandStory.getBrand_brief());
        }

//        itemViewHolder.tvPraiseNum.setText(String.valueOf(brandStory.getDd()));
//        itemViewHolder.btnPraise.init((Activity) itemViewHolder.itemView.getContext());
//        itemViewHolder.tvCommentNum.setText(String.valueOf(brandStory.getSs()));
//        itemViewHolder.nineGridImageView.setImagesData(brandStory.getUrlList());
//        itemViewHolder.btnPraise.setChecked(brandStory.getIsdz() > 0);

        OnDetailClickListener onDetailClickListener = new OnDetailClickListener(itemViewHolder.itemView.getContext(), brandStory);
//        itemViewHolder.layoutTop.setOnClickListener(onDetailClickListener);
//        itemViewHolder.layoutComment.setOnClickListener(onDetailClickListener);
//        itemViewHolder.layoutPraise.setOnClickListener(brandStory.getIsdz() > 0 ? null : new OnPraiseClickListener(itemViewHolder.btnPraise, brandStory));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        View spaceView;
        ImageView ivHead;
        TextView tvUsername;
        TextView tvContent;
        TextView tvTime;
        //        NineGridImageView nineGridImageView;
//        ShineButton btnPraise;
        View layoutTop;
//        View layoutPraise;
//        View layoutComment;

        public ItemViewHolder(View itemView) {
            super(itemView);
            this.spaceView = itemView.findViewById(R.id.view_space);
            this.ivHead = (ImageView) itemView.findViewById(R.id.iv_head);
            this.tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            this.tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            this.tvTime = (TextView) itemView.findViewById(R.id.tv_time);

//            this.nineGridImageView = (NineGridImageView) itemView.findViewById(R.id.nineGridImageView);
//            this.nineGridImageView.setMaxSize(9);
//            NineGridAdapter nineGridAdapter  = new NineGridAdapter(mActivity);
//            this.nineGridImageView.setAdapter(nineGridAdapter);
//            int singleImgWidth = ViewUtils.getScreenInfo(mActivity).widthPixels - 2 * ViewUtils.dip2px(20);
//            this.nineGridImageView.setSingleImgWidth(singleImgWidth);

//            this.btnPraise = (ShineButton) itemView.findViewById(R.id.btn_praise);
//            this.tvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praise_num);
//            this.tvCommentNum = (TextView) itemView.findViewById(R.id.tv_comment_num);
            this.layoutTop = itemView.findViewById(R.id.rl_top);
//            this.layoutPraise = itemView.findViewById(R.id.layout_praise);
//            this.layoutComment = itemView.findViewById(R.id.layout_comment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(BrandStoryAdapter.this, getLayoutPosition() - 1);
                    }
                }
            });
        }

    }


    class OnPraiseClickListener implements View.OnClickListener {

        private ShineButton shineButton;
        private BrandStory brandStory;

        public OnPraiseClickListener(ShineButton shineButton, BrandStory brandStory) {
            this.shineButton = shineButton;
            this.brandStory = brandStory;
        }

        @Override
        public void onClick(View v) {
            if (brandStory.getIsdz() > 0) {
                return;
            }
            if (!(v instanceof ShineButton)) {
                shineButton.click();
            }
            ServiceManager.createGsonService(BrandStoryService.class)
                    .addPraise(UserCache.getInstance().getUser().getIds(), brandStory.getIds())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<BaseResponse>() {
                        @Override
                        public void accept(BaseResponse response) throws Exception {
                            if (response != null && BaseResponse.SUCCESS.equals(response.getStatus())) {
                                RxBus.getDefault().post(EventType.TYPE_PRAISE_ADDED, brandStory.getIds());
                                brandStory.setIsdz(1);
                            } else {
                                ViewUtils.showToastError(R.string.failed_add_praise);
                            }
                            notifyDataSetChanged();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable throwable) throws Exception {
                        }
                    });
        }
    }

    class OnDetailClickListener implements View.OnClickListener {

        private Context context;
        private BrandStory brandStory;

        public OnDetailClickListener(Context context, BrandStory brandStory) {
            this.brandStory = brandStory;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (ViewUtils.isFastClick()) return;
            Intent intent = new Intent(mActivity, BrandStoryActivity.class);
            intent.putExtra("brandStory", brandStory);
            mActivity.startActivity(intent);
        }
    }

    class OnCommentClickListener implements View.OnClickListener {

        private Context context;
        private BrandStory brandStory;

        public OnCommentClickListener(Context context, BrandStory brandStory) {
            this.brandStory = brandStory;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (ViewUtils.isFastClick()) return;
            Intent intent = new Intent(mActivity, BrandCommentAddActivity.class);
            intent.putExtra("brandId", brandStory.getIds());
            mActivity.startActivity(intent);
        }
    }


}