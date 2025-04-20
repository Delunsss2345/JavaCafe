package entities;
//Người làm phạm thanh huy 
public class TaiKhoan {
	private String tenDangNhap ; 
	private String matKhau ;
	private PhanQuyen quyen ;
	
	
	public TaiKhoan() {
		super();
	}
	
	public TaiKhoan(String tenDangNhap, String matKhau, PhanQuyen quyen) {
		super();
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
		this.quyen = quyen;
	}


	public PhanQuyen getQuyen() {
		return quyen;
	}


	public void setQuyen(PhanQuyen quyen) {
		this.quyen = quyen;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}



	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}



	public String getMatKhau() {
		return matKhau;
	}



	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}
	
	
	
}
