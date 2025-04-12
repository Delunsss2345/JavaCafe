package entities;
//Người làm Phạm Thanh Huy
public class LoaiSanPham {
    private int maLoai;
    private String tenLoai;
    private String moTa;
    private String icon;

    public LoaiSanPham(int maLoai, String tenLoai, String moTa, String icon) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.moTa = moTa;
        this.icon = icon;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
    }
}