package edu.ecu.cs.pirateplaces.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import edu.ecu.cs.pirateplaces.PiratePlace;

import static edu.ecu.cs.pirateplaces.database.PiratePlaceDbSchema.PiratePlaceTable;

/**
 * The cursor wrapper for the PirateBase SQLite database
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.0
 */
public class PirateCursorWrapper extends CursorWrapper
{
    public PirateCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    public PiratePlace getPiratePlace()
    {
        String uuidString = getString(getColumnIndex(PiratePlaceTable.Cols.UUID));
        String placeName = getString(getColumnIndex(PiratePlaceTable.Cols.PLACE_NAME));
        long lastVisitedDate = getLong(getColumnIndex(PiratePlaceTable.Cols.LAST_VISITED_DATE));
        boolean hasLocation = getInt(getColumnIndex(PiratePlaceTable.Cols.HAS_LOCATION)) == 1;
        double latitude = getDouble(getColumnIndex(PiratePlaceTable.Cols.LATITUDE));
        double longitude = getDouble(getColumnIndex(PiratePlaceTable.Cols.LONGITUDE));


        PiratePlace piratePlace = new PiratePlace(UUID.fromString(uuidString));
        piratePlace.setPlaceName(placeName);
        piratePlace.setLastVisited(new Date(lastVisitedDate));
        piratePlace.setHas_location(hasLocation);
        piratePlace.setLatitude(latitude);
        piratePlace.setlongitude(longitude);


        return piratePlace;
    }

}
