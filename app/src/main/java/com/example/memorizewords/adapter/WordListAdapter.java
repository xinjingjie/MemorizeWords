package com.example.memorizewords.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorizewords.R;
import com.example.memorizewords.WordViewModel;
import com.example.memorizewords.bean.Word;

import java.util.ArrayList;
import java.util.List;

public class WordListAdapter extends ListAdapter<Word, WordListAdapter.MyViewHolder> {

    private WordViewModel wordViewModel;
    private boolean isCard;
    public WordListAdapter(boolean isCard, WordViewModel wordViewModel){
        super(new DiffUtil.ItemCallback<Word>() {
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {

                return oldItem.getId()==newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return (oldItem.getEnglish_mean().equals(newItem.getEnglish_mean()))
                        &&(oldItem.getChinese_mean().equals(newItem.getChinese_mean()));

            }
        });

        this.isCard = isCard;
        this.wordViewModel = wordViewModel;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView;
        if ( isCard ) {
            itemView = layoutInflater.inflate(R.layout.card_cell, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.normal_cell, parent, false);
        }
        return new WordListAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,final int position) {
        final Word word = getItem(position);
        Log.d("ALLWORD", "onBindViewHolder: " + word);
//        holder.id.setText(String.valueOf(word.getId()));
        holder.id.setText(String.valueOf(position+1));
        holder.chinese_mean.setText(word.getChinese_mean());
        holder.english_mean.setText(word.getEnglish_mean());
        if ( wordViewModel.getSavedStateHandle().contains(String.valueOf(word.getId())) ) {
            holder.chinese_mean.setVisibility(View.GONE);
            holder.aSwitch.setChecked(true);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://translate.google.cn/?hl=cn#view=home&op=translate&sl=en&tl=zh-CN&text="
                        + holder.english_mean.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    holder.chinese_mean.setVisibility(View.GONE);
                    word.setIs_visitable(true);
                    wordViewModel.save(holder.id.getText().toString(), "0");
                } else {
                    holder.chinese_mean.setVisibility(View.VISIBLE);
                    word.setIs_visitable(false);
                    //wordViewModel.save(holder.id.getText().toString(),1);
                }
            }
        });

    }

    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.id.setText(String.valueOf(holder.getAdapterPosition()+1));
    }

   public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView id, chinese_mean, english_mean;
        Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.aSwitch = itemView.findViewById(R.id.switch_button);
            this.id = itemView.findViewById(R.id.id);
            this.english_mean = itemView.findViewById(R.id.english_mean);
            this.chinese_mean = itemView.findViewById(R.id.chinese_mean);
        }
    }
}
