package com.kasir.cetakstruk;

import java.util.List;
import com.kasir.model.Barang;

public interface CetakStruk {
    void cetak(List<Barang> barangList, List<Integer> jumlahList, double total);
}
