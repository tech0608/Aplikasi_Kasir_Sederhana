package com.mycompany.kasir;

import com.kasir.model.Barang;
import com.kasir.transaksi.Transaksi;
import com.kasir.database.DatabaseManager;
import com.kasir.cetakstruk.CetakStruk;
import com.kasir.cetakstruk.CetakStrukImpl;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Transaksi transaksi = new Transaksi();
        CetakStruk cetakStruk = new CetakStrukImpl();

        System.out.println("====== KASIR SEDERHANA ======");

        boolean running = true;
        while (running) {
            System.out.println("\nMenu:");
            System.out.println("1. Buat Transaksi");
            System.out.println("2. Lihat History Transaksi");
            System.out.println("3. Keluar");

            System.out.print("Pilih menu: ");
            int pilih = sc.nextInt();
            sc.nextLine(); // buang enter

            switch (pilih) {
                case 1:
                    mulaiTransaksi(sc, transaksi, cetakStruk);
                    break;
                case 2:
                    transaksi.lihatHistory();
                    break;
                case 3:
                    running = false;
                    System.out.println("Terima kasih telah menggunakan kasir!");
                    DatabaseManager.closeConnection();
                    break;
                default:
                    System.out.println("Menu tidak valid.");
            }
        }

        sc.close();
    }

    private static void mulaiTransaksi(Scanner sc, Transaksi transaksi, CetakStruk cetakStruk) {
        List<Barang> daftarBarang = transaksi.ambilDaftarBarang();
        transaksi.resetTransaksi(); // supaya transaksi lama tidak nyampur

        boolean lanjut = true;
        while (lanjut) {
            System.out.println("\n=============== Daftar Barang ===============");
            System.out.printf("%-5s %-20s %-10s\n", "ID", "Nama", "Harga");
            System.out.println("---------------------------------------------");

            for (Barang barang : daftarBarang) {
                System.out.printf("%-5d %-20s Rp %.2f\n", 
                                  barang.getId(), 
                                  barang.getNama(), 
                                  barang.getHarga());
            }
            System.out.println("---------------------------------------------");


            System.out.print("\nPilih ID barang: ");
            int id = sc.nextInt();
            System.out.print("Jumlah: ");
            int jumlah = sc.nextInt();

            Barang barangDipilih = daftarBarang.stream()
                    .filter(b -> b.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (barangDipilih != null) {
                transaksi.tambahBarang(barangDipilih, jumlah);
            } else {
                System.out.println("Barang tidak ditemukan!");
            }

            System.out.print("\nTambah lagi? (y/n): ");
            String lagi = sc.next();
            lanjut = lagi.equalsIgnoreCase("y");
        }

        double total = transaksi.hitungTotal();
        cetakStruk.cetak(transaksi.getBarangList(), transaksi.getJumlahList(), total);

        transaksi.simpanTransaksi();
    }
}
