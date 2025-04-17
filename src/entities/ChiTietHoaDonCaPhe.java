//Nguyễn Tuấn Phát
package entities;

public class ChiTietHoaDonCaPhe {
    private String maHoaDon;
    private String maSP;
    private String tenSP;
    private int soLuong;
    private double donGia;
    private double thanhTien;

    // ✅ THÊM constructor này:
    public ChiTietHoaDonCaPhe(String maHD, String maSP, String tenSP, int soLuong, double donGia, double thanhTien) {
        this.maHoaDon = maHD;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
    
    
    public String getMaHoaDon() {
		return maHoaDon;
	}


	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}


	public String getMaSP() {
		return maSP;
	}


	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}


	public String getTenSP() {
		return tenSP;
	}


	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
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
                "maHoaDon='" + maHoaDon + '\'' +
                ", maSanPham='" + maSP + '\'' +
                ", tenSanPham='" + tenSP + '\'' +
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
