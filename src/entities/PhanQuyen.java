package entities;

public class PhanQuyen {
	 private int maQuyen ; 
	 private String tenQuyen ;
	 
	public PhanQuyen() {
		super();
		
	}
	
	public PhanQuyen(int maQuyen, String tenQuyen) {
		super();
		this.maQuyen = maQuyen;
		this.tenQuyen = tenQuyen;
	}
	
	public int getMaQuyen() {
		return maQuyen;
	}
	public void setMaQuyen(int maQuyen) {
		this.maQuyen = maQuyen;
	}
	public String getTenQuyen() {
		return tenQuyen;
	}
	public void setTenQuyen(String tenQuyen) {
		this.tenQuyen = tenQuyen;
	}
	 
	 
}
