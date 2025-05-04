//Người làm: Hà Hoàng Anh 
package entities;

import java.util.Date;

public class NhapHang {
	private int maNhapHang;
	private NhanVien nhanVien;
	private NhaCungCap nhaCungCap;
	private Date ngayNhap;
	private double tongTien;
	private String trangThai;
	private String ghiChu;
	
	public NhapHang(int maNhapHang, NhanVien nhanVien, NhaCungCap nhaCungCap, Date ngayNhap, double tongTien,
			String trangThai, String ghiChu) {
		super();
		this.maNhapHang = maNhapHang;
		this.nhanVien = nhanVien;
		this.nhaCungCap = nhaCungCap;
		this.ngayNhap = ngayNhap;
		this.tongTien = tongTien;
		this.trangThai = trangThai;
		this.ghiChu = ghiChu;
	}
	public NhapHang(int maNhapHang) {
		this.maNhapHang = maNhapHang;
	}
	public int getMaNhapHang() {
		return maNhapHang;
	}
	public void setMaNhapHang(int maNhapHang) {
		this.maNhapHang = maNhapHang;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public NhaCungCap getNhaCungCap() {
		return nhaCungCap;
	}
	public void setNhaCungCap(NhaCungCap nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}
	public Date getNgayNhap() {
		return ngayNhap;
	}
	public void setNgayNhap(Date ngayNhap) {
		if (ngayNhap == null) {
			throw new IllegalArgumentException("Ngày nhập không hợp lệ");
		}
		this.ngayNhap = ngayNhap;
	}
	public double getTongTien() {
		return tongTien;
	}
	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
	public String getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}
	public String getGhiChu() {
		return ghiChu;
	}
	public void setGhiChu(String ghiChu) {
		this.ghiChu = ghiChu;
	}
	
	
}
