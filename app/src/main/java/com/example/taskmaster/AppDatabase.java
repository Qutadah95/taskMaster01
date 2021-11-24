//package com.example.taskmaster;
//
//import android.content.Context;
//
//import androidx.room.Database;
//import androidx.room.Room;
//import androidx.room.RoomDatabase;
//
//@Database(entities = {Task.class}, version = 1 ,exportSchema = false)
//public abstract class AppDatabase extends RoomDatabase {
//    public abstract TaskDAO taskDao();
//
//    public static AppDatabase  db;
//
//    public static AppDatabase getInstance(Context context){
//        if(db == null){
//            db = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,"tasks").allowMainThreadQueries().build();
//        }
//        return db;
//    }
//
//}