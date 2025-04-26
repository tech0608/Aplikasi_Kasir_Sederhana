package com.kasir.transaksi;

import com.kasir.model.Barang;
import com.kasir.database.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private List<Barang> barangList = new ArrayList<>();
    private List<Integer> jumlahList = new ArrayList<>();

    public List<Barang> ambilDaftarBarang() {
        List<Barang> daftar = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM barang");

            while (rs.next()) {
                Barang barang = new Barang(
                    rs.getInt("id"),
                    rs.getString("nama_barang"),
                    rs.getDouble("harga_barang")
                );
                daftar.add(barang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftar;
    }

    public void tambahBarang(Barang barang, int jumlah) {
        barangList.add(barang);
        jumlahList.add(jumlah);
    }

    private void simpanTransaksiKeDatabase() {
        double total = hitungTotal();  // hitung total dari transaksi yang ada

        try (Connection conn = DatabaseManager.getConnection()) {
            conn.setAutoCommit(false); // mulai transaksi manual

            // Insert ke tabel transaksi (ambil id_transaksi)
            String insertTransaksiSql = "INSERT INTO transaksi (total) VALUES (?)";
            PreparedStatement insertTransaksiStmt = conn.prepareStatement(insertTransaksiSql, Statement.RETURN_GENERATED_KEYS);
            insertTransaksiStmt.setDouble(1, total);
            insertTransaksiStmt.executeUpdate();

            ResultSet rs = insertTransaksiStmt.getGeneratedKeys();
            int idTransaksi = 0;
            if (rs.next()) {
                idTransaksi = rs.getInt(1);
            }

            // Insert ke tabel transaksi_detail
            String insertDetailSql = "INSERT INTO transaksi_detail (id_transaksi, id_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement insertDetailStmt = conn.prepareStatement(insertDetailSql);

            for (int i = 0; i < barangList.size(); i++) {
                Barang barang = barangList.get(i);
                int jumlah = jumlahList.get(i);
                double subtotal = barang.getHarga() * jumlah;

                insertDetailStmt.setInt(1, idTransaksi);
                insertDetailStmt.setInt(2, barang.getId());
                insertDetailStmt.setInt(3, jumlah);
                insertDetailStmt.setDouble(4, subtotal);

                insertDetailStmt.addBatch();
            }

            insertDetailStmt.executeBatch();

            conn.commit(); // commit transaksi

            System.out.println("Transaksi berhasil disimpan ke database!");

            // Reset data setelah transaksi selesai
            resetTransaksi();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metode public yang bisa dipanggil dari luar (di kelas Main)
    public void simpanTransaksi() {
        simpanTransaksiKeDatabase(); // memanggil metode private dari dalam
    }

    public List<Barang> getBarangList() {
        return barangList;
    }

    public List<Integer> getJumlahList() {
        return jumlahList;
    }

    public double hitungTotal() {
        double total = 0;
        for (int i = 0; i < barangList.size(); i++) {
            total += barangList.get(i).getHarga() * jumlahList.get(i);
        }
        return total;
    }

    public void resetTransaksi() {
        barangList.clear();
        jumlahList.clear();
    }

    public void lihatHistory() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            String sql = "SELECT * FROM transaksi";
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n===== HISTORY TRANSAKSI =====");
            while (rs.next()) {
                int id = rs.getInt("id");
                double total = rs.getDouble("total");
                Timestamp waktu = rs.getTimestamp("waktu");
                System.out.printf("ID: %d | Total: Rp %.2f | Waktu: %s\n", id, total, waktu);
            }

        } catch (SQLException e) {
            System.out.println("Gagal mengambil history: " + e.getMessage());
        }
    }
}
