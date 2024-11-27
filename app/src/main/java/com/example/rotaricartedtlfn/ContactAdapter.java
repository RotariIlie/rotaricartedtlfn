package com.example.rotaricartedtlfn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactAdapter extends BaseAdapter {

    private Context context;
    private List<Contact> contactList;
    private LayoutInflater inflater;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return contactList.size();
    }

    @Override
    public Object getItem(int position) {
        return contactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_list_item, parent, false);
        }

        Contact contact = contactList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.contactNameTextView);
        TextView phoneTextView = convertView.findViewById(R.id.contactPhoneTextView);
        ImageView contactImageView = convertView.findViewById(R.id.contactImageView);

        nameTextView.setText(contact.getName());
        phoneTextView.setText(contact.getPhone());

        // Afișare imagine din BLOB (dacă există)
        if (contact.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(contact.getImage(), 0, contact.getImage().length);
            contactImageView.setImageBitmap(bitmap);
        } else {
            contactImageView.setImageResource(R.drawable.ic_default_contact); // Imagine implicită dacă nu există imagine
        }

        return convertView;
    }
}