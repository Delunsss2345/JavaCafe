//Nguyễn Tuấn Phát
package dao;

import entities.HoaDon;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HoaDonCaPheDAO {
    private Connection conn;

    public HoaDonCaPheDAO(Connection conn) {
        this.conn = conn;
    }

    // Thêm hóa đơn mới
    public int insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (MaDH, NgayTao, TongTien, TienKhachTra, TienThua, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, hd.getMaDH());
            ps.setTimestamp(2, Timestamp.valueOf(hd.getNgayTao()));
            ps.setDouble(3, hd.getTongTien());
            ps.setDouble(4, hd.getTienKhachTra());
            ps.setDouble(5, hd.getTienThua());
            ps.setInt(6, hd.getMaNV());

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

    public List<Object[]> getAllHoaDon() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT hd.MaHD, hd.NgayTao, kh.TenKH, hd.TongTien
            FROM HoaDon hd
            JOIN DonHang dh ON hd.MaDH = dh.MaDH
            JOIN KhachHang kh ON dh.MaKH = kh.MaKH
            ORDER BY hd.NgayTao DESC
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("MaHD"),
                    rs.getTimestamp("NgayTao").toLocalDateTime(),
                    rs.getString("TenKH"),
                    rs.getDouble("TongTien")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public HoaDon getHoaDonByMaHD(int maHD) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new HoaDon(
                    rs.getInt("MaHD"),
                    rs.getInt("MaDH"),
                    rs.getTimestamp("NgayTao").toLocalDateTime(),
                    rs.getDouble("TongTien"),
                    rs.getDouble("TienKhachTra"),
                    rs.getDouble("TienThua"),
                    rs.getInt("MaNV")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
}


}
