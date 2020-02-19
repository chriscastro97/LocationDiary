package edu.ecu.cs.pirateplaces;

import android.support.v4.app.Fragment;


public class PirateMapsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return   PirateMapsFragment.newInstance();
    }
}
