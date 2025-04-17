package entities;

public class ChiTietHoaDonCaPhe {
<<<<<<< HEAD
    private int maCTHD;           
    private int maHoaDon;          
    private int maSanPham;         
    private String tenSanPham;     
    private int soLuong;          
    private double donGia;        
    private double thanhTien;      

    public ChiTietHoaDonCaPhe() {
    }

    public ChiTietHoaDonCaPhe(int maCTHD, int maHoaDon, int maSanPham, String tenSanPham,
                               int soLuong, double donGia, double thanhTien) {
        this.maCTHD = maCTHD;
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
=======
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
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }
<<<<<<< HEAD

    // Getters & Setters
    public int getMaCTHD() {
        return maCTHD;
    }

    public void setMaCTHD(int maCTHD) {
        this.maCTHD = maCTHD;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }
=======
    
    
    public String getMaHoaDon() {
		return maHoaDon;
	}


	public void setMaHoaDon(String maHoaDon) {
		this.maHoaDon = maHoaDon;
	}

>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62

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
<<<<<<< HEAD
                "maCTHD=" + maCTHD +
                ", maHoaDon=" + maHoaDon +
                ", maSanPham=" + maSanPham +
                ", tenSanPham='" + tenSanPham + '\'' +
=======
                "maHoaDon='" + maHoaDon + '\'' +
                ", maSanPham='" + maSP + '\'' +
                ", tenSanPham='" + tenSP + '\'' +
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
                ", soLuong=" + soLuong +
                ", donGia=" + donGia +
                ", thanhTien=" + thanhTien +
                '}';
    }
}
