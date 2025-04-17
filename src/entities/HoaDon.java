package entities;

import java.time.LocalDateTime;

public class HoaDon {
    private int maHoaDon; 
    private LocalDateTime ngayLap; 
    private double tongTien; 
    private double tienKhachTra;
    private double tienThua; 
    private int maNhanVien; 
    private int maKhachHang; 

  
    public HoaDon() {
    }

   
    public HoaDon(int maHoaDon, LocalDateTime ngayLap, double tongTien, double tienKhachTra, double tienThua,
                  int maNhanVien, int maKhachHang) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.tienKhachTra = tienKhachTra;
        this.tienThua = tienThua;
        this.maNhanVien = maNhanVien;
        this.maKhachHang = maKhachHang;
    }

    // Getters & Setters
    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienKhachTra() {
        return tienKhachTra;
    }

    public void setTienKhachTra(double tienKhachTra) {
        this.tienKhachTra = tienKhachTra;
    }

    public double getTienThua() {
        return tienThua;
    }

    public void setTienThua(double tienThua) {
        this.tienThua = tienThua;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon=" + maHoaDon +
                ", ngayLap=" + ngayLap +
                ", tongTien=" + tongTien +
                ", tienKhachTra=" + tienKhachTra +
                ", tienThua=" + tienThua +
                ", maNhanVien=" + maNhanVien +
                ", maKhachHang=" + maKhachHang +
                '}';
    }
}
