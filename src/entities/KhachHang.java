package entities;

import java.sql.Date;
import java.sql.Timestamp;

public class KhachHang {
    private long maKhachHang; 
    private String ho;
    private String ten;
    private String gioiTinh;
    private String cmnd;
    private Date ngaySinh;
    private DiaChi diaChi;
    private String soDienThoai;
    private String email;
    private int diemTichLuy;
    private Timestamp ngayDangKy;

    // Constructor mặc định
    public KhachHang() {
    }

    // Constructor với tất cả tham số, loại bỏ maKhachHang = 0
    public KhachHang(long maKhachHang, String ho, String ten, String gioiTinh,
                     String cmnd, Date ngaySinh, DiaChi diaChi, String soDienThoai,
                     String email, int diemTichLuy, Timestamp ngayDangKy) {
        this.maKhachHang = maKhachHang; // Gán mã khách hàng trực tiếp
        this.ho = ho;
        this.ten = ten;
        this.gioiTinh = gioiTinh;
        this.cmnd = cmnd;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diemTichLuy = diemTichLuy;
        this.ngayDangKy = ngayDangKy;
    }

   
    public KhachHang(String ten) {
        this.ten = ten;
        this.ho = "";
        this.gioiTinh = "Khác";
        this.cmnd = "";
        this.ngaySinh = Date.valueOf("2000-01-01");
        this.diaChi = new DiaChi(1, ten, ten, ten, ten, ten, ten); 
        this.soDienThoai = "";
        this.email = "";
        this.diemTichLuy = 0;
        this.ngayDangKy = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public long getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(long maKhachHang) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getDiemTichLuy() {
        return diemTichLuy;
    }

    public void setDiemTichLuy(int diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public Timestamp getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Timestamp ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    @Override
    public String toString() {
        return ho + " " + ten;
    }
}
