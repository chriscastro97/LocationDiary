package edu.ecu.cs.pirateplaces;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.UUID;

/**
 * Edit a PiratePlace item
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.1
 */
public class PiratePlaceActivity extends SingleFragmentActivity implements PiratePlaceFragment.Callbacks
{
    private static final String EXTRA_PIRATE_PLACE_ID = "edu.ecu.cs.pirateplaces.piratePlaceId";
    private  static final int REQUEST_ERROR = 0;

    public static Intent newIntent(Context packageContext, UUID id) {
        Intent intent = new Intent(packageContext, PiratePlaceActivity.class);
        intent.putExtra(EXTRA_PIRATE_PLACE_ID, id);
        return intent;
    }

    @Override
    protected Fragment createFragment()
    {
        UUID id = (UUID)getIntent().getSerializableExtra(EXTRA_PIRATE_PLACE_ID);
        return PiratePlaceFragment.newInstance(id);
    }

    protected void onResume() {
        super.onResume();

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailability
                    .getErrorDialog(this, errorCode, REQUEST_ERROR,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    //Leave if services are unavailable.
                                    finish();
                                }
                            });
            errorDialog.show();
        }

    }




    @Override
    public void onPiratePlaceUpdated(PiratePlace piratePlace)
    {

    }

    @Override
    public void onPiratePlaceDeleted(PiratePlace piratePlace)
    {
        this.finish();
    }





}
