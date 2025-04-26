package com.kasir.model;

public class Barang {
    private int id;
    private String nama;
    private double harga;

    // Constructor default (no-arg) -- dibutuhkan oleh JDBC kadang-kadang
    public Barang() {
    }

    // Constructor dengan parameter
    public Barang(int id, String nama, double harga) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
    }

    // Getter
    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public double getHarga() {
        return harga;
    }

    // Setter (opsional, kalau mau data mutable)
    public void setId(int id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }
}
