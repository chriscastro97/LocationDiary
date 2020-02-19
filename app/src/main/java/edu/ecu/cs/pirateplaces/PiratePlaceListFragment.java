package edu.ecu.cs.pirateplaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;

/**
 * Main fragment for the Pirate Place list
 *
 * @author Mark Hills (mhills@cs.ecu.edu)
 * @version 1.0
 */
public class PiratePlaceListFragment extends Fragment
{
    private static final int REQUEST_PIRATE_PLACE = 1;

    private RecyclerView mPiratePlaceRecyclerView;
    private PiratePlaceAdapter mAdapter;
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
    public interface Callbacks {
        void onPiratePlaceSelected(PiratePlace piratePlace);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_pirate_place_list, container, false);

        mPiratePlaceRecyclerView = (RecyclerView) view.findViewById(R.id.pirate_place_recycler_view);
        mPiratePlaceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    void updateUI() {
        PirateBase pirateBase = PirateBase.getPirateBase(getActivity());
        List<PiratePlace> piratePlaces = pirateBase.getPiratePlaces();

        if (mAdapter == null) {
            mAdapter = new PiratePlaceAdapter(piratePlaces);
            mPiratePlaceRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setPlaces(piratePlaces);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_pirate_place_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.new_place:
                PiratePlace piratePlace = new PiratePlace();
                PirateBase.getPirateBase(getActivity()).addPiratePlace(piratePlace);
                mCallbacks.onPiratePlaceSelected(piratePlace);
                return true;
            case R.id.map:
                Intent intent =  new Intent (getActivity(), PirateMapsActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        updateUI();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        if (requestCode == REQUEST_PIRATE_PLACE) {
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                UUID updatedId = PiratePlaceFragment.getUpdatedId(data);
//                if (updatedId != null) {
//                    PirateBase pirateBase = PirateBase.getPirateBase(getActivity());
//                    List<PiratePlace> piratePlaces = pirateBase.getPiratePlaces();
//                    PiratePlace updatedPlace = pirateBase.getPiratePlace(updatedId);
//                    int index = piratePlaces.indexOf(updatedPlace);
//                    if (index >= 0 && mAdapter != null) {
//                        mAdapter.notifyItemChanged(index);
//                    }
//                }
//            }
//        }
//    }

    private class PiratePlaceHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener
    {
        private PiratePlace mPiratePlace;
        private TextView mTitleTextView;
        private TextView mDateTextView;

        public PiratePlaceHolder(LayoutInflater inflater, ViewGroup parent)
        {
            super(inflater.inflate(R.layout.list_item_pirate_place, parent, false));

            mTitleTextView = (TextView) itemView.findViewById(R.id.pirate_place_name);
            mDateTextView = (TextView) itemView.findViewById(R.id.pirate_place_last_visited);

            itemView.setOnClickListener(this);
        }

        public void bind(PiratePlace piratePlace)
        {
            mPiratePlace = piratePlace;
            mTitleTextView.setText(mPiratePlace.getPlaceName());
            String lastVisitedDate = DateFormat.getDateFormat(getActivity()).format(mPiratePlace.getLastVisited());
            String lastVisitedTime = DateFormat.getTimeFormat(getActivity()).format(mPiratePlace.getLastVisited());
            mDateTextView.setText(lastVisitedDate + " " + lastVisitedTime);
        }

        @Override
        public void onClick(View v)
        {
            mCallbacks.onPiratePlaceSelected(mPiratePlace);
        }
    }

    private class PiratePlaceAdapter extends RecyclerView.Adapter<PiratePlaceHolder>
    {
        private List<PiratePlace> mPiratePlaces;

        public PiratePlaceAdapter(List<PiratePlace> piratePlaces)
        {
            mPiratePlaces = piratePlaces;
        }

        public void setPlaces(List<PiratePlace> places) {
            mPiratePlaces = places;
        }

        @Override
        public PiratePlaceHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PiratePlaceHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(PiratePlaceHolder holder, int position)
        {
            PiratePlace piratePlace = mPiratePlaces.get(position);
            holder.bind(piratePlace);
        }

        @Override
        public int getItemCount()
        {
            return mPiratePlaces.size();
        }
    }
}
