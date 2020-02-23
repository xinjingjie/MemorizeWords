package com.example.memorizewords;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;

import com.example.memorizewords.bean.Word;
import com.example.memorizewords.factory.WordRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WordViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private SavedStateHandle savedStateHandle;
    private String shpName = "chinese_visitable";
    private String is_had = "is_had";
    private String is_card = "is_card";
    private String yes = "yes";

    public WordViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        wordRepository = new WordRepository(application.getApplicationContext());
        if ( !handle.contains(is_had) ) {
           SharedPreferences sharedPreferences = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            handle.set(is_had, sharedPreferences.getString(is_had, yes));
            handle.set(is_card, sharedPreferences.getString(is_card, yes));
            handle.set(is_card,yes);
            Map<String, ?> all_data = sharedPreferences.getAll();
            Log.d("WordViewModel", "WordViewModel: " + all_data);
            for (Map.Entry<String, ?> entry : all_data.entrySet()) {
                handle.set(entry.getKey(), entry.getValue());
            }//遍历所有sharedPreferences里的数据填充到handle里
        }
        this.savedStateHandle = handle;
    }
    public void clearSavedStateHandle(){

        Set<String> set = savedStateHandle.keys();
        Log.d("CCCCCC", "clearSavedStateHandle: "+set);
        List<String> list=new ArrayList();
        for (String s : set) {
            String key=s;
            if ( key!=is_card&&key!=is_had ){
                list.add(key);
            }
        }
        for (int i=0;i<list.size();i++){
            savedStateHandle.remove(list.get(i));
        }
    }
    public SavedStateHandle getSavedStateHandle() {
        return savedStateHandle;
    }

    public boolean getIsCard() {
        return savedStateHandle.getLiveData(is_card).getValue().equals(yes) ;
    }

    public void delete(String string) {
        savedStateHandle.remove(string);
    }

    public void save(String str, String i) {
        savedStateHandle.set(str, i);
    }

    public void saveAll() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.clear();
        Set<String> set = savedStateHandle.keys();
        for (String s : set) {
            String key = s;
            editor.putString(key, (String) savedStateHandle.getLiveData(key).getValue());
        }

        editor.apply();
    }

    public LiveData<List<Word>> getSimilar(String s) {
        return wordRepository.findSimilar(s);
    }

    public LiveData<List<Word>> getAllWords() {
        return wordRepository.getWordsLiveData();
    }

    public void addWord(Word... words) {
        Log.d("Add", "addWord: " + words.toString());
        wordRepository.addWord(words);
    }

    public boolean deleteWord(Word... words) {
        return wordRepository.deleteWord(words);
    }

    public boolean updateWord(Word... words) {
        return wordRepository.updateWord(words);
    }

    public boolean deleteAll() {
        return wordRepository.deleteAllWords();
    }

    public void resetId(){
        wordRepository.reset();
    }
}
