package entities;
//Người làm Phạm Thanh Huy
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SanPham {
    private int maSanPham;
    private String tenSanPham;
    private int maLoai;
    private BigDecimal gia; 
    private String moTa;
    private String trangThai;
    private String hinhAnh; 
    private LocalDateTime ngayTao; 
    private LocalDateTime ngayCapNhat; 

    public SanPham() {
    }
    public SanPham(int maSanPham, String tenSanPham, int maLoai, BigDecimal gia, String moTa, String trangThai, String hinhAnh, LocalDateTime ngayCapNhat) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.maLoai = maLoai;
        this.gia = gia;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.hinhAnh = hinhAnh;
        this.ngayCapNhat = ngayCapNhat;
    }
    public SanPham(int maSanPham, String tenSanPham, int maLoai,
                  BigDecimal gia, String moTa, String trangThai,
                  String hinhAnh, LocalDateTime ngayTao, LocalDateTime ngayCapNhat) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.maLoai = maLoai;
        this.gia = gia;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.hinhAnh = hinhAnh;
        this.ngayTao = ngayTao;
        this.ngayCapNhat = ngayCapNhat;
    }

    // Getters and Setters
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

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public BigDecimal getGia() {
        return gia;
    }

    public void setGia(BigDecimal gia) {
        this.gia = gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayCapNhat() {
        return ngayCapNhat;
    }

    public void setNgayCapNhat(LocalDateTime ngayCapNhat) {
        this.ngayCapNhat = ngayCapNhat;
    }

    @Override
    public String toString() {
        return tenSanPham + " [Mã " + maSanPham + "] - " + gia + "đ - " + trangThai;
    }
}