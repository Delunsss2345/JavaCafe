//Nguyễn Tuấn Phát
package dao;

import entities.ChiTietHoaDonCaPhe;
import entities.HoaDon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import ConnectDB.DatabaseConnection;

public class HoaDonCaPheDAO {
    private Connection conn;

    public HoaDonCaPheDAO(Connection conn) {
		super();
		this.conn = conn;
	}


	// Thêm hóa đơn vào database
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (maHoaDon, ngayLap, maNhanVien, tenNhanVien, maKhachHang, tenKhachHang, tongTien) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hd.getMaHoaDon());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayLap()));
            ps.setString(3, hd.getMaNhanVien());
            ps.setString(4, hd.getTenNhanVien());
            ps.setString(5, hd.getMaKhachHang());
            ps.setString(6, hd.getTenKhachHang());
            ps.setDouble(7, hd.getTongTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tất cả hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon ORDER BY ngayLap DESC";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getTimestamp("ngayLap").toLocalDateTime(),
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getDouble("tongTien")
                );
                ds.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Lấy hóa đơn theo mã
    public HoaDon getHoaDonByID(String maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE maHoaDon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HoaDon(
                        rs.getString("maHoaDon"),
                        rs.getTimestamp("ngayLap").toLocalDateTime(),
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien"),
                        rs.getString("maKhachHang"),
                        rs.getString("tenKhachHang"),
                        rs.getDouble("tongTien")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xóa hóa đơn theo mã
    public boolean deleteHoaDon(String maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE maHoaDon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean taoHoaDon(HoaDon hd, List<ChiTietHoaDonCaPhe> dsCT) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);

            // Insert hóa đơn
            String sqlHD = "INSERT INTO HoaDon (...) VALUES (...)";
            // thực hiện insert, lấy ra mã hóa đơn vừa tạo

            // Insert chi tiết hóa đơn
            for (ChiTietHoaDonCaPhe ct : dsCT) {
                // gán mã hóa đơn
                ct.setMaHoaDon(hd.getMaHoaDon());
                // thực hiện insert chi tiết
            }

            con.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try { con.rollback(); } catch (SQLException ex) {}
            return false;
        }
    }

}
