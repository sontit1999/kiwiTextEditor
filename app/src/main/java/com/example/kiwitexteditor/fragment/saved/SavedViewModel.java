package com.example.kiwitexteditor.fragment.saved;

import androidx.lifecycle.MutableLiveData;

import com.example.kiwitexteditor.adapter.PhotoAdapter;
import com.example.kiwitexteditor.base.BaseViewmodel;
import com.example.kiwitexteditor.model.PictureFacer;

import java.util.ArrayList;

public class SavedViewModel extends BaseViewmodel {
    public com.example.kiwitexteditor.adapter.PhotoAdapter PhotoAdapter = new PhotoAdapter();
    MutableLiveData<ArrayList<PictureFacer>> arrPhoto = new MutableLiveData<>();
    public MutableLiveData<ArrayList<PictureFacer>> getArrPhoto(){
        return arrPhoto;
    }
    public void setPhoto(ArrayList<PictureFacer> arrayList){
        arrPhoto.postValue(arrayList);
    }

}
