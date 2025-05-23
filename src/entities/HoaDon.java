//Nguyen Tuan Phat
package entities;

import java.time.LocalDateTime;

public class HoaDon {
    private int maHD;
    private LocalDateTime ngayTao;
    private double tongTien;
    private double tienKhachTra;
    private double tienThua;
    private int maNV; 
    private int maKH;    

    public HoaDon(int maHD, LocalDateTime ngayTao, double tongTien,
                  double tienKhachTra, double tienThua,
                  int maNV, int maKH) {
        this.maHD = maHD;
        this.ngayTao = ngayTao;
        this.tongTien = tongTien;
        this.tienKhachTra = tienKhachTra;
        this.tienThua = tienThua;
        this.maNV = maNV;
        this.maKH = maKH;  
    }

    // Getters & Setters
    public int getMaHD() { return maHD; }
    public void setMaHD(int maHD) { this.maHD = maHD; }

    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime ngayTao) { this.ngayTao = ngayTao; }

    public double getTongTien() { return tongTien; }
    public void setTongTien(double tongTien) { this.tongTien = tongTien; }

    public double getTienKhachTra() { return tienKhachTra; }
    public void setTienKhachTra(double tienKhachTra) { this.tienKhachTra = tienKhachTra; }

    public double getTienThua() { return tienThua; }
    public void setTienThua(double tienThua) { this.tienThua = tienThua; }

    public int getMaNV() { return maNV; }
    public void setMaNV(int maNV) { this.maNV = maNV; }

    public int getMaKH() { return maKH; }  
    public void setMaKH(int maKH) { this.maKH = maKH; }  

    @Override
    public String toString() {
        return "HoaDon [maHD=" + maHD + ", ngayTao=" + ngayTao +
               ", tongTien=" + tongTien + ", tienKhachTra=" + tienKhachTra +
               ", tienThua=" + tienThua + ", maNV=" + maNV +
               ", maKH=" + maKH + "]";
    }
}
