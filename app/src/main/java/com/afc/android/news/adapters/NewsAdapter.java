package com.afc.android.news.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afc.android.news.R;
import com.afc.android.news.model.NewsItem;
import com.afc.android.news.ui.NewsDetails;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hp on 12/31/2016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    public static final String LINK = "LINK";
    public static final String THUMBNAIL = "THUMBNAIL";
    public static final String TITLE = "TITLE";

    private Context mContext;
    private ArrayList<NewsItem> mNewsItems;

    public NewsAdapter(Context context, ArrayList<NewsItem> newsItems) {
        mContext = context;
        mNewsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        NewsItem item = mNewsItems.get(position);
        holder.bindNews(item);
    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle;
        private ImageView mThumbImage;
        private ImageView mLogoImageView;
        private TextView mPubDate;
        private CardView mCardView;
        //        private TextView mDescription;

        public NewsViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.titleText);
            mPubDate = (TextView) itemView.findViewById(R.id.dateText);
            mThumbImage = (ImageView) itemView.findViewById(R.id.thumbImage);
            mLogoImageView = (ImageView) itemView.findViewById(R.id.logoImageView);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
//            mDescription = (TextView) itemView.findViewById(R.id.descText);
        }

        public void bindNews(final NewsItem item) {
            mTitle.setText(item.getTitle());
            mPubDate.setText(item.getPubDate());
            Picasso.with(mContext).load(item.getThumbnail()).into(mThumbImage);
            YoYo.with(Techniques.FadeIn)
                    .duration(700)
                    .playOn(itemView.findViewById(R.id.cardView));
            mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, NewsDetails.class);
                    intent.putExtra(LINK, item.getLink());
                    intent.putExtra(THUMBNAIL, item.getThumbnail());
                    intent.putExtra(TITLE, item.getTitle());
                    mContext.startActivity(intent);
                }
            });
            mLogoImageView.setImageResource(R.drawable.aitnews_logo);
//            if(item.getLink().contains("https://aitnews.com")) {
//                mLogoImageView.setImageResource(R.drawable.aitnews_logo);
//            }
//            else if(item.getLink().contains("http://www.sharkiatoday.com")) {
//                mLogoImageView.setImageResource(R.drawable.sharkiatoday_logo);
//            }
//            else if(item.getLink().contains("https://ardroid.com")) {
//                mLogoImageView.setImageResource(R.drawable.ardroid_logo);
//            }
//            else if(item.getLink().contains("http://www.tahrirnews.com")) {
//                mLogoImageView.setImageResource(R.drawable.tahrir_logo);
//            }
//            mDescription.setText(item.getDescription());
        }
    }
}
