package com.example.kiwitexteditor.adapter.filter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.kiwitexteditor.R;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import ja.burhanrashid52.photoeditor.PhotoFilter;

/**
 * @author <a href="https://github.com/burhanrashid52">Burhanuddin Rashid</a>
 * @version 0.1.2
 * @since 5/23/2018
 */
public class FilterViewAdapter extends RecyclerView.Adapter<FilterViewAdapter.ViewHolder> {

    private FilterListener mFilterListener;
    private List<Pair<Integer, PhotoFilter>> mPairList = new ArrayList<>();

    public FilterViewAdapter(FilterListener filterListener) {
        mFilterListener = filterListener;
        setupFilters();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_filter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pair<Integer, PhotoFilter> filterPair = mPairList.get(position);
        holder.mImageFilterView.setImageResource(filterPair.first);
        holder.mTxtFilterName.setText(filterPair.second.name().replace("_", " "));
    }

    @Override
    public int getItemCount() {
        return mPairList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageFilterView;
        TextView mTxtFilterName;

        ViewHolder(View itemView) {
            super(itemView);
            mImageFilterView = itemView.findViewById(R.id.imgFilterView);
            mTxtFilterName = itemView.findViewById(R.id.txtFilterName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFilterListener.onFilterSelected(mPairList.get(getLayoutPosition()).second);
                }
            });
        }
    }

    private Bitmap getBitmapFromAsset(Context context, String strName) {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
            return BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupFilters() {
        mPairList.add(new Pair<>(R.drawable.original, PhotoFilter.NONE));
        mPairList.add(new Pair<>(R.drawable.auto_fix, PhotoFilter.AUTO_FIX));
        mPairList.add(new Pair<>(R.drawable.brightness, PhotoFilter.BRIGHTNESS));
        mPairList.add(new Pair<>(R.drawable.contrast, PhotoFilter.CONTRAST));
        mPairList.add(new Pair<>(R.drawable.documentary, PhotoFilter.DOCUMENTARY));
        mPairList.add(new Pair<>(R.drawable.dual_tone, PhotoFilter.DUE_TONE));
        mPairList.add(new Pair<>(R.drawable.fill_light, PhotoFilter.FILL_LIGHT));
        mPairList.add(new Pair<>(R.drawable.fish_eye, PhotoFilter.FISH_EYE));
        mPairList.add(new Pair<>(R.drawable.grain, PhotoFilter.GRAIN));
        mPairList.add(new Pair<>(R.drawable.gray_scale, PhotoFilter.GRAY_SCALE));
        mPairList.add(new Pair<>(R.drawable.lomish, PhotoFilter.LOMISH));
        mPairList.add(new Pair<>(R.drawable.negative, PhotoFilter.NEGATIVE));
        mPairList.add(new Pair<>(R.drawable.posterize, PhotoFilter.POSTERIZE));
        mPairList.add(new Pair<>(R.drawable.saturate, PhotoFilter.SATURATE));
        mPairList.add(new Pair<>(R.drawable.sepia, PhotoFilter.SEPIA));
        mPairList.add(new Pair<>(R.drawable.sharpen, PhotoFilter.SHARPEN));
        mPairList.add(new Pair<>(R.drawable.temprature, PhotoFilter.TEMPERATURE));
        mPairList.add(new Pair<>(R.drawable.tint, PhotoFilter.TINT));
        mPairList.add(new Pair<>(R.drawable.vignette, PhotoFilter.VIGNETTE));
        mPairList.add(new Pair<>(R.drawable.cross_process, PhotoFilter.CROSS_PROCESS));
        mPairList.add(new Pair<>(R.drawable.b_n_w, PhotoFilter.BLACK_WHITE));
        mPairList.add(new Pair<>(R.drawable.flip_horizental, PhotoFilter.FLIP_HORIZONTAL));
        mPairList.add(new Pair<>(R.drawable.flip_vertical, PhotoFilter.FLIP_VERTICAL));
        mPairList.add(new Pair<>(R.drawable.rotate, PhotoFilter.ROTATE));
    }
}
