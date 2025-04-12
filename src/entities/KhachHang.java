package entities;

import java.sql.Date;

public class KhachHang {
    private int maKhachHang;
    private String ho;
    private String ten;
    private String gioiTinh;
    private String cmnd;
    private Date ngaySinh;
    private DiaChi diaChi;
    private String soDienThoai;

    public KhachHang() {
    }

    public KhachHang(int maKhachHang, String ho, String ten, String gioiTinh, 
                    String cmnd, Date ngaySinh, DiaChi diaChi, String soDienThoai) {
        this.maKhachHang = maKhachHang;
        this.ho = ho;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.cmnd = cmnd;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
    }

    // Getters and Setters
    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
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

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public DiaChi getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DiaChi diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    @Override
    public String toString() {
        return ho + " " + ten;
    }
}