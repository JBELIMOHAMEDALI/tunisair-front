package com.example.tunisairapp.views.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Session2Command;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;
import com.example.tunisairapp.models.DemandeConge;
import com.example.tunisairapp.models.IapiConnexion;
import com.example.tunisairapp.models.RealPathUtil;
import com.example.tunisairapp.models.User;
import com.example.tunisairapp.models.insertResponse;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewDemandeCongeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewDemandeCongeFrag extends Fragment {
    private IapiConnexion apiCnx;
    private static final String BASE_URL="http://"+ Constants.IP_ADRESS+"/api_app_androide/leave/";
    private static final String BASE_URL_CHECK="http://"+ Constants.IP_ADRESS+"/api_app_androide/";
    Spinner dropdown;
    Button btnUploadFile, btnValidateLeaveReq;
    Intent myFileIntent;
    TextView txtPathFile;
    EditText fromDate,daysEdit,  edtDescription;
    LinearLayout layoutFile;
    DatePickerDialog dialog;
    private String currentLeave;
    private ProgressDialog progressDialog;
    private String currentFilepath="";
    // For validators
    private Pattern pattern;
    private Matcher matcher;

    private static final String DATE_PATTERN =
            "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    final Calendar myCalendar = Calendar.getInstance();
    MultipartBody.Part fileImage = null;
    private RequestBody uploadFormData=null;
    public AddNewDemandeCongeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewDemandeCongeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewDemandeCongeFrag newInstance(String param1, String param2) {
        AddNewDemandeCongeFrag fragment = new AddNewDemandeCongeFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_new_demande_conge, container, false);

        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);

        initView(rootView);
        updateLabel(fromDate,true);
        // Listeners
        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_GRANTED){
                    openGallery();
                }
                else{
                    requestPermissions(new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10);

                }
            }
        });
        SharedPreferences preferences= getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String matricule = preferences.getString(Constants.USER_MATRICULE, null);
        btnValidateLeaveReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProgressBar();
                String currentLeave=dropdown.getSelectedItem().toString();
                if(verifyData(currentLeave)) {
                    if (currentLeave.equals("Annual leave")) {
                        checkAuth(matricule);
                    } else {
                        createDemandeConge(matricule, getNumberofdays());
                    }
                }
            }
        });


        layoutFile.setVisibility(View.INVISIBLE);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dropDownListener(parent,position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing happen
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(fromDate,false);


            }

        };

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               dialog=new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
               dialog.getDatePicker().setMinDate(System.currentTimeMillis());
               dialog.show();
            }
        });

        return rootView;
    }

    private void openGallery() {
        myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        myFileIntent.setType("image/*");
        startActivityForResult(myFileIntent, 10);
    }

    private boolean verifyData(String currentType) {
        String days=daysEdit.getText().toString().trim();
        int numberOfDays;
        String from=fromDate.getText().toString().trim();
        String desc=edtDescription.getText().toString().trim();
        boolean validator=true;


        if(days.isEmpty()&&(daysEdit.getVisibility()==View.VISIBLE)){
            daysEdit.setError("days cannot be empty");
            daysEdit.setFocusable(true);
            validator=false;
        }
        if(desc.isEmpty()){
            edtDescription.setError("Description cannot be empty");
            edtDescription.setFocusable(true);
            validator=false;
        }


if(daysEdit.getVisibility()==View.VISIBLE&&!days.isEmpty()){
    numberOfDays = Integer.parseInt(days);
    switch (currentType) {

        case "Annual leave":
            if (numberOfDays < 1 || numberOfDays > 26) {
                daysEdit.setError("Annual leave between 1 and 26 days");
                daysEdit.setFocusable(true);

                validator = false;
            }
            break;
        case "Medical leave":
            if (numberOfDays < 1 || numberOfDays > 5) {
                daysEdit.setError("Medical leave between 1 and 5 days");
                daysEdit.setFocusable(true);
                validator = false;
            }
            if (currentFilepath.equals("")) {
                txtPathFile.setError("File is required in medical leave ");
                txtPathFile.setFocusable(true);
                validator = false;
            }
            break;
        case "Maternity leave":
            if (numberOfDays < 1 || numberOfDays > 60) {
                daysEdit.setError("Maternity leave between 1 and 60 days");
                daysEdit.setFocusable(true);
                validator = false;
            }
            break;
}
        }
        return validator;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==10&&resultCode==getActivity().RESULT_OK) {
            Uri uri=data.getData();
            String path= RealPathUtil.getRealPath(getContext(),uri);
            currentFilepath=path;
            txtPathFile.setText("Image selected");
            txtPathFile.setError(null);
            txtPathFile.setFocusable(false);
        }

    }





    private void createDemandeConge(String matricule,int numberOfDays){
        RequestBody FormDatarequest;
        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);
        Call<insertResponse> call;
        DemandeConge dConge=new DemandeConge();
        dConge.setDateDebut(getToDate(0));
        dConge.setDatefin(getToDate(numberOfDays));
        dConge.setRaison(edtDescription.getText().toString());
        int id_type_leave=dropdown.getSelectedItemPosition()+1;
        if(currentLeave.equals("Medical leave")){
            requestWithupload(currentFilepath,dConge,matricule,String.valueOf(id_type_leave));
            File file = new File(currentFilepath);

            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/jpg"),
                            file
                    );
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("justification", file.getName(), requestFile);
            RequestBody from = RequestBody.create(MediaType.parse("text/plain"), dConge.getDateDebut());
            RequestBody to = RequestBody.create(MediaType.parse("text/plain"), dConge.getDatefin());
            RequestBody description = RequestBody.create(MediaType.parse("text/plain"), dConge.getRaison());
            RequestBody mat = RequestBody.create(MediaType.parse("text/plain"), matricule);
            RequestBody id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(id_type_leave));
            call=apiCnx.createRequestLeaveUpload(body,from,to,description,mat,id);
        }else{
            FormDatarequest= simpleRequest(dConge,matricule,String.valueOf(id_type_leave));
            call=apiCnx.createRequestLeave(FormDatarequest);
        }


        call.enqueue(new Callback<insertResponse>() {
            @Override
            public void onResponse(Call<insertResponse> call, Response<insertResponse> response) {
                progressDialog.dismiss();
                if(response.body().isUpdate()&&response.body().isInsert()){

                    FragmentTransaction ft4 = getActivity().getSupportFragmentManager().beginTransaction();
                    ft4.replace(R.id.frag1, new DemandesCongeFrag());
                    ft4.commit();

                }else{
                    alertError("LEAVE REQUEST","Something went wrong!please retry");
                }
            }

            @Override
            public void onFailure(Call<insertResponse> call, Throwable t) {
                progressDialog.dismiss();
                alertError("SERVER","Something went wrong!please retry!");
            }
        });
    }



    private void updateLabel(TextView date,boolean value) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(value){
            date.setText(sdf.format(System.currentTimeMillis()));
        }else{
            date.setText(sdf.format(myCalendar.getTime()));
        }

    }

    private String getToDate(int days){
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        myCalendar.add(Calendar.DAY_OF_MONTH,days);
        return sdf.format(myCalendar.getTime());
    }

    private void initView(View rootView){
        dropdown = (Spinner)rootView.findViewById(R.id.spinnerType);


        // buttons
        btnUploadFile = rootView.findViewById(R.id.btnUploadFile);
        btnValidateLeaveReq = rootView.findViewById(R.id.btn_ValidLeaveReq);

        // Text path file
        txtPathFile = rootView.findViewById(R.id.txtfichier);

        // Edit Texts variables
        fromDate = rootView.findViewById(R.id.fromEdit);
        daysEdit = rootView.findViewById(R.id.daysEdit);
        edtDescription = rootView.findViewById(R.id.description);
        //edtNbrDays = rootView.findViewById(R.id.nbrDays);

        // Layout
        layoutFile = rootView.findViewById(R.id.layout_file);
    }

    private void dropDownListener(AdapterView<?> parent,  int position){
        daysEdit.setVisibility(View.VISIBLE);
        layoutFile.setVisibility(View.INVISIBLE);
        String leaveType=parent.getItemAtPosition(position).toString();
        currentLeave=leaveType;
        if(parent.getItemAtPosition(position).toString().equals("Medical leave")) {
            layoutFile.setVisibility(View.VISIBLE);
        }
        if(leaveType.equals("Breavement leave")||leaveType.equals("Paternity leave")||leaveType.equals("Marriage leave")||
                leaveType.equals("Birth leave")) {
            daysEdit.setVisibility(View.INVISIBLE);
        }

    }

    private void checkAuth(String matricule){

        apiCnx=Constants.initRetrofit(BASE_URL_CHECK).create(IapiConnexion.class);
        Call<List<User>> call=apiCnx.checkRequestAuthorization(matricule);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                final String balanceLeave=response.body().get(0).getLeavebalance();


                if(getNumberofdays()<=Integer.parseInt(balanceLeave)){
                    createDemandeConge(matricule,getNumberofdays());
                }else{
                    progressDialog.dismiss();
                    alertError("ACCESS DENIED","You have already depassed your leave limit");

                    FragmentTransaction ft4 = getActivity().getSupportFragmentManager().beginTransaction();
                    ft4.replace(R.id.frag1, new DemandesCongeFrag());

                    ft4.commit();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                progressDialog.dismiss();
                alertError("LEAVE REQUEST","Something went wrong!please retry");
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
                }).show();
    }

