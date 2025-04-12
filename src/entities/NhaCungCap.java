package entities;

public class NhaCungCap {
    private int maNhaCungCap;
    private String ten;
    private String soDienThoai;
    private String email;
    private DiaChi diaChi;

    public NhaCungCap() {
    }

    public NhaCungCap(int maNhaCungCap, String ten, String soDienThoai, 
                     String email, DiaChi diaChi) {
        this.maNhaCungCap = maNhaCungCap;
        this.ten = ten;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.diaChi = diaChi;
    }

    // Getters and Setters
    public int getMaNhaCungCap() {
        return maNhaCungCap;
    }

    public void setMaNhaCungCap(int maNhaCungCap) {
        this.maNhaCungCap = maNhaCungCap;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DiaChi getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(DiaChi diaChi) {
        this.diaChi = diaChi;
    }

    @Override
    public String toString() {
        return "NhaCungCap [maNhaCungCap=" + maNhaCungCap + ", ten=" + ten + 
               ", soDienThoai=" + soDienThoai + ", email=" + email + 
               ", diaChi=" + diaChi + "]";
    }
}