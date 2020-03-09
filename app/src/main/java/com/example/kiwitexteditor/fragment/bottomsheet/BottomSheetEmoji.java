package com.example.kiwitexteditor.fragment.bottomsheet;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kiwitexteditor.R;
import com.example.kiwitexteditor.adapter.EmojiAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetEmoji extends BottomSheetDialogFragment {
    RecyclerView recyclerView;
    EmojiBottomSheetListener emojiBottomSheetListener;

    public BottomSheetEmoji() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.SheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottomsheet_dialog_emojii,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init recyclerview
        recyclerView = view.findViewById(R.id.rvEmojii);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new GridLayoutManager(getContext(),5,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager1);
        EmojiAdapter emojiAdapter = new EmojiAdapter(getContext());
        emojiAdapter.setListener(new EmojiAdapter.EmojiListener() {
            @Override
            public void onEmojiClick(String emojiCode) {
                Toast.makeText(getActivity(), "Click " + emojiCode, Toast.LENGTH_SHORT).show();
                emojiBottomSheetListener.onEmojiClick(emojiCode);
                dismiss();
            }
        });
        recyclerView.setAdapter(emojiAdapter);
    }
    public interface EmojiBottomSheetListener{
        void onEmojiClick(String emojiCode);
    }

    public void setEmojiBottomSheetListener(EmojiBottomSheetListener emojiBottomSheetListener) {
        this.emojiBottomSheetListener = emojiBottomSheetListener;
    }
}
