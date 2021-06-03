package com.example.tunisairapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tunisairapp.Constants;
import com.example.tunisairapp.R;
import com.example.tunisairapp.models.DemandeAutorisation;
import com.example.tunisairapp.models.DemandeConge;
import com.example.tunisairapp.views.activities.DetailsAuthorization;
import com.example.tunisairapp.views.activities.DetailsDemandeActivity;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tunisairapp.R.color.black;
import static com.example.tunisairapp.R.color.myblue;
import static com.example.tunisairapp.R.color.mydarkGreen;
import static com.example.tunisairapp.R.color.mydarkOrange;

public class ListDemandeAutorisationAdapter extends RecyclerView.Adapter<ListDemandeAutorisationAdapter.DemandeAutorisViewHolder> {

    List<DemandeAutorisation> listeDemandeAutorisation;
    Context context;
    private OnDemandeAutorisationListener mOnDemandeAutorisationListener;

    public ListDemandeAutorisationAdapter(List<DemandeAutorisation> listeCamp, Context context, OnDemandeAutorisationListener mOndemandeAutoListener ) {
        this.listeDemandeAutorisation = listeCamp;
        this.context = context;
        this.mOnDemandeAutorisationListener = mOndemandeAutoListener;
    }

    @NonNull
    @Override
    public DemandeAutorisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vue= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_demande_authorisation_item,parent,false);
        DemandeAutorisViewHolder PVH=new DemandeAutorisViewHolder(vue, mOnDemandeAutorisationListener);
        return PVH;
    }

    @Override
    public void onBindViewHolder(@NonNull DemandeAutorisViewHolder holder, int position) {

        DemandeAutorisation demandeAutorisation = listeDemandeAutorisation.get(position);

        holder.dateAutoReq.setText(demandeAutorisation.getDateAutoReq());
        holder.heureDebut.setText(demandeAutorisation.getHeureDebut());
        holder.heureFin.setText(demandeAutorisation.getHeureFin());
        holder.etatAutoReq.setText(demandeAutorisation.getEtatAutoReq());

        switch (holder.etatAutoReq.getText().toString()){
            case "accepted" :
                holder.etatAutoReq.setTextColor(ContextCompat.getColor(context, mydarkGreen));
                break;
            case "ongoing" :
                holder.etatAutoReq.setTextColor(ContextCompat.getColor(context, mydarkOrange));
                break;
            case "rejected" :
                holder.etatAutoReq.setTextColor(ContextCompat.getColor(context, myblue));
                break;
            default:
                holder.etatAutoReq.setTextColor(ContextCompat.getColor(context, black));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToDetails(context,demandeAutorisation,holder);

            }
        });

    }
    private void redirectToDetails(Context context, DemandeAutorisation dAuth, ListDemandeAutorisationAdapter.DemandeAutorisViewHolder holder) {
        SharedPreferences preferences= context.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        String matricule = preferences.getString(Constants.USER_MATRICULE, null);
        Intent intent=new Intent(context, DetailsAuthorization.class);
        Bundle b=new Bundle();
        b.putString("from",dAuth.getHeureDebut());
        b.putString("to",dAuth.getHeureFin());
        b.putString("date_request",dAuth.getDateAutoReq());
        b.putString("status",dAuth.getEtatAutoReq());
        b.putString("matricule",matricule);
        intent.putExtras(b);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return listeDemandeAutorisation.size();
    }

    public static class DemandeAutorisViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView heureDebut, heureFin, dateAutoReq, etatAutoReq;
        OnDemandeAutorisationListener onDemandeAutorisationListener;

        public DemandeAutorisViewHolder(@NonNull View itemView, OnDemandeAutorisationListener onDemandAutoListener) {
            super(itemView);

            heureDebut = itemView.findViewById(R.id.heure_debut);
            heureFin = itemView.findViewById(R.id.heure_fin);
            dateAutoReq = itemView.findViewById(R.id.dateAutoReq);
            etatAutoReq = itemView.findViewById(R.id.etatAutoReq);

            this.onDemandeAutorisationListener = onDemandAutoListener;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            onDemandeAutorisationListener.onNoteClick(getAdapterPosition());
        }
    }
    public interface OnDemandeAutorisationListener
    {
        void onNoteClick(int position);
    }
}
