package entities;

import java.sql.Date;

public class DonHang {
    private int maDonHang;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private Date ngayTao;
    private float tongTien;
    private String trangThai;

    public DonHang() {
    }

    public DonHang(int maDonHang, KhachHang khachHang, NhanVien nhanVien, 
                  Date ngayTao, float tongTien, String trangThai) {
        this.maDonHang = maDonHang;
        this.khachHang = khachHang;
        this.nhanVien = nhanVien;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public int getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(int maDonHang) {
        this.maDonHang = maDonHang;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "ĐH" + maDonHang + " - " + ngayTao + " - " + tongTien + "đ";
    }
}