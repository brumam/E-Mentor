package com.example.e_mentor.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_mentor.Helpers.Module;
import com.example.e_mentor.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ModuleHolder> {

    List<Module> moduleList;
    List<Module> filteredModuleList;
    ModuleHolder.ModuleInterface listener;

    public ModuleAdapter(List<Module> moduleList, ModuleHolder.ModuleInterface _listener) {
        this.moduleList = moduleList;
        listener = _listener;
        filteredModuleList = new ArrayList<>(moduleList);
    }


    // Inflates the layout for each module item and creates a ModuleHolder
    @NonNull
    @Override
    public ModuleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_module_adapter, parent, false);
        return new ModuleHolder(v, listener);
    }
    // Sets the text and image for each module item using the filteredModuleList
    @Override
    public void onBindViewHolder(@NonNull ModuleHolder holder, int position) {
        holder.tv.setText(filteredModuleList.get(position).getName());
        Picasso.get().load(filteredModuleList.get(position).getImageURL()).fit().into(holder.iv);
    }

    // Returns the total number of module items in the filteredModuleList
    @Override
    public int getItemCount() {
        return filteredModuleList.size();
    }
    // Updates the filteredModuleList with new data and refreshes the adapter

    public void setData(List<Module> filteredModuleList) {
        this.filteredModuleList.clear();
        this.filteredModuleList.addAll(filteredModuleList);
        notifyDataSetChanged();
    }

    // ViewHolder class for the module items in the RecyclerView
    public static class ModuleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv;
        TextView tv;
        ModuleInterface listener;

        // Initializes views and listener for each module item
        public ModuleHolder(@NonNull View itemView, ModuleInterface _listener) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_card_image);
            tv = itemView.findViewById(R.id.tv_card_name);
            listener = _listener;
            itemView.setOnClickListener(this);
        }
        // Triggers the onModuleClick method when a module item is clicked
        @Override
        public void onClick(View v) {
            listener.onModuleClick(getAdapterPosition());
        }

        // Interface for handling clicks on module items
        public interface ModuleInterface {
            void onModuleClick(int i);
        }
    }
}
