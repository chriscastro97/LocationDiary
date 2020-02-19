package edu.ecu.cs.pirateplaces;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Displays all the Pirate Places, one by one
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.1
 */
public class PiratePlacesActivity extends AppCompatActivity
{
    private TextView mPlaceNameTextView;
    private TextView mLastVisitedTextView;
    private Button mNextButton;
    private Button mPrevButton;

    private static final int REQUEST_CODE_EDIT = 0;
    private static final String KEY_INDEX = "index";

    private int mCurrentPlaceIndex;
    private PirateBase mPirateBase;

    /**
     * Updates the UI when a new Pirate Place is selected
     */
    private void updatePlaceInfo()
    {
        List<PiratePlace> places = mPirateBase.getPiratePlaces();
        PiratePlace currentPlace = places.get(mCurrentPlaceIndex);
        mPlaceNameTextView.setText(currentPlace.getPlaceName());
        mLastVisitedTextView.setText(currentPlace.getLastVisited().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pirate_places);

        mPirateBase = PirateBase.getPirateBase(this);

        if (savedInstanceState != null) {
            mCurrentPlaceIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mPlaceNameTextView = findViewById(R.id.pirate_place_name);
        mLastVisitedTextView = findViewById(R.id.pirate_place_last_visited);
        mNextButton = findViewById(R.id.next_button);
        mPrevButton = findViewById(R.id.prev_button);

        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<PiratePlace> piratePlaces = mPirateBase.getPiratePlaces();
                if (mCurrentPlaceIndex == (piratePlaces.size() - 1)) {
                    Toast.makeText(PiratePlacesActivity.this, R.string.already_at_end, Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentPlaceIndex += 1;
                    updatePlaceInfo();
                }
            }
        });

        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mCurrentPlaceIndex == 0) {
                    Toast.makeText(PiratePlacesActivity.this, R.string.already_at_start, Toast.LENGTH_SHORT).show();
                } else {
                    mCurrentPlaceIndex -= 1;
                    updatePlaceInfo();
                }
            }
        });

        mLastVisitedTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                List<PiratePlace> piratePlaces = mPirateBase.getPiratePlaces();
                Intent intent = PiratePlaceActivity.newIntent(
                        PiratePlacesActivity.this,
                        piratePlaces.get(mCurrentPlaceIndex).getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        updatePlaceInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_EDIT) {
            updatePlaceInfo();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_INDEX, mCurrentPlaceIndex);
    }
}
