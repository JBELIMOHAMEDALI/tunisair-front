package com.example.tunisairapp.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;

import java.util.ArrayList;

public class DetailsDemandeActivity extends AppCompatActivity {
    ImageView img_medical;
    TextView tv_categorie,tv_from,tv_to,tv_matricule,tv_date_request,tv_status;
    private final static String BASE_URL="http://"+ Constants.IP_ADRESS+"/api_app_androide/upload/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_demande);
        img_medical=findViewById(R.id.tv_image);
        tv_categorie=findViewById(R.id.tv_categorie);
        tv_from=findViewById(R.id.tv_from);
        tv_to=findViewById(R.id.tv_to);
        tv_matricule=findViewById(R.id.tv_matricule);
        tv_date_request=findViewById(R.id.tv_date_request);
        tv_status=findViewById(R.id.tv_status);
        Bundle bundle = getIntent().getExtras();
        getData(bundle);
    }

    public void getData(Bundle bundle){
        String categorie = bundle.getString("categorie");
        String image = bundle.getString("image");
        if(bundle!=null){
            if(categorie.equals("Medical leave")){
                Glide
                        .with(DetailsDemandeActivity.this)
                        .load(BASE_URL+image)
                        .centerCrop()
                        .placeholder(R.drawable.progress_bar)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(img_medical);
            }




            String from = bundle.getString("from");
            String to = bundle.getString("to");
            String date_request = bundle.getString("date_request");
            String status = bundle.getString("status");
            String matricule = bundle.getString("matricule");
            tv_categorie.setText(categorie);
            tv_from.setText(from);
            tv_to.setText(to);
            tv_matricule.setText(matricule);
            tv_date_request.setText(date_request);
            tv_status.setText(status);



        }


    }
}