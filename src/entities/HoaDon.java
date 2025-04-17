//Nguyễn Tuấn Phát
package entities;

import java.time.LocalDateTime;

public class HoaDon {
    private int maHD;
    private int maDH;
    private LocalDateTime ngayTao;
    private double tongTien;
    private double tienKhachTra;
    private double tienThua;
    private int maNV;

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
    }

    public int getMaDH() {
        return maDH;
    }

    public void setMaDH(int maDH) {
        this.maDH = maDH;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    public double getTienKhachTra() {
        return tienKhachTra;
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
