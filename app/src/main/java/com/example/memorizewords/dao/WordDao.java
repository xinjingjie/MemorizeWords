package com.example.memorizewords.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.memorizewords.bean.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void addWord(Word... words);

    @Delete
    void delete(Word... words);

    @Update
    void updateWord(Word... words);

    @Query("DELETE FROM WORD;")
    void deleteAll();

    @SuppressWarnings("AndroidUnresolvedRoomSqlReference")
    @Query("DELETE FROM sqlite_sequence;")
    void reset();

    @Query("SELECT * FROM WORD")
    LiveData<List<Word>> getAllWords();

//    @Query("UPDATE WORD SET is_visitable=:pattern WHERE id=:id")
//    void setIsVisitable(int id,boolean pattern);

    @Query("SELECT * FROM WORD WHERE english_mean LIKE :pattern")
    LiveData<List<Word>> findSimilar(String pattern);
}
