//Người làm: Hà Hoàng Anh 
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import ConnectDB.DatabaseConnection;
import entities.NhaCungCap;
import entities.NhanVien;
import entities.NhapHang;

public class NhapHangDAO {
	private Connection connection;

    public NhapHangDAO() throws SQLException {
    	this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Failed to establish a database connection.");
        }
    }
    public List<NhapHang> getDanhSachNhapHang() throws SQLException {
    	List<NhapHang> ds = new ArrayList<NhapHang>();
    	String sql = "SELECT * FROM NhapHang";
    	try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
    		ResultSet rs = ps.executeQuery();
	    		while (rs.next()) {
	    		int maNH = rs.getInt(1);
	    		int maNV = rs.getInt(2);
	    		int maNCC = rs.getInt(3);
	    		Date date = rs.getDate(4);
	    		double tongTien = rs.getDouble(5);
	    		String trangThai = rs.getString(6);
	    		String ghiChu = (rs.getString(7) == null) ? "Không có" : rs.getString(7);
	    		NhapHang nh = new NhapHang(maNH, new NhanVien(maNV), new NhaCungCap(maNCC), date , tongTien, trangThai, ghiChu);
	    		ds.add(nh);
    		}
    	}
    	return ds;
    }
    
    public boolean themDonNhapHang(NhapHang nh) {
    	String sql = "INSERT INTO NhapHang(MaNV,MaNCC,NgayNhap,TrangThai,GhiChu)"
    			+ "VALUES (?,?,?,?,?)";
    	try (Connection conn = DatabaseConnection.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
    		stmt.setInt(1, nh.getNhanVien().getMaNV());
    		stmt.setInt(2, nh.getNhaCungCap().getMaNCC());
    		stmt.setDate(3, new java.sql.Date(nh.getNgayNhap().getTime()));
    		stmt.setString(4, nh.getTrangThai());
    		stmt.setString(5, nh.getGhiChu());
    		return stmt.executeUpdate() > 0;
    	} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
    	
    }
	public void updateGhiChu(int maNH, String string) {
		String sql = "UPDATE NhapHang SET GhiChu = ? WHERE MaNH = ?";
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, string);
			stmt.setInt(2, maNH);
			stmt.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi cập nhật ghi chú: " + e.getMessage());
		}
		
	}
	
	public void updateTrangThai(int maNH, String string) {
		String sql = "UPDATE NhapHang SET TrangThai = ? WHERE MaNH = ?";
		try (Connection conn = DatabaseConnection.getInstance().getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, string);
			stmt.setInt(2, maNH);
			stmt.executeUpdate();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Lỗi cập nhật trạng thái: " + e.getMessage());
		}
		
	}
}
