package com.example.memorizewords;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class WordClickListener implements View.OnClickListener {
    private String str;
    private RecyclerView.ViewHolder holder;

    public void setStr(String str) {
        this.str = str;
    }

    public WordClickListener(RecyclerView.ViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse("https://www.baidu.com/s?wd="
                + str);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        holder.itemView.getContext().startActivity(intent);
    }
}
