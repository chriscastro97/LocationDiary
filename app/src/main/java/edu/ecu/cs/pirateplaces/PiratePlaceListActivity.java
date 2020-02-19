package edu.ecu.cs.pirateplaces;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Main activity for the Pirate Place list
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.0
 */
public class PiratePlaceListActivity extends SingleFragmentActivity
        implements PiratePlaceFragment.Callbacks, PiratePlaceListFragment.Callbacks
{
    @Override
    protected Fragment createFragment()
    {
        return new PiratePlaceListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onPiratePlaceSelected(PiratePlace piratePlace)
    {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = PiratePlaceActivity.newIntent(this, piratePlace.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = PiratePlaceFragment.newInstance(piratePlace.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onPiratePlaceUpdated(PiratePlace piratePlace)
    {
        PiratePlaceListFragment listFragment = (PiratePlaceListFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onPiratePlaceDeleted(PiratePlace piratePlace)
    {
        FragmentManager fm = getSupportFragmentManager();
        PiratePlaceListFragment listFragment = (PiratePlaceListFragment)
                fm.findFragmentById(R.id.fragment_container);
        listFragment.updateUI();

        if (findViewById(R.id.detail_fragment_container) != null) {
            Fragment fragment = fm.findFragmentById(R.id.detail_fragment_container);
            getSupportFragmentManager().beginTransaction()
                    .remove(fragment)
                    .commit();
        }
    }

}
