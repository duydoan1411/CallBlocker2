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

public class RVContactApdater extends RecyclerView.Adapter<RVContactApdater.ContactViewHolder> {
    private List<Contact> contacts;

    RVContactApdater(List<Contact> contacts){
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_card_view, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(v);

        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (!contacts.get(position).getName().equals("")) {
            holder.tvName.setText(contacts.get(position).getName());
        }else {
            holder.tvName.setText("No Name");
        }
        holder.tvNumber.setText(contacts.get(position).getNumber());
        holder.deleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDAO contactDAO;
                ContactDatabase contactDatabase = ContactDatabase.getInstance(v.getContext());
                contactDatabase.contactDAO().deleteContact(contacts.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView ivContactImg, deleteImg;
        TextView tvName, tvNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_contact);
            ivContactImg = itemView.findViewById(R.id.iv_contact_img);
            tvName = itemView.findViewById(R.id.tv_contact_name);
            tvNumber = itemView.findViewById(R.id.tv_Contact_number);
            deleteImg = itemView.findViewById(R.id.delete_img);
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
}
