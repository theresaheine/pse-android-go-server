package edu.kit.pse.bdhkw.client.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

import edu.kit.pse.bdhkw.R;
import edu.kit.pse.bdhkw.client.communication.ObjectResponse;
import edu.kit.pse.bdhkw.client.communication.Response;
import edu.kit.pse.bdhkw.client.communication.SetAppointmentRequest;
import edu.kit.pse.bdhkw.client.controller.NetworkIntentService;
import edu.kit.pse.bdhkw.client.controller.database.GroupService;
import edu.kit.pse.bdhkw.client.model.objectStructure.GroupClient;
import edu.kit.pse.bdhkw.client.model.objectStructure.SimpleUser;

import static android.content.Context.MODE_PRIVATE;
import static edu.kit.pse.bdhkw.client.controller.NetworkIntentService.REQUEST_TAG;
import static edu.kit.pse.bdhkw.client.controller.NetworkIntentService.RESPONSE_TAG;


/**
 * Created by Schokomonsterchen on 12.01.2017.
 */

public class GroupAppointmentFragment extends Fragment implements View.OnClickListener {

    private String groupName;
    private GroupClient groupClient;
    private GroupService groupService;

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private Fragment groupMapFragment;

    private static final String TAG = GroupAppointmentFragment.class.getSimpleName();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Button gn = (Button) getView().findViewById(edu.kit.pse.bdhkw.R.id.groupname_button);
        //gn.setText(groupName);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(edu.kit.pse.bdhkw.R.layout.group_appointment_fragment, container, false);
        groupName = ((BaseActivity) getActivity()).getGroupname();
        groupService = new GroupService(getActivity());
        //TODO get name !!
        groupClient = groupService.readOneGroupRow(groupName);

        if (container != null) {
            container.removeAllViews();
        }
        view.findViewById(edu.kit.pse.bdhkw.R.id.groupname_button).setOnClickListener(this);
        view.findViewById(edu.kit.pse.bdhkw.R.id.appointment_button).setOnClickListener(this);
        view.findViewById(edu.kit.pse.bdhkw.R.id.time_button).setOnClickListener(this);
        view.findViewById(edu.kit.pse.bdhkw.R.id.date_button).setOnClickListener(this);
        view.findViewById(edu.kit.pse.bdhkw.R.id.place_button).setOnClickListener(this);
        view.findViewById(edu.kit.pse.bdhkw.R.id.next_appointment_button).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();



        if (goStatus()) {
            groupMapFragment = new GroupMapGoFragment();
        } else {
            groupMapFragment = new GroupMapNotGoFragment();
        }
        if (edu.kit.pse.bdhkw.R.id.groupname_button == id) {
            getFragmentManager().beginTransaction()
                    .replace(edu.kit.pse.bdhkw.R.id.group_container, new GroupMembersFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (edu.kit.pse.bdhkw.R.id.appointment_button == id) {
            getFragmentManager().beginTransaction()
                    .replace(edu.kit.pse.bdhkw.R.id.group_container, groupMapFragment)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } else if (edu.kit.pse.bdhkw.R.id.time_button == id) {
            showTimePickerDialog(view);

            SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            int hour = prefs.getInt(getString(R.string.selectedHour), 00);
            int min = prefs.getInt(getString(R.string.selectedMin), 00);

            groupClient.getAppointment().getAppointmentDate().setTime(hour + ":" + min);
            groupService.updateGroupData(groupClient.getGroupName(), groupClient);
        } else if (edu.kit.pse.bdhkw.R.id.date_button == id) {
            showDatePickerDialog(view);

            SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            int dd = prefs.getInt(getString(R.string.selectedDay), 01);
            int mM = prefs.getInt(getString(R.string.selectedMonth), 01);
            int yYYY = prefs.getInt(getString(R.string.selectedYear), 2000);

            groupClient.getAppointment().getAppointmentDate().setDate(dd + "." + mM + "." + yYYY);
        } else if (edu.kit.pse.bdhkw.R.id.place_button == id) {

            PlacePickerFragment ppf = new PlacePickerFragment();
            //ppf.setGo(goStatus());
            getFragmentManager().beginTransaction()
                    .replace(edu.kit.pse.bdhkw.R.id.group_container, ppf)
                    .addToBackStack(getTag())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

            SharedPreferences prefs = getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            String placeName = prefs.getString(getString(R.string.selectedPlace), "");
            double latitude = Double.longBitsToDouble(prefs.getLong(getString(R.string.selectedLatitude), 0));
            double longitude = Double.longBitsToDouble(prefs.getLong(getString(R.string.selectedLongitude), 0));

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            groupClient.getAppointment().setAppointmentDestination(placeName, geoPoint);


        } else if (edu.kit.pse.bdhkw.R.id.next_appointment_button == id) {
            // Start server request to update the appointment data of the group
            String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            SetAppointmentRequest setAppointmentRequest = new SetAppointmentRequest();
            setAppointmentRequest.setSenderDeviceId(deviceId);
            setAppointmentRequest.setTargetGroupName(groupName);
            setAppointmentRequest.setAppointment(groupClient.getAppointment().toSimpleAppointment());//TODO Appointment extends SimpleAppointment
            Intent intent = new Intent(getActivity().getApplicationContext(), NetworkIntentService.class);
            intent.putExtra(REQUEST_TAG, setAppointmentRequest);
            getActivity().startService(intent);
        }

    }

    private boolean goStatus() {
        //TODO: überprüfen, ob go gedrückt ist
        return false;
    }

    private void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        intentFilter = new IntentFilter(NetworkIntentService.BROADCAST_RESULT);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Response response = intent.getParcelableExtra(RESPONSE_TAG);
                try {
                    boolean successful = response.getSuccess();
                    Log.i(TAG, String.valueOf(successful));
                    if(successful) {
                        groupService.updateGroupData(groupClient.getGroupName(), groupClient);
                        //TODO Treffen auch im Fragment anpassen

                        Toast.makeText(context, getString(R.string.setAppointmentSuccessful), Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction()
                                .replace(edu.kit.pse.bdhkw.R.id.group_container, groupMapFragment)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                    } else {
                        Toast.makeText(context, getString(R.string.setAppointmentNotSuccessful), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

}