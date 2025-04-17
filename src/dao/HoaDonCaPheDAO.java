package dao;

import entities.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HoaDonCaPheDAO {
    private Connection conn;

    public HoaDonCaPheDAO(Connection conn) {
<<<<<<< HEAD
        super();
        this.conn = conn;
    }

    // Thêm hóa đơn vào database
    public boolean insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap()));
            ps.setDouble(2, hd.getTongTien());
            ps.setDouble(3, hd.getTienKhachTra());
            ps.setDouble(4, hd.getTienThua());
            ps.setInt(5, hd.getMaNhanVien());
            ps.setInt(6, hd.getMaKhachHang()); // MaKH có thể là NULL, nếu không có khách hàng thì đặt thành NULL
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy tất cả hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                        rs.getInt("MaHD"), // Mã hóa đơn (ID)
                        rs.getTimestamp("NgayTao").toLocalDateTime(), // Ngày tạo
                        rs.getDouble("TongTien"), // Tổng tiền
                        rs.getDouble("TienKhachTra"), // Tiền khách trả
                        rs.getDouble("TienThua"), // Tiền thừa
                        rs.getInt("MaNV"), // Mã nhân viên
                        rs.getInt("MaKH") // Mã khách hàng (nếu có)
                );
                ds.add(hd);
=======
        this.conn = conn;
    }

    // Thêm hóa đơn mới - để SQL Server tự tăng MaHD
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
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

<<<<<<< HEAD
    // Lấy hóa đơn theo mã
    public HoaDon getHoaDonByID(int maHoaDon) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
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
=======
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
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

<<<<<<< HEAD
    // Xóa hóa đơn theo mã
    public boolean deleteHoaDon(int maHoaDon) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHoaDon);
=======
    public boolean deleteHoaDon(int maHD) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
<<<<<<< HEAD
    // Phương thức lấy tên khách hàng theo MaKH
    public String getTenKhachHangByMaKH(int maKH) throws SQLException {
        String tenKhachHang = "";
        String sql = "SELECT TenKhachHang FROM KhachHang WHERE MaKH = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tenKhachHang = rs.getString("TenKhachHang");
                }
            }
        }
        return tenKhachHang;
    }
 // Tạo hóa đơn và chi tiết hóa đơn
    public boolean taoHoaDon(HoaDon hd, List<ChiTietHoaDonCaPhe> dsCT) throws SQLException {
        Connection con = DatabaseConnection.getInstance().getConnection();
        try {
            con.setAutoCommit(false);

            // Insert hóa đơn
            String sqlHD = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement psHD = con.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS)) {
                psHD.setTimestamp(1, Timestamp.valueOf(hd.getNgayLap()));
                psHD.setDouble(2, hd.getTongTien());
                psHD.setDouble(3, hd.getTienKhachTra());
                psHD.setDouble(4, hd.getTienThua());
                psHD.setInt(5, hd.getMaNhanVien());
                psHD.setInt(6, hd.getMaKhachHang());
                psHD.executeUpdate();

                // Lấy mã hóa đơn vừa tạo
                ResultSet rs = psHD.getGeneratedKeys();
                if (rs.next()) {
                    int maHoaDon = rs.getInt(1);
                    hd.setMaHoaDon(maHoaDon);

                    // Insert chi tiết hóa đơn
                    String sqlCT = "INSERT INTO ChiTietHoaDonCaPhe (MaHoaDon, MaSanPham, TenSanPham, SoLuong, DonGia, ThanhTien) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psCT = con.prepareStatement(sqlCT)) {
                        for (ChiTietHoaDonCaPhe ct : dsCT) {
                            psCT.setInt(1, maHoaDon);
                            psCT.setInt(2, ct.getMaSanPham());
                            psCT.setString(3, ct.getTenSanPham());   // Chèn tên sản phẩm
                            psCT.setInt(4, ct.getSoLuong());
                            psCT.setDouble(5, ct.getDonGia());
                            psCT.setDouble(6, ct.getThanhTien());  // Tính toán và chèn thành tiền (SoLuong * DonGia)
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

=======
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
}
