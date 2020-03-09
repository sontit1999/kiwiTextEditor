package com.example.kiwitexteditor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import ja.burhanrashid52.photoeditor.PhotoEditor;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiHoder> {
    List<String> arrEmojis = new ArrayList<>();
    Context context;
    EmojiListener listener;

    public EmojiAdapter() {
    }

    public EmojiAdapter(Context context) {
        this.context = context;
        arrEmojis = PhotoEditor.getEmojis(context);
    }

    @NonNull
    @Override
    public EmojiHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emoji,parent,false);
        return new EmojiHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmojiHoder holder, final int position) {
         holder.binData(arrEmojis.get(position));
         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 listener.onEmojiClick(arrEmojis.get(position));
             }
         });
    }

    @Override
    public int getItemCount() {
        return arrEmojis.size();
    }

    class EmojiHoder extends RecyclerView.ViewHolder{
        TextView tvEmoji;
        public EmojiHoder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = (TextView) itemView.findViewById(R.id.tvEmoji);
        }
        public void binData(String emojiCode){
            tvEmoji.setText(emojiCode);
        }
    }
    public interface EmojiListener{
        void onEmojiClick(String emojiCode);
    }

    public void setListener(EmojiListener listener) {
        this.listener = listener;
    }
}
