package com.DD141.callblocker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVContactCallLogApdater extends RecyclerView.Adapter<RVContactCallLogApdater.ContactViewHolder> {
    private List<Contact> contacts;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    RVContactCallLogApdater(List<Contact> contacts, RecyclerViewItemClickListener listener){
        this.contacts = contacts;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_log_item, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(v);

        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.tvName.setText(contacts.get(position).getName());
        holder.tvNumber.setText(contacts.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CardView cardView;
        ImageView ivContactImg;
        TextView tvName, tvNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_contact);
            ivContactImg = itemView.findViewById(R.id.iv_contact_img);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvNumber = itemView.findViewById(R.id.tv_Contact_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(contacts.get(getAdapterPosition()));
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setData(List<Contact> newdata){
        this.contacts = newdata;
        notifyDataSetChanged();
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(Contact data);
    }
}
