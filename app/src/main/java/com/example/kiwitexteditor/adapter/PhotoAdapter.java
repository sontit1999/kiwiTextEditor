package com.example.kiwitexteditor.adapter;

import com.example.kiwitexteditor.BR;
import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.base.BaseAdapter;
import com.example.kiwitexteditor.base.CBAdapter;
import com.example.kiwitexteditor.callback.PhotoCallback;
import com.example.kiwitexteditor.databinding.ItemPhotoBinding;
import com.example.kiwitexteditor.model.Photo;
import com.example.kiwitexteditor.model.PictureFacer;

public class PhotoAdapter extends BaseAdapter<PictureFacer, ItemPhotoBinding> {
    PhotoCallback callback;
    @Override
    public int getLayoutId() {
        return R.layout.item_photo;
    }

    @Override
    public int getIdVariable() {
        return BR.image;
    }

    @Override
    public int getIdVariableOnclick() {
        return BR.callback;
    }

    @Override
    public CBAdapter getOnclick() {
        return callback;
    }

    public void setCallback(PhotoCallback callback) {
        this.callback = callback;
    }
}
