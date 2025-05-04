//Người làm: Hà Hoàng Anh 
package entities;

public class ChiTietNhapHang {
	private int maCTNH;
	private NhapHang nhapHang;
	private SanPham sanPham;
	private String tenSanPham;
	private int soLuong;
	private double donGia;
	private double thanhTien;
	public ChiTietNhapHang(int maCTNH, NhapHang nhapHang, SanPham sanPham, String tenSanPham, int soLuong,
			double donGia, double thanhTien) {
		super();
		this.maCTNH = maCTNH;
		this.nhapHang = nhapHang;
		this.sanPham = sanPham;
		this.tenSanPham = tenSanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.thanhTien = thanhTien;
	}
	public ChiTietNhapHang(NhapHang nhapHang, SanPham sanPham, String tenSanPham, int soLuong, double donGia) {
		super();
		this.nhapHang = nhapHang;
		this.sanPham = sanPham;
		this.tenSanPham = tenSanPham;
		this.soLuong = soLuong;
		this.donGia = donGia;
	}
	public int getMaCTNH() {
		return maCTNH;
	}
	public void setMaCTNH(int maCTNH) {
		this.maCTNH = maCTNH;
	}
	public NhapHang getNhapHang() {
		return nhapHang;
	}
	public void setNhapHang(NhapHang nhapHang) {
		this.nhapHang = nhapHang;
	}
	public SanPham getSanPham() {
		return sanPham;
	}
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
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
		if (soLuong <= 0) {
			throw new IllegalArgumentException("Số lượng không hợp lệ");
		}
		this.soLuong = soLuong;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		if (donGia <= 0) {
			throw new IllegalArgumentException("Giá nhập không hợp lệ");
		}
		this.donGia = donGia;
	}
	public double getThanhTien() {
		return thanhTien;
	}
	public void setThanhTien(double thanhTien) {
		this.thanhTien = thanhTien;
	}
	
	
}
