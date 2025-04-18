package entities;
//Người làm phạm thanh huy 
public class TaiKhoan {
	private String tenDangNhap ; 
	private String matKhau ;

	
	
	public TaiKhoan() {
		super();
	}


	public TaiKhoan(String tenDangNhap, String matKhau) {
		super();
		this.tenDangNhap = tenDangNhap;
		this.matKhau = matKhau;
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
