package edu.ecu.cs.pirateplaces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.ecu.cs.pirateplaces.database.PiratePlaceDbSchema.PiratePlaceTable;

/**
 * The open helper for the PirateBase SQLite database
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.0
 */
public class PirateBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "pirateBase.db";

    public PirateBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table " + PiratePlaceTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            PiratePlaceTable.Cols.UUID + ", " +
            PiratePlaceTable.Cols.PLACE_NAME + ", " +
            PiratePlaceTable.Cols.LAST_VISITED_DATE + "," +
            PiratePlaceTable.Cols.HAS_LOCATION+  "," +
            PiratePlaceTable.Cols.LATITUDE + "," +
            PiratePlaceTable.Cols.LONGITUDE + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
