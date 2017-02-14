package edu.kit.pse.bdhkw.client.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Created by Schokomonsterchen on 15.01.2017.
 */

public class GroupMapNotGoFragment extends GroupMapFragment {

    private String groupname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        groupname = ((BaseActivity) getActivity()).getGroupname();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button gn = (Button) getView().findViewById(edu.kit.pse.bdhkw.R.id.groupname_button);
        gn.setText(groupname);
    }

    @Override
    protected View defineView(LayoutInflater inflater, ViewGroup container) {
        View view;

        groupname = ((BaseActivity) getActivity()).getGroupname();

        if (!goStatus()) {
            view = inflater.inflate(edu.kit.pse.bdhkw.R.layout.group_map_not_go_fragment, container, false);
        } else {
            getFragmentManager().beginTransaction()
                    .replace(edu.kit.pse.bdhkw.R.id.group_container, new GroupMapGoFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            view = inflater.inflate(edu.kit.pse.bdhkw.R.layout.group_map_go_fragment, container, false);
        }
        return view;
    }

    @Override
    protected void go(MapView mapView) {
        GroupMapGoFragment groupMapGoFragment = new GroupMapGoFragment();
        groupMapGoFragment.setActuallView(((GeoPoint) mapView.getMapCenter()), mapView.getZoomLevel());
        getFragmentManager().beginTransaction()
                .replace(edu.kit.pse.bdhkw.R.id.group_container, groupMapGoFragment)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
