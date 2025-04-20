package dao;

import entities.HoaDon;
import entities.ChiTietHoaDonCaPhe;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import ConnectDB.DatabaseConnection;

public class HoaDonCaPheDAO {
    // Xóa trường conn và constructor

    // Thêm hóa đơn mới - để SQL Server tự tăng MaHD
    public int insertHoaDon(HoaDon hd) {
        String sql = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
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
                    rs.close();
                    ps.close();
                    return maHD;
                }
                rs.close();
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
        return -1;
    }
    
    // Lấy tên khách hàng từ MaKH
    public String getTenKhachHangByMaKH(int maKH) {
        String sql = "SELECT Ho, Ten FROM KhachHang WHERE MaKH = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, maKH);
            ResultSet rs = ps.executeQuery();
            String result = null;
            if (rs.next()) {
                result = rs.getString("Ho") + " " + rs.getString("Ten");
            }
            
            rs.close();
            ps.close();
            
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }
    
    // Lấy tất cả hóa đơn
    public List<HoaDon> getAllHoaDon() {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT * FROM HoaDon";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

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
            
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
        return ds;
    }

    // Lấy hóa đơn theo mã
    public HoaDon getHoaDonByMaHD(int maHD) {
        String sql = "SELECT * FROM HoaDon WHERE MaHD = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, maHD);
            ResultSet rs = ps.executeQuery();
            HoaDon hd = null;
            if (rs.next()) {
                hd = new HoaDon(
                        rs.getInt("MaHD"),
                        rs.getTimestamp("NgayTao").toLocalDateTime(),
                        rs.getDouble("TongTien"),
                        rs.getDouble("TienKhachTra"),
                        rs.getDouble("TienThua"),
                        rs.getInt("MaNV"),
                        rs.getInt("MaKH")
                );
            }
            
            rs.close();
            ps.close();
            
            return hd;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    // Xóa hóa đơn theo mã
    public boolean deleteHoaDon(int maHD) {
        String sql = "DELETE FROM HoaDon WHERE MaHD = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, maHD);
            int result = ps.executeUpdate();
            ps.close();
            
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DatabaseConnection.getInstance().closeConnection();
        }
    }

    // Tạo hóa đơn và chi tiết hóa đơn - trường hợp giao dịch đặc biệt
    public boolean taoHoaDon(HoaDon hd, List<ChiTietHoaDonCaPhe> dsCT) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            conn.setAutoCommit(false);

            // Insert hóa đơn
            String sqlHD = "INSERT INTO HoaDon (NgayTao, TongTien, TienKhachTra, TienThua, MaNV, MaKH) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psHD = conn.prepareStatement(sqlHD, Statement.RETURN_GENERATED_KEYS);
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
                PreparedStatement psCT = conn.prepareStatement(sqlCT);
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
                psCT.close();
            }
            rs.close();
            psHD.close();

            conn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { 
                    conn.rollback(); 
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DatabaseConnection.getInstance().closeConnection();
        }
    }
}
