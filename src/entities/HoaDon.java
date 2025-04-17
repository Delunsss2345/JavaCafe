package entities;

import java.time.LocalDateTime;

public class HoaDon {
<<<<<<< HEAD
    private int maHoaDon; 
    private LocalDateTime ngayLap; 
    private double tongTien; 
    private double tienKhachTra;
    private double tienThua; 
    private int maNhanVien; 
    private int maKhachHang; 

  
    public HoaDon() {
    }

   
    public HoaDon(int maHoaDon, LocalDateTime ngayLap, double tongTien, double tienKhachTra, double tienThua,
                  int maNhanVien, int maKhachHang) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.tongTien = tongTien;
        this.tienKhachTra = tienKhachTra;
        this.tienThua = tienThua;
        this.maNhanVien = maNhanVien;
        this.maKhachHang = maKhachHang;
    }

    // Getters & Setters
    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
=======
    private int maHD;
    private int maDH; //bo
    private LocalDateTime ngayTao;
    private double tongTien;
    private double tienKhachTra;	
    private double tienThua;
    private int maNV; //bo
    private int maKH;

    public HoaDon(int maHD, int maDH, LocalDateTime ngayTao, double tongTien, double tienKhachTra, double tienThua, int maNV) {
        this.maHD = maHD;
        this.maDH = maDH;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.tienKhachTra = tienKhachTra;
        this.tienThua = tienThua;
        this.maNV = maNV;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

<<<<<<< HEAD
=======
    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienKhachTra() {
        return tienKhachTra;
<<<<<<< HEAD
    }

    public void setTienKhachTra(double tienKhachTra) {
        this.tienKhachTra = tienKhachTra;
    }

    public double getTienThua() {
        return tienThua;
    }

    public void setTienThua(double tienThua) {
        this.tienThua = tienThua;
    }

    public int getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(int maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHoaDon=" + maHoaDon +
                ", ngayLap=" + ngayLap +
                ", tongTien=" + tongTien +
                ", tienKhachTra=" + tienKhachTra +
                ", tienThua=" + tienThua +
                ", maNhanVien=" + maNhanVien +
                ", maKhachHang=" + maKhachHang +
                '}';
=======
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
    }

    public void setTienKhachTra(double tienKhachTra) {
        this.tienKhachTra = tienKhachTra;
    }

    public double getTienThua() {
        return tienThua;
    }

    public void setTienThua(double tienThua) {
        this.tienThua = tienThua;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", maDH=" + maDH + ", ngayTao=" + ngayTao + ", tongTien=" + tongTien
				+ ", tienKhachTra=" + tienKhachTra + ", tienThua=" + tienThua + ", maNV=" + maNV + "]";
	}
    
}
