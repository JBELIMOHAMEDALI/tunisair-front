package com.example.tunisairapp.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.models.DemandeAutorisation;
import com.example.tunisairapp.models.IapiConnexion;
import com.example.tunisairapp.views.activities.DetailsDemandeActivity;
import com.example.tunisairapp.R;
import com.example.tunisairapp.adapters.ListDemandeCongeAdapter;
import com.example.tunisairapp.models.DemandeConge;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemandesCongeFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemandesCongeFrag extends Fragment  implements ListDemandeCongeAdapter.OnDemandeCongeListener{
    private static final String BASE_URL="http://"+ Constants.IP_ADRESS+"/api_app_androide/leave/";
    private IapiConnexion apiCnx;
    // list Demande conge adapter
    //private ListDemandeCongeAdapter pad;
    //private RecyclerView.LayoutManager LM;
    private List<DemandeConge> lstDemandConge = new ArrayList<DemandeConge>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DemandesCongeFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemandesCongeFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DemandesCongeFrag newInstance(String param1, String param2) {
        DemandesCongeFrag fragment = new DemandesCongeFrag();
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

        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);
        /*lstDemandConge.add(new DemandeConge("26-11-2020","30-11-2020", "description", "fichier", "Annual Leave", "Accepted"));
        lstDemandConge.add(new DemandeConge("21-12-2021","27-12-2021", "description", "fichier", "Medical Leave", "In progress"));
        lstDemandConge.add(new DemandeConge("23-05-2020","29-05-2020", "description", "fichier", "Medical Leave", "Rejected"));
        lstDemandConge.add(new DemandeConge("13-12-2005","27-12-2005", "description", "fichier", "Annual Leave", "In progress"));
        lstDemandConge.add(new DemandeConge("12-11-2021","13-11-2021", "description", "fichier", "Annual Leave", "In progress"));
        lstDemandConge.add(new DemandeConge("12-11-2012","15-11-2012", "description", "fichier", "Medical Leave", "Accepted"));
        lstDemandConge.add(new DemandeConge("21-11-2012","29-11-2012", "description", "fichier", "Medical Leave", "Accepted"));
        lstDemandConge.add(new DemandeConge("11-12-2020","25-12-2020", "description", "fichier", "Annual Leave", "In progress"));
        lstDemandConge.add(new DemandeConge("14-15-2023","15-11-2023", "description", "fichier", "Annual Leave", "Rejected"));
        lstDemandConge.add(new DemandeConge("11-01-2009","19-01-2009", "description", "fichier", "Medical Leave", "In progress"));
        lstDemandConge.add(new DemandeConge("14-12-2020","20-12-2020", "description", "fichier", "Medical Leave", "Rejected"));
        lstDemandConge.add(new DemandeConge("17-03-2025","23-03-2025", "description", "fichier", "Medical Leave", "Accepted"));
*/
        View view = inflater.inflate(R.layout.fragment_demandes_conge, container, false);
        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_demande_conge);
        pad=new ListDemandeCongeAdapter(lstDemandConge, getContext(),this);
        recyclerView.setAdapter(pad);
        LM=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LM);*/
        loadDemandeConge(view);
        return view;
    }



    @Override
    public void onNoteClick(int position) {
        Intent i = new Intent(getContext(), DetailsDemandeActivity.class);
        startActivity(i);
    }

    public void loadDemandeConge(View view){
        SharedPreferences preferences= getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String matricule = preferences.getString(Constants.USER_MATRICULE, null);
        Call<List<DemandeConge>> call=apiCnx.getRequestLeavesByMatriculeQuery(matricule);//parameter is here
        call.enqueue(new Callback<List<DemandeConge>>() {

            @Override
            public void onResponse(Call<List<DemandeConge>> call, Response<List<DemandeConge>> response) {
                if(response.body().size()>0){
                    for (int i = 0; i <response.body().size() ; i++) {
                        final DemandeConge dc=response.body().get(i);
                        lstDemandConge.add(dc);
                    }
                    handleAdapter(view,lstDemandConge);
                }
            }

            @Override
            public void onFailure(Call<List<DemandeConge>> call, Throwable t) {
                handleAdapter(view,lstDemandConge);
            }


        });
    }



    public void handleAdapter(View view,List<DemandeConge> lstDemandConge){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_demande_conge);
        ListDemandeCongeAdapter pad=new ListDemandeCongeAdapter(lstDemandConge, getContext(),this);
        recyclerView.setAdapter(pad);
        RecyclerView.LayoutManager LM=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LM);
    }
}