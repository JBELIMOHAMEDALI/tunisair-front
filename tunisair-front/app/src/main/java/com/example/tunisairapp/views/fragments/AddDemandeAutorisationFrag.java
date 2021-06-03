package com.example.tunisairapp.views.fragments;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;
import com.example.tunisairapp.models.DemandeAutorisation;
import com.example.tunisairapp.models.DemandeConge;
import com.example.tunisairapp.models.IapiConnexion;
import com.example.tunisairapp.models.User;
import com.example.tunisairapp.models.insertResponse;
import com.example.tunisairapp.views.activities.HomeActivity;
import com.example.tunisairapp.views.activities.MainActivity;

import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddDemandeAutorisationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddDemandeAutorisationFrag extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String BASE_URL="http://"+ Constants.IP_ADRESS+"/api_app_androide/authorization/";
    private static final String BASE_URL_CHECK="http://"+ Constants.IP_ADRESS+"/api_app_androide/";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button btnValid;
    private TextView tv_timer_from;
    private TextView tv_timer_to;
    private EditText edt_raison;
    private int minutesFrom,hoursFrom;
    private IapiConnexion apiCnx;
    private Calendar calendar = Calendar.getInstance();
    private TimePickerDialog timerpicker;
    private ProgressDialog progressDialog;
    public AddDemandeAutorisationFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddDemandeAutorisationFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddDemandeAutorisationFrag newInstance(String param1, String param2) {
        AddDemandeAutorisationFrag fragment = new AddDemandeAutorisationFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //btnValid=getActivity().findViewById(R.id.btn_ValidLogin);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_demande_autorisation, container, false);
        tv_timer_from=view.findViewById(R.id.tv_timer_from);
        tv_timer_to=view.findViewById(R.id.tv_timer_to);
        edt_raison=view.findViewById(R.id.edit_raison);

        int sysTemHours=calendar.get(Calendar.HOUR);
        int sysTemMinutes=calendar.get(Calendar.MINUTE);
        handleTimepicker(sysTemMinutes,sysTemHours);


        //apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);
        btnValid=view.findViewById(R.id.btn_ValidLogin);
        SharedPreferences preferences= getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String matricule = preferences.getString(Constants.USER_MATRICULE, null);
        tv_timer_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerpicker=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        handleTimepicker(minute,hourOfDay);
                    }
                },12,0,false);
                updatePicker(timerpicker);

            }
        });
        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgressBar();
                if(verifyData()){
                    checkAuth(matricule);
                }

            }
        });
        return view;
    }

    private void updatePicker(TimePickerDialog timepicker){
        timepicker.updateTime(hoursFrom,minutesFrom);
        timepicker.show();
    }

    private void handleTimepicker(int minute,int hourOfDay) {
        minutesFrom=minute;
        hoursFrom=hourOfDay;

        Calendar calendarFrom=Calendar.getInstance();
        calendarFrom.set(0,0,0,hoursFrom,minutesFrom);
        Calendar calendarTo=Calendar.getInstance();
        calendarTo.set(0,0,0,hoursFrom+2,minutesFrom);
        tv_timer_from.setText(DateFormat.format("hh:mm:aa",calendarFrom));
        tv_timer_to.setText(DateFormat.format("hh:mm:aa",calendarTo));
    }

    private boolean verifyData() {
        String from=edt_raison.getText().toString().trim();
        String reason=edt_raison.getText().toString().trim();
        boolean validator=true;
        if(from.isEmpty()){
            edt_raison.setError("Time cannot be empty");
            validator=false;
        }
        if(reason.isEmpty()){
            edt_raison.setError("Reason cannot be empty");
            validator=false;
        }
        return validator;
    }

    @Override
    public void onClick(View v) {

    }

    private void createDemandeAuth(String matricule){


        String from=tv_timer_from.getText().toString();
        String to=tv_timer_to.getText().toString();
        String raison=edt_raison.getText().toString();
        DemandeAutorisation dAuth=new DemandeAutorisation(from,to,raison);
        //method verify inputs
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", dAuth.getHeureDebut())
                .addFormDataPart("to", dAuth.getHeureFin())
                .addFormDataPart("description", dAuth.getRaison())
                .addFormDataPart("matricule", matricule)
                .build();
        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);
        Call<insertResponse> call=apiCnx.createRequestAuthorization(requestBody);
        call.enqueue(new Callback<insertResponse>() {
            @Override
            public void onResponse(Call<insertResponse> call, Response<insertResponse> response) {
                progressDialog.dismiss();
                if(response.body().isUpdate()&&response.body().isInsert()){

                    FragmentTransaction ft4 = getActivity().getSupportFragmentManager().beginTransaction();
                ft4.replace(R.id.frag1, new DemandeAutorisationFrag());
                //sweet alert
                ft4.commit();

                }else{
                    alertError("AUTHORIZATION REQUEST","Something went wrong!please retry");
                }


            }

            @Override
            public void onFailure(Call<insertResponse> call, Throwable t) {
                progressDialog.dismiss();
                alertError("AUTHORIZATION REQUEST","Something went wrong!please retry");
            }
        });
    }

    private void displayProgressBar() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    private void checkAuth(String matricule){
        apiCnx=Constants.initRetrofit(BASE_URL_CHECK).create(IapiConnexion.class);
        Call<List<User>> call=apiCnx.checkRequestAuthorization(matricule);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                final String balanceAuth=response.body().get(0).getAuthorizationbalance();
                if(!balanceAuth.equals("0")){
                    createDemandeAuth(matricule);
                }else{
                    progressDialog.dismiss();
                    alertError("ACCESS DENIED","You have already asked for an authorization");
                    FragmentTransaction ft4 = getActivity().getSupportFragmentManager().beginTransaction();
                    ft4.replace(R.id.frag1, new DemandeAutorisationFrag());
                    ft4.commit();
                }



            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

                alertError("SERVER","Something went wrong!please retry!");
                progressDialog.dismiss();
            }
        });

    }

    private void alertError(String title,String message){

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(title)
                .setMessage(message)
                //set positive button
                .setPositiveButton("OK", (dialogInterface, i1) -> {
                })

                .show();
    }
}