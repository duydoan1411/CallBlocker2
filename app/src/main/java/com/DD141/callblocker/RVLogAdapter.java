package com.DD141.callblocker;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DD141.callblocker.LogDatabase.Log;
import com.DD141.callblocker.LogDatabase.LogViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RVLogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int HEAD_TYPE = 0;
    private final static int ITEM_TYPE = 1;

    private ArrayList<Log> listType;

    RVLogAdapter(List<Log> logs){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        listType = new ArrayList<Log>();
        if (logs.size() > 0) {
            listType.add(new Log(simpleDateFormat.format(logs.get(0).getDate())));
            for (int i = 1; i < logs.size(); i++) {
                String date1 = simpleDateFormat.format(logs.get(i).getDate());
                String date2 = simpleDateFormat.format(logs.get(i-1).getDate());
                if (date1.equals(date2)){
                    listType.add(logs.get(i));
                }else {
                    listType.add(new Log(date1));
                    listType.add(logs.get(i));
                }
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_view_log, parent, false);
             return new RVLogAdapter.LogViewHolder(v);
        }else if (viewType == HEAD_TYPE){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_log, parent, false);
            return new RVLogAdapter.HeaderViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LogViewHolder) {
            LogViewHolder item = (LogViewHolder)holder;
            if (!logs.get(position).getName().equals("")) {
                item.tvName.setText(logs.get(position).getName());
            } else {
                item.tvName.setText("No Name");
            }
            item.tvNumber.setText(logs.get(position).getNumber());
            item.deleteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.viewModelLog.delete(logs.get(position));
                }
            });
        } else if (holder instanceof HeaderViewHolder){
            if ()
        }
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView ivLogImg, deleteImg;
        TextView tvName, tvNumber, time;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_contact);
            ivLogImg = itemView.findViewById(R.id.iv_contact_img);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvNumber = itemView.findViewById(R.id.tv_Contact_number);
            time = itemView.findViewById(R.id.tv_log_time);
            deleteImg = itemView.findViewById(R.id.delete_img);
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView header;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tv_header);
        }
    }
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<Log> newdata){
        this.logs = newdata;
        notifyDataSetChanged();
    }
}
