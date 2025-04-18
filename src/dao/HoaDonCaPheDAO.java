package dao;

import entities.HoaDon;
import entities.ChiTietHoaDonCaPhe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonCaPheDAO {
    private Connection conn;

    public HoaDonCaPheDAO(Connection conn) {
        this.conn = conn;
    }

    // Thêm hóa đơn mới - để SQL Server tự tăng MaHD
    public int insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setTimestamp(1, Timestamp.valueOf(hd.getNgayTao()));
            ps.setDouble(2, hd.getTongTien());
            ps.setDouble(3, hd.getTienKhachTra());
            ps.setDouble(4, hd.getTienThua());
            ps.setInt(5, hd.getMaNV());
            ps.setInt(6, hd.getMaKH());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int maHD = rs.getInt(1);
                    hd.setMaHD(maHD); // Gán vào đối tượng
                    return maHD;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    // Lấy tên khách hàng từ MaKH
    public String getTenKhachHangByMaKH(int maKH) throws SQLException {
        String sql = "SELECT Ho, Ten FROM KhachHang WHERE MaKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Ho") + " " + rs.getString("Ten");
                }
            }
        }
        return null;
    }
    // Lấy tất cả hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getInt("MaHD"),
                        rs.getTimestamp("NgayTao").toLocalDateTime(),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienKhachTra"),
                        rs.getDouble("TienThua"),
                        rs.getInt("MaNV"),
                        rs.getInt("MaKH")
                );
                ds.add(hd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // Lấy hóa đơn theo mã
    public HoaDon getHoaDonByMaHD(int maHD) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HoaDon(
                        rs.getInt("MaHD"),
                        rs.getTimestamp("NgayTao").toLocalDateTime(),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienKhachTra"),
                        rs.getDouble("TienThua"),
                        rs.getInt("MaNV"),
                        rs.getInt("MaKH")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Xóa hóa đơn theo mã
    public boolean deleteHoaDon(int maHD) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Tạo hóa đơn và chi tiết hóa đơn
    public boolean taoHoaDon(HoaDon hd, List<ChiTietHoaDonCaPhe> dsCT) throws SQLException {
        Connection con = conn;
        try {
            con.setAutoCommit(false);

            // Insert hóa đơn
            String sqlHD = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psHD = con.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS)) {
                psHD.setTimestamp(1, Timestamp.valueOf(hd.getNgayTao()));
                psHD.setDouble(2, hd.getTongTien());
                psHD.setDouble(3, hd.getTienKhachTra());
                psHD.setDouble(4, hd.getTienThua());
                psHD.setInt(5, hd.getMaNV());
                psHD.setInt(6, hd.getMaKH());
                psHD.executeUpdate();

                ResultSet rs = psHD.getGeneratedKeys();
                if (rs.next()) {
                    int maHoaDon = rs.getInt(1);
                    hd.setMaHD(maHoaDon);

                    // Insert chi tiết hóa đơn
                    String sqlCT = "INSERT INTO ChiTietHoaDonCaPhe (MaHoaDon, MaSanPham, TenSanPham, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psCT = con.prepareStatement(sqlCT)) {
                        for (ChiTietHoaDonCaPhe ct : dsCT) {
                            psCT.setInt(1, maHoaDon);
                            psCT.setInt(2, ct.getMaSanPham());
                            psCT.setString(3, ct.getTenSanPham());
                            psCT.setInt(4, ct.getSoLuong());
                            psCT.setDouble(5, ct.getDonGia());
                            psCT.setDouble(6, ct.getThanhTien());
                            psCT.addBatch();
                        }
                        psCT.executeBatch();
                    }
                }
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
