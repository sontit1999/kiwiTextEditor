package com.example.kiwitexteditor.fragment.bottomsheet;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.adapter.AddImageAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class BottomSheetAddImage extends BottomSheetDialogFragment {
    RecyclerView rvImage;
    StickerListener stickerListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set full screen and background transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bottomsheet_addsticker,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvImage = (RecyclerView) view.findViewById(R.id.rvImage);
        // init recyclerview
        rvImage.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
        rvImage.setLayoutManager(layoutManager);
        StickerAdapter adapter = new StickerAdapter();
        rvImage.setAdapter(adapter);

    }
    public interface StickerListener{
        void onClickImage(Bitmap imageResource);
    }

    public void setStickerListener(StickerListener stickerListener) {
        this.stickerListener = stickerListener;
    }
    public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

        int[] stickerList = new int[]{R.drawable.aa, R.drawable.bb,R.drawable.cute5,R.drawable.cute16};

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.imgSticker.setImageResource(stickerList[position]);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(stickerListener!=null){
                        stickerListener.onClickImage(BitmapFactory.decodeResource(getResources(),
                                stickerList[position]));
                    }
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return stickerList.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imgSticker;
            ViewHolder(View itemView) {
                super(itemView);
                imgSticker = itemView.findViewById(R.id.ivSticker);
            }
    }
    }
}
