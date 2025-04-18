package dao;
//Người làm phạm thanh huy
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ConnectDB.DatabaseConnection;
import entities.TaiKhoan;

public class TaiKhoanDAO {
	public ArrayList<TaiKhoan> dsTaiKhoan() {
		String sql = " SELECT TenTaiKhoan , MatKhau FROM DBO.TaiKhoan " ; 
		ArrayList<TaiKhoan> dsTk = new ArrayList<TaiKhoan>() ;
		try(Connection conn = DatabaseConnection.getInstance().getConnection() ; 
			PreparedStatement stmt = conn.prepareStatement(sql) ; 
			ResultSet rs = stmt.executeQuery() ; 
				) {
			while(rs.next()) {
				TaiKhoan tk = new TaiKhoan() ;
				tk.setTenDangNhap(rs.getString("TenTaiKhoan"));
				tk.setMatKhau(rs.getString("MatKhau"));
				dsTk.add(tk) ; 
			}
			
			return dsTk ; 
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Danh Sach Tai Khoan Loi");
		}
		return dsTk;
	}
}
