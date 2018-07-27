package com.example.vidit.movienary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchlistOpenHelper extends SQLiteOpenHelper
{

    public static final String DATABASE_NAME="movienary_db";
    public static final int VERSION=1;
    private static WatchlistOpenHelper instance;
    public static WatchlistOpenHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WatchlistOpenHelper(context.getApplicationContext());
        }
        return instance;
    }
    private WatchlistOpenHelper(Context context)
    {
        super(context,DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        String taskSql="CREATE TABLE "+ContractMovies.Movie.TABLE_NAME+"("+ContractMovies.Movie.COLUMN_POSITION+" INTEGER PRIMARY KEY AUTOINCREMENT , "
                +ContractMovies.Movie.COLUMN_ID+" TEXT , "
                +ContractMovies.Movie.COLUMN_MOVIENAME+" TEXT , "+ContractMovies.Movie.COLUMN_BACKDROPPATH+" TEXT , "
                +ContractMovies.Movie.COLUMN_MEDIATYPE+" TEXT , "+ContractMovies.Movie.COLUMN_OVERVIEW+" TEXT , "
                +ContractMovies.Movie.COLUMN_RATING+" TEXT , "+ContractMovies.Movie.COLUMN_RELEASEDATE+" TEXT , "
                +ContractMovies.Movie.COLUMN_POSTERPATH+" TEXT )";
        sqLiteDatabase.execSQL(taskSql);

        taskSql="CREATE TABLE "+ContractTv.Tv.TABLE_NAME+"("+ContractTv.Tv.COLUMN_POSITION+" INTEGER PRIMARY KEY AUTOINCREMENT , "
                +ContractTv.Tv.COLUMN_ID+" TEXT , "
                +ContractTv.Tv.COLUMN_TVSHOWNAME+" TEXT , "+ContractTv.Tv.COLUMN_BACKDROPPATH+" TEXT , "
                +ContractTv.Tv.COLUMN_MEDIATYPE+" TEXT , "+ContractTv.Tv.COLUMN_OVERVIEW+" TEXT , "
                +ContractTv.Tv.COLUMN_RATING+" TEXT , "+ContractTv.Tv.COLUMN_FIRSTAIRDATE+" TEXT , "
                +ContractTv.Tv.COLUMN_POSTERPATH+" TEXT )";
        sqLiteDatabase.execSQL(taskSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
