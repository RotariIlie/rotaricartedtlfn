package com.example.rotaricartedtlfn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView contactListView;
    private Button addContactButton;
    private DatabaseHelper db;
    private ContactAdapter contactAdapter;
    private List<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactListView = findViewById(R.id.contactListView);
        addContactButton = findViewById(R.id.addContactButton);

        db = new DatabaseHelper(this);
        loadContactList();

        // Eveniment pentru adăugarea unui nou contact
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactListActivity.this, AddEditContactActivity.class);
                startActivity(intent);
            }
        });

        // Eveniment pentru selectarea unui contact din listă pentru a-l edita
        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact selectedContact = contactList.get(position);
                Intent intent = new Intent(ContactListActivity.this, AddEditContactActivity.class);
                intent.putExtra("CONTACT_ID", selectedContact.getId());
                startActivity(intent);
            }
        });

        // Eveniment pentru ștergerea unui contact prin apăsare lungă
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contactToDelete = contactList.get(position);
                boolean deleted = db.deleteContact(contactToDelete.getId());
                if (deleted) {
                    Toast.makeText(ContactListActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                    loadContactList(); // Reîncarcă lista după ștergere
                } else {
                    Toast.makeText(ContactListActivity.this, "Error deleting contact", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    // Funcție pentru a încărca lista de contacte
    private void loadContactList() {
        contactList = db.getAllContacts();
        contactAdapter = new ContactAdapter(this, contactList);
        contactListView.setAdapter(contactAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reîncarcă lista de contacte atunci când utilizatorul revine la această activitate
        loadContactList();
    }
}