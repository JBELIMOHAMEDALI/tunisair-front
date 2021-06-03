package com.example.tunisairapp.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.tunisairapp.R;

public class DetailsAuthorization extends AppCompatActivity {
    TextView tv_from,tv_to,tv_matricule,tv_date_request,tv_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_authorization);
        tv_from=findViewById(R.id.tv_from);
        tv_to=findViewById(R.id.tv_to);
        tv_matricule=findViewById(R.id.tv_matricule);
        tv_date_request=findViewById(R.id.tv_date_request);
        tv_status=findViewById(R.id.tv_status);
        Bundle bundle = getIntent().getExtras();
        getData(bundle);
    }
    public void getData(Bundle bundle){
        if(bundle!=null){
            String from = bundle.getString("from");
            String to = bundle.getString("to");
            String date_request = bundle.getString("date_request");
            String status = bundle.getString("status");
            String matricule = bundle.getString("matricule");
            tv_from.setText(from);
            tv_to.setText(to);
            tv_matricule.setText(matricule);
            tv_date_request.setText(date_request);
            tv_status.setText(status);


        }


    }
}