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
import com.example.tunisairapp.models.DemandeConge;
import com.example.tunisairapp.models.IapiConnexion;
import com.example.tunisairapp.views.activities.DetailsDemandeActivity;
import com.example.tunisairapp.R;
import com.example.tunisairapp.adapters.ListDemandeAutorisationAdapter;
import com.example.tunisairapp.models.DemandeAutorisation;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DemandeAutorisationFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DemandeAutorisationFrag extends Fragment implements ListDemandeAutorisationAdapter.OnDemandeAutorisationListener {
    private IapiConnexion apiCnx;
    private static final String BASE_URL="http://"+ Constants.IP_ADRESS +"/api_app_androide/authorization/";
    // list Demande autorisation adapter

    //private ListDemandeAutorisationAdapter pad;
    //private RecyclerView.LayoutManager LM;
    private List<DemandeAutorisation> lstDemandAutorisation = new ArrayList<DemandeAutorisation>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DemandeAutorisationFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DemandeAutorisationFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static DemandeAutorisationFrag newInstance(String param1, String param2) {
        DemandeAutorisationFrag fragment = new DemandeAutorisationFrag();
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
        // list Demandes

        apiCnx=Constants.initRetrofit(BASE_URL).create(IapiConnexion.class);
        /*lstDemandAutorisation.add(new DemandeAutorisation("12:00","18:00", "description", "Accepted","25-01-2020"));
        lstDemandAutorisation.add(new DemandeAutorisation("07:00","13:00", "description", "In progress","30-03-2021"));
        lstDemandAutorisation.add(new DemandeAutorisation("08:30","10:30", "description", "Accepted","05-11-2021"));
        lstDemandAutorisation.add(new DemandeAutorisation("09:25","18:30", "description", "Rejected","18-07-2020"));
        lstDemandAutorisation.add(new DemandeAutorisation("12:15","17:30", "description", "Accepted","07-06-2021"));
        lstDemandAutorisation.add(new DemandeAutorisation("14:45","14:40", "description", "In progress","20-06-2021"));
        lstDemandAutorisation.add(new DemandeAutorisation("08:00","18:00", "description", "Accepted","19-10-2020"));
        lstDemandAutorisation.add(new DemandeAutorisation("11:00","16:00", "description", "Rejected","01-12-2020"));
*/

        View view = inflater.inflate(R.layout.fragment_demande_autorisation, container, false);
        /*RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_demande_autoris);
        pad=new ListDemandeAutorisationAdapter(lstDemandAutorisation, getContext(),this);
        recyclerView.setAdapter(pad);
        LM=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LM);*/
        SharedPreferences preferences= getContext().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String matricule = preferences.getString(Constants.USER_MATRICULE, null);
        loadDemandeAuthorizations(view,matricule);
        return view;
    }



    @Override
    public void onNoteClick(int position) {
        Intent i = new Intent(getContext(), DetailsDemandeActivity.class);
        startActivity(i);
    }




    public void loadDemandeAuthorizations(View view,String matricule){

        Call<List<DemandeAutorisation>> call=apiCnx.getRequestAuthorizationByMatriculQuery(matricule);

        call.enqueue(new Callback<List<DemandeAutorisation>>() {

            @Override
            public void onResponse(Call<List<DemandeAutorisation>> call, Response<List<DemandeAutorisation>> response) {
                if(response.body().size()>0){
                    for (int i = 0; i <response.body().size() ; i++) {
                        final DemandeAutorisation demandeAuth=response.body().get(i);
                        lstDemandAutorisation.add(demandeAuth);
                        Log.d("DC",demandeAuth.getHeureFin());
                    }
                    handleAdapter(view,lstDemandAutorisation);
                }
            }

            @Override
            public void onFailure(Call<List<DemandeAutorisation>> call, Throwable t) {
                handleAdapter(view,lstDemandAutorisation);
            }


        });
    }
    public void handleAdapter(View view,List<DemandeAutorisation> demandesAuthList){
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_demande_autoris);
        ListDemandeAutorisationAdapter pad=new ListDemandeAutorisationAdapter(demandesAuthList, getContext(),this);
        recyclerView.setAdapter(pad);
        RecyclerView.LayoutManager LM=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(LM);
    }
}