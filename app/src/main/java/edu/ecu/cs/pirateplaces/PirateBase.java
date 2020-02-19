package edu.ecu.cs.pirateplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import edu.ecu.cs.pirateplaces.database.PirateBaseHelper;
import edu.ecu.cs.pirateplaces.database.PirateCursorWrapper;
import edu.ecu.cs.pirateplaces.database.PiratePlaceDbSchema;

import static edu.ecu.cs.pirateplaces.database.PiratePlaceDbSchema.*;

/**
 * Maintain the collection of Pirate Places
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.2
 */
public class PirateBase
{
    /** The singleton holding the {@link PiratePlace} information */
    private static PirateBase sPirateBase;

    /** The context for creation of the singleton/database */
    private Context mContext;

    /** Handle to the database */
    private SQLiteDatabase mDatabase;

    /**
     * Return the PirateBase singleton
     *
     * @param context the context for the call
     * @return the PirateBase singleton
     */
    public static PirateBase getPirateBase(Context context)
    {
        if (sPirateBase == null) {
            sPirateBase = new PirateBase(context);
        }
        return sPirateBase;
    }

    /**
     * Create a new instance of the PirateBase
     *
     * @param context the context for the call
     */
    private PirateBase(Context context)
    {
        mContext = context.getApplicationContext();
        mDatabase = new PirateBaseHelper(mContext).getWritableDatabase();
    }

    /**
     * Return the list of {@link PiratePlace} objects
     *
     * @return the list of {@link PiratePlace} objects
     */
    public List<PiratePlace> getPiratePlaces()
    {
        List<PiratePlace> piratePlaces = new ArrayList<>();

        PirateCursorWrapper cursor = queryPiratePlaces(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                piratePlaces.add(cursor.getPiratePlace());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return piratePlaces;
    }

    /**
     * Given an id, return the {@link PiratePlace} with that id, or null if
     * no {@link PiratePlace} is found.
     *
     * @param id the id to look up
     *
     * @return the {@link PiratePlace} with that id, or null if none exists
     */
    public PiratePlace getPiratePlace(UUID id)
    {
        PirateCursorWrapper cursor = queryPiratePlaces(
                PiratePlaceTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPiratePlace();
        } finally {
            cursor.close();
        }
    }

    /**
     * Add a new {@link PiratePlace} into the PirateBase
     *
     * @param piratePlace the {@link PiratePlace} to add
     */
    public void addPiratePlace(PiratePlace piratePlace)
    {
        ContentValues values = PirateBase.getContentValues(piratePlace);
        mDatabase.insert(PiratePlaceTable.NAME, null, values);
    }

    /**
     * Update an existing {@link PiratePlace} in the PirateBase
     *
     * @param piratePlace the {@link PiratePlace} to update
     */
    public void updatePiratePlace(PiratePlace piratePlace)
    {
        String uuidString = piratePlace.getId().toString();
        ContentValues values = PirateBase.getContentValues(piratePlace);

        mDatabase.update(PiratePlaceTable.NAME, values,
                PiratePlaceTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    public void deletePiratePlace(PiratePlace piratePlace)
    {
        String uuidString = piratePlace.getId().toString();

        mDatabase.delete(PiratePlaceTable.NAME,
                PiratePlaceTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    /**
     * Query the PiratePlaces table, filtering using the given WHERE information
     *
     * @param whereClause the where clause to use in our filtering
     * @param whereArgs any arguments needed in the where clause
     *
     * @return a {@link Cursor} with the resulting values
     */
    private PirateCursorWrapper queryPiratePlaces(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDatabase.query(
                PiratePlaceTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null);

        return new PirateCursorWrapper(cursor);
    }

    /**
     * Create a {@link ContentValues} instance based on the given {@link PiratePlace}
     *
     * @param piratePlace the {@link PiratePlace} to convert into a {@link ContentValues} object
     *
     * @return the resulting {@link ContentValues} instance
     */
    private static ContentValues getContentValues(PiratePlace piratePlace)
    {
        ContentValues values = new ContentValues();
        values.put(PiratePlaceTable.Cols.UUID, piratePlace.getId().toString());
        values.put(PiratePlaceTable.Cols.PLACE_NAME, piratePlace.getPlaceName());
        values.put(PiratePlaceTable.Cols.LAST_VISITED_DATE, piratePlace.getLastVisited().getTime());
        values.put(PiratePlaceTable.Cols.HAS_LOCATION,piratePlace.doesHaslocation());
        values.put(PiratePlaceTable.Cols.LATITUDE,piratePlace.getLatitude());
        values.put(PiratePlaceTable.Cols.LONGITUDE,piratePlace.getLongitude());
        return values;
    }

    public File getPhotoFileDir(PiratePlace piratePlace)
    {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, piratePlace.getPhotoFilenameDir());
    }

    public List<File> getPhotoFiles(PiratePlace piratePlace)
    {
        File filesDir = getPhotoFileDir(piratePlace);

        if (! filesDir.exists() || ! filesDir.isDirectory() ) {
            return new ArrayList<>();
        }

        File[] files = filesDir.listFiles();
        List<File> fileList = new ArrayList<>();
        for (int i = 0 ; i < files.length; ++i) {
            fileList.add(files[i]);
        }

        return fileList;
    }

    public File getNewPhotoFile(PiratePlace piratePlace)
    {
        File filesDir = getPhotoFileDir(piratePlace);
        if (! filesDir.exists()) {
            filesDir.mkdirs();
        }
        String newFileName = "IMG_" + UUID.randomUUID().toString() + ".jpg";
        return new File(filesDir, newFileName);
    }
}
