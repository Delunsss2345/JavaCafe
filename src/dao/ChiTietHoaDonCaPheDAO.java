//Nguyễn Tuấn Phát
package dao;

import entities.ChiTietHoaDonCaPhe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonCaPheDAO {
    private Connection conn;

    public ChiTietHoaDonCaPheDAO(Connection conn) {
        this.conn = conn;
    }

    // Thêm một chi tiết hóa đơn
    public boolean insertChiTiet(ChiTietHoaDonCaPhe ct) {
        String sql = "INSERT INTO ChiTietHoaDon (maHoaDon, maSanPham, tenSanPham, soLuong, donGia, thanhTien) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, ct.getMaHoaDon());
            ps.setLong(2, ct.getMaSanPham());
            ps.setString(3, ct.getTenSanPham());
            ps.setInt(4, ct.getSoLuong());
            ps.setDouble(5, ct.getDonGia());
            ps.setDouble(6, ct.getThanhTien());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    // Lấy danh sách chi tiết hóa đơn theo mã hóa đơn
//    public List<ChiTietHoaDonCaPhe> getChiTietByMaHoaDon(String maHoaDon) {
//        List<ChiTietHoaDonCaPhe> ds = new ArrayList<>();
//        String sql = "SELECT * FROM ChiTietHoaDon WHERE maHoaDon = ?";
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            ps.setString(1, maHoaDon);
//            ResultSet rs = ps.executeQuery();
//
//            while (rs.next()) {
//                ChiTietHoaDonCaPhe ct = new ChiTietHoaDonCaPhe(
//                        rs.getString("maHoaDon"),
//                        rs.getString("maSanPham"),
//                        rs.getString("tenSanPham"),
//                        rs.getInt("soLuong"),
//                        rs.getDouble("donGia"),
//                        rs.getDouble("thanhTien")
//                );
//                ds.add(ct);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return ds;
//    }

    // Xóa tất cả chi tiết hóa đơn theo mã hóa đơn
    public boolean deleteChiTietByMaHoaDon(String maHoaDon) {
        String sql = "DELETE FROM ChiTietHoaDon WHERE maHoaDon = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maHoaDon);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
