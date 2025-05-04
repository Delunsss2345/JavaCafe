//Người làm: Hà Hoàng Anh 
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entities.ChiTietNhapHang;
import entities.NhapHang;
import entities.SanPham;
import ConnectDB.DatabaseConnection;

public class ChiTietNhapHangDAO {
	private Connection connection;

    public ChiTietNhapHangDAO() throws SQLException {
    	this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Failed to establish a database connection.");
        }
    }
    
    public boolean themChiTietNhapHang(ChiTietNhapHang chiTiet) throws SQLException {
        String sql = ""
            + "INSERT INTO ChiTietNhapHang "
            + "  (MaNH, MaSP,TenSanPham, SoLuong, GiaNhap) "
            + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chiTiet.getNhapHang().getMaNhapHang());
            ps.setInt(2, chiTiet.getSanPham().getMaSanPham());
            ps.setString(3, chiTiet.getTenSanPham());
            ps.setInt(4, chiTiet.getSoLuong());
            ps.setDouble(5, chiTiet.getDonGia());
            return ps.executeUpdate() > 0;
        }
    }
    
    public List<ChiTietNhapHang> getChiTietNhapHangTheoMaNhapHang(int maNH) throws SQLException {
    	List<ChiTietNhapHang> ds = new ArrayList<ChiTietNhapHang>();
    	String sql = "SELECT * FROM ChiTietNhapHang WHERE MaNH = ?";
    	try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
    		stmt.setInt(1, maNH);
    		ResultSet rs = stmt.executeQuery();
    		while (rs.next()) {
    			int maCTNH = rs.getInt(1);
    			int maNhapHang = rs.getInt(2);
    			int maSP = rs.getInt(3);
    			String tenSanPham = rs.getString(4);
    			int soLuong = rs.getInt(5);
    			double giaNhap = rs.getDouble(6);
    			double thanhTien = rs.getDouble(7);
    			ChiTietNhapHang ctnh = new ChiTietNhapHang(maCTNH, new NhapHang(maNhapHang), new SanPham(maSP), tenSanPham, soLuong, giaNhap, thanhTien);
    			ds.add(ctnh);
    		}
    	}
    	return ds;
    }
}
