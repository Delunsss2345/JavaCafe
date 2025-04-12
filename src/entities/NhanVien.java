package entities;

import java.sql.Date;

public class NhanVien {
    private int maNhanVien;
    private String ten;
    private String ho;
    private Date ngaySinh;
    private String gioiTinh;
    private String cmnd;
    private String soDienThoai;
    private DiaChi diaChi;
    private TaiKhoan taiKhoan;
    private String loaiNhanVien;
    private String trangThai;
    private String caLamViec;

    public NhanVien() {
    }

    public NhanVien(int maNhanVien, String ten, String ho, Date ngaySinh, 
                   String gioiTinh, String cmnd, String soDienThoai, 
                   DiaChi diaChi, TaiKhoan taiKhoan, String loaiNhanVien, 
                   String trangThai, String caLamViec) {
        this.maNhanVien = maNhanVien;
        this.ten = ten;
        this.ho = ho;
        this.ngaySinh = ngaySinh;
        this.gioiTinh = gioiTinh;
        this.cmnd = cmnd;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.taiKhoan = taiKhoan;
        this.loaiNhanVien = loaiNhanVien;
        this.trangThai = trangThai;
        this.caLamViec = caLamViec;
    }

    // Getters and Setters
    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getCmnd() {
        return cmnd;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public DiaChi getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DiaChi diaChi) {
        this.diaChi = diaChi;
    }

    public TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getLoaiNhanVien() {
        return loaiNhanVien;
    }

    public void setLoaiNhanVien(String loaiNhanVien) {
        this.loaiNhanVien = loaiNhanVien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getCaLamViec() {
        return caLamViec;
    }

    public void setCaLamViec(String caLamViec) {
        this.caLamViec = caLamViec;
    }

    @Override
    public String toString() {
        return ho + " " + ten;
    }
}