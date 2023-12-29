package fr.univpau.fueltoday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<String> servicesList;

    public ServicesAdapter(List<String> servicesList) {
        this.servicesList = servicesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String service = servicesList.get(position);
        holder.textViewService.setText(service);
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewService;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewService = itemView.findViewById(R.id.textViewService);
        }
    }
}
