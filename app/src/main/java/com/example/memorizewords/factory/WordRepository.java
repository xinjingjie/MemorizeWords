package com.example.memorizewords.factory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.memorizewords.AppDatabase;
import com.example.memorizewords.bean.Word;
import com.example.memorizewords.dao.WordDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordRepository {
    private LiveData<List<Word>> wordsLiveData;
    private WordDao wordDao;
    private final static String TAG = "WordRepository";

    public WordRepository(Context context) {
        wordDao = AppDatabase.getINSTANCE(context).getWordDao();
        wordsLiveData = wordDao.getAllWords();
    }

    public LiveData<List<Word>> getWordsLiveData() {
        return wordsLiveData;
    }

    public void addWord(Word... words) {
        new AddWord(wordDao).execute(words);


    }
public  void reset(){
        new ResetId(wordDao).execute();
}
    public boolean deleteWord(Word... words) {
        try {
            return new DeleteWord(wordDao).execute(words).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "deleteWord: GOING WRONG!");
            return false;
        }

    }

    public boolean updateWord(Word... words) {
        try {
            return new UpdateWord(wordDao).execute(words).get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "updateWord: GOING WRONG!");
            return false;
        }

    }

    public boolean deleteAllWords() {
        try {
            return new DeleteAllWords(wordDao).execute().get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            Log.d(TAG, "deleteAllWords:GOING WRONG! ");
            return false;
        }

    }

    public LiveData<List<Word>> findSimilar(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            sb.append("%");
        }
        return wordDao.findSimilar("%" + sb);
    }

    static class AddWord extends AsyncTask<Word, Void, Void> {
        private WordDao wordDao;

        AddWord(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.addWord(words);
            return null;
        }
    }

    static class DeleteWord extends AsyncTask<Word, Void, Boolean> {
        private WordDao wordDao;

        DeleteWord(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Boolean doInBackground(Word... words) {
            try {
                wordDao.delete(words);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class UpdateWord extends AsyncTask<Word, Void, Boolean> {
        private WordDao wordDao;

        UpdateWord(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Boolean doInBackground(Word... words) {
            try {
                wordDao.updateWord(words);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }


    static class DeleteAllWords extends AsyncTask<Void, Void, Boolean> {
        private WordDao wordDao;

        DeleteAllWords(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                wordDao.deleteAll();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    static class ResetId extends AsyncTask<Void, Void, Void> {
        private WordDao wordDao;

        ResetId(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.reset();
            return null;
        }
    }
}
