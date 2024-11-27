package com.example.rotaricartedtlfn;

public class Contact {
    private int id;
    private String name;
    private String phone;
    private String email;
    private byte[] image;

    public Contact(int id, String name, String phone, String email, byte[] image) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }

    // Getter și Setter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public byte[] getImage() { return image; }
}