package entities;

public class ChiTietHoaDonCaPhe {

    private int maCTHD;
    private int maHoaDon;
    private int maSanPham;
    private String tenSanPham;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    public ChiTietHoaDonCaPhe() {
    }

    // Constructor đầy đủ (khi lấy từ DB)
    public ChiTietHoaDonCaPhe(int maCTHD, int maHoaDon, int maSanPham, String tenSanPham,
                               int soLuong, double donGia, double thanhTien) {
        this.maCTHD = maCTHD;
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }

    // Constructor khi INSERT (KHÔNG truyền thanhTien)
    public ChiTietHoaDonCaPhe(int maHoaDon, int maSanPham, String tenSanPham, int soLuong, double donGia) {
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    // Getters & Setters
    public int getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(int maCTHD) {
        this.maCTHD = maCTHD;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    @Override
    public String toString() {
        return "ChiTietHoaDonCaPhe{" +
                "maCTHD=" + maCTHD +
                ", maHoaDon=" + maHoaDon +
                ", maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