private int getNumberofdays(){
    int numberOfDays=-1;
    switch (currentLeave){
        case "Breavement leave":
            numberOfDays=2;
            break;
        case "Paternity leave":
            numberOfDays=1;
            break;
        case "Marriage leave":
            numberOfDays=3;
            break;
        case "Birth leave":
            numberOfDays=3;
            break;
        default:numberOfDays=Integer.parseInt(daysEdit.getText().toString());

    }
    return numberOfDays;
}

private void requestWithupload(String path,DemandeConge dConge,String matricule,String id_type_leave){
        File image=new File(path);
        /*return new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("justification",image.getName(),RequestBody.create(MediaType.parse("image/*"),image))
            .addFormDataPart("from", dConge.getDateDebut())
            .addFormDataPart("to", dConge.getDatefin())
            .addFormDataPart("description", dConge.getRaison())
            .addFormDataPart("matricule", matricule)
            .addFormDataPart("id_type_leave", id_type_leave)
            .build();*/

    RequestBody requestFile =
            RequestBody.create(MediaType.parse("image/*"), image);

    try {
        if (requestFile.contentLength() != 0){
            fileImage = MultipartBody.Part.createFormData("justification", image.getName(), requestFile);
            uploadFormData=new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("from", dConge.getDateDebut())
                    .addFormDataPart("to", dConge.getDatefin())
                    .addFormDataPart("description", dConge.getRaison())
                    .addFormDataPart("matricule", matricule)
                    .addFormDataPart("id_type_leave", id_type_leave)
                    .build();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

}

    private RequestBody simpleRequest(DemandeConge dConge,String matricule,String id_type_leave){
        return new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("from", dConge.getDateDebut())
                .addFormDataPart("to", dConge.getDatefin())
                .addFormDataPart("description", dConge.getRaison())
                .addFormDataPart("matricule", matricule)
                .addFormDataPart("id_type_leave", id_type_leave)
                .build();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                openGallery();

            }

        }
    }
    private void displayProgressBar() {
        progressDialog=new ProgressDialog(getContext());
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
}



