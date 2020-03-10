package com.example.kiwitexteditor.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.Imageholder> {
    List<Bitmap> stickerList = new ArrayList<>();
    AddStickerListener listener;

    public AddImageAdapter(Activity activity) {
        stickerList = getListImagesBitMap(activity);
    }

    @NonNull
    @Override
    public Imageholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sticker,parent,false);
        return new Imageholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Imageholder holder, int position) {
        final Bitmap sticker = stickerList.get(position);
          holder.binData(sticker);
          holder.itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  if(listener!=null){
                      listener.onStickerClick(sticker);
                  }
              }
          });
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }

    public class Imageholder extends RecyclerView.ViewHolder{
        ImageView ivSticker;
        public Imageholder(@NonNull View itemView) {
            super(itemView);
            ivSticker = itemView.findViewById(R.id.ivSticker);
        }
        public void binData(Bitmap imageRource){
            ivSticker.setImageBitmap(imageRource);
        }
    }
    public static ArrayList<Integer> getSticker(){
        ArrayList<Integer> arr = new ArrayList<>();
        arr.add(R.drawable.aa);
        arr.add(R.drawable.bb);
        return arr;
    }
    public interface AddStickerListener{
        void onStickerClick(Bitmap imageResource);
    }

    public void setListener(AddStickerListener listener) {
        this.listener = listener;
    }
    public static ArrayList<Bitmap> getListImagesBitMap(Activity activity) {
        ArrayList<Bitmap> arrbitmap = new ArrayList<>();
        Uri uri;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        String PathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            PathOfImage = cursor.getString(column_index_data);
            listOfAllImages.add(PathOfImage);
            Bitmap bitmap = getBitmap(PathOfImage);
            arrbitmap.add(bitmap);
        }
        return arrbitmap;
    }

    public static Bitmap getBitmap(String path) {
        Bitmap bitmap=null;
        try {
            File f= new File(path);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap ;
    }
}
