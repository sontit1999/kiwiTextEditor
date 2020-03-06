package com.example.kiwitexteditor.callback;

import com.example.kiwitexteditor.base.CBAdapter;
import com.example.kiwitexteditor.model.Photo;
import com.example.kiwitexteditor.model.PictureFacer;

public interface PhotoCallback extends CBAdapter {
    void onPhotoClick(PictureFacer pictureFacer);
}
