package com.example.memorizewords;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.memorizewords.bean.Word;
import com.example.memorizewords.dao.WordDao;

@Database(entities = {Word.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static AppDatabase getINSTANCE(Context context) {
        if ( INSTANCE == null ) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "word")
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();


    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Word ADD COLUMN is_visitable INTEGER NOT NULL DEFAULT 1");
        }
    };
}
