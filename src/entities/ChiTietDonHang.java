package entities;

public class ChiTietDonHang {
    private int maChiTietDonHang;
    private DonHang donHang;
    private SanPham sanPham;
    private int soLuong;
    private float gia;

    public ChiTietDonHang() {
    }

    public ChiTietDonHang(int maChiTietDonHang, DonHang donHang, 
                         SanPham sanPham, int soLuong, float gia) {
        this.maChiTietDonHang = maChiTietDonHang;
        this.donHang = donHang;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
        this.gia = gia;
    }

    // Getters and Setters
    public int getMaChiTietDonHang() {
        return maChiTietDonHang;
    }

    public void setMaChiTietDonHang(int maChiTietDonHang) {
        this.maChiTietDonHang = maChiTietDonHang;
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public float getGia() {
        return gia;
    }

    public void setGia(float gia) {
        this.gia = gia;
    }

    public float tinhThanhTien() {
        return soLuong * gia;
    }

    @Override
    public String toString() {
        return sanPham.getTenSanPham() + " x" + soLuong + " - " + tinhThanhTien() + "đ";
    }
}