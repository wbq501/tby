package com.baigu.dms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baigu.dms.R;
import com.baigu.dms.domain.model.City;

public class CitySelectorAdapter extends BaseRVAdapter<City> {
    private City mCitySelected;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_selector, parent, false);
        return new CitySelectorAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        City city = mDataList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.tvName.setText(city.getName());
        itemViewHolder.ivSel.setVisibility(mCitySelected != null && mCitySelected.getId().equals(city.getId()) ? View.VISIBLE : View.GONE);
    }

    public void setCitySelected(City city) {
        mCitySelected = city;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivSel;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_realname);
            ivSel = (ImageView) itemView.findViewById(R.id.iv_sel);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(CitySelectorAdapter.this, getAdapterPosition());
                    }
                }
            });
        }
    }
}
