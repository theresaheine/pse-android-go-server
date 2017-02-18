package edu.kit.pse.bdhkw.client.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import edu.kit.pse.bdhkw.client.communication.SetAppointmentRequest;
import edu.kit.pse.bdhkw.client.controller.NetworkIntentService;
import edu.kit.pse.bdhkw.client.controller.database.GroupService;
import edu.kit.pse.bdhkw.client.model.objectStructure.GroupClient;
import edu.kit.pse.bdhkw.client.model.objectStructure.SimpleUser;

import static edu.kit.pse.bdhkw.client.controller.NetworkIntentService.REQUEST_TAG;
import static edu.kit.pse.bdhkw.client.controller.NetworkIntentService.RESPONSE_TAG;
import edu.kit.pse.bdhkw.R;
import edu.kit.pse.bdhkw.client.controller.database.GroupService;
import edu.kit.pse.bdhkw.client.model.objectStructure.GroupClient;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Schokomonsterchen on 12.01.2017.
 */

public class GroupAppointmentFragment extends Fragment implements View.OnClickListener {

    private GroupClient groupClient;
    private GroupService groupService;

    private IntentFilter intentFilter;
    private BroadcastReceiver broadcastReceiver;
    private Fragment groupMapFragment;

    private static final String TAG = GroupAppointmentFragment.class.getSimpleName();

    private GroupClient group;
    private Button groupName;
    private Button groupAppointment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(edu.kit.pse.bdhkw.R.layout.group_appointment_fragment, container, false);

        if (container != null) {
            container.removeAllViews();
        }

        defineGroup(view);

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

            int hour = 0;
            int min = 0;

            //TODO min von time picker
            groupClient.getAppointment().getAppointmentDate().setTime(hour + ":" + min);
            groupService.updateGroupData(groupClient.getGroupName(), groupClient);
        } else if (edu.kit.pse.bdhkw.R.id.date_button == id) {
            showDatePickerDialog(view);

            //TODO date from date picker
            int dd = 0;
            int mM = 0;
            int yYYY = 0;

            groupClient.getAppointment().getAppointmentDate().setDate(dd + "." + mM + "." + yYYY);
        } else if (edu.kit.pse.bdhkw.R.id.place_button == id) {

            //TODO: speichere place
            PlacePickerFragment ppf = new PlacePickerFragment();
            //ppf.setGo(goStatus());
            getFragmentManager().beginTransaction()
                    .replace(edu.kit.pse.bdhkw.R.id.group_container, ppf)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
            //TODO wieder zurück hier her kommen und longitude, latitude und name vom zielort zurück geben
            double latitude = 0;
            double longitude = 0;
            String placeName = "";

            GeoPoint geoPoint = new GeoPoint(latitude, longitude);
            groupClient.getAppointment().setAppointmentDestination(placeName, geoPoint);
        } else if (edu.kit.pse.bdhkw.R.id.next_appointment_button == id) {
            // Start server request to update the appointment data of the group
            String deviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            SetAppointmentRequest setAppointmentRequest = new SetAppointmentRequest();
            setAppointmentRequest.setSenderDeviceId(deviceId);
            setAppointmentRequest.setTargetGroupName(group.getGroupName());
            setAppointmentRequest.setAppointment(groupClient.getAppointment());//TODO Appointment extends SimpleAppointment
            Intent intent = new Intent(getActivity().getApplicationContext(), NetworkIntentService.class);
            intent.putExtra(REQUEST_TAG, setAppointmentRequest);
            getActivity().startService(intent);
        }

    }

    protected boolean goStatus() {
        return group.getGoService().getGoStatus();
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
                ObjectResponse objResp = intent.getParcelableExtra(RESPONSE_TAG);
                try {
                    boolean successful = objResp.getSuccess();
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
    private void defineGroup(View view) {
        groupName = (Button) view.findViewById(R.id.groupname_button);
        groupAppointment = (Button) view.findViewById(R.id.appointment_button);
        String name = this.getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE).
                getString(getString(R.string.groupname), "");
        GroupService groupService = new GroupService(getActivity().getApplicationContext());
        group = groupService.readOneGroupRow(name);
        groupName.setText(group.getGroupName());
        groupAppointment.setText(group.getAppointment().getAppointmentDestination().getDestinationName());
    }


}