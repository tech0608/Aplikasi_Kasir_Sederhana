package com.kasir.cetakstruk;

import com.kasir.model.Barang;
import java.util.List;

public class CetakStrukImpl implements CetakStruk {

    @Override
    public void cetak(List<Barang> barangList, List<Integer> jumlahList, double total) {
        System.out.println("\n===== STRUK PEMBELIAN =====");
        for (int i = 0; i < barangList.size(); i++) {
            Barang barang = barangList.get(i);
            int qty = jumlahList.get(i);
            double subtotal = barang.getHarga() * qty;
            System.out.printf("%s x%d = Rp %.2f\n", barang.getNama(), qty, subtotal);
        }
        System.out.println("---------------------------");
        System.out.printf("TOTAL: Rp %.2f\n", total);
        System.out.println("===========================\n");
    }
}
