package com.example.inventoryobat;

public enum JenisObat {
    KAPSUL("Kapsul"),
    TABLET("Tablet"),
    SIRUP("Sirup"),
    VITAMIN("Vitamin"),
    OBAT_TETES("Obat Tetes"),
    SALEP("Salep"),
    SUPPOSITORIA("Suppositoria"),
    OBAT_SUNTIK("Obat Suntik");

    private String displayName;

    JenisObat(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
}

}
