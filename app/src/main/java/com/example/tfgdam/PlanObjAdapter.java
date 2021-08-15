package com.example.tfgdam;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlanObjAdapter extends RecyclerView.Adapter<PlanObjAdapter.PlanViewHolder> {

    Context context;
    List<PlanObj> listaPlanes;

    public PlanObjAdapter(Context context, List<PlanObj> listaPlanes) {
        this.context = context;
        this.listaPlanes = listaPlanes;
    }

    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_layout, null);
        return new PlanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {
        PlanObj plan = listaPlanes.get(position);
        holder.title1.setText(plan.getLugar1());
        holder.direccion1.setText(plan.getDireccion1());
        holder.title2.setText(plan.getLugar2());
        holder.direccion2.setText(plan.getDireccion2());
        holder.title3.setText(plan.getLugar3());
        holder.direccion3.setText(plan.getDireccion3());
    }

    @Override
    public int getItemCount() {
        return listaPlanes.size();
    }

    class PlanViewHolder extends  RecyclerView.ViewHolder{

        ImageView image;
        TextView title1, direccion1, title2, direccion2, title3, direccion3;

        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title1 = itemView.findViewById(R.id.title1);
            direccion1 = itemView.findViewById(R.id.direccion1);
            title2 = itemView.findViewById(R.id.title2);
            direccion2 = itemView.findViewById(R.id.direccion2);
            title3 = itemView.findViewById(R.id.title3);
            direccion3 = itemView.findViewById(R.id.direccion3);

        }
    }
}



