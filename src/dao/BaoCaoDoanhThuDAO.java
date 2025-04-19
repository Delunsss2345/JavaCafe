// Người làm: Hà Hoàng Anh 
package dao;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;

import ConnectDB.DatabaseConnection;

public class BaoCaoDoanhThuDAO {
    private Connection connection;

    public BaoCaoDoanhThuDAO() throws SQLException {
    	this.connection = DatabaseConnection.getInstance().getConnection();
        if (this.connection == null || this.connection.isClosed()) {
            throw new SQLException("Failed to establish a database connection.");
        }
    }
    

    private String generateDateWhereClause(String dateColumn, Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            return " WHERE " + dateColumn + " BETWEEN ? AND ?";
        } else if (startDate != null) {
            return " WHERE " + dateColumn + " >= ?";
        } else if (endDate != null) {
            return " WHERE " + dateColumn + " <= ?";
        }
        return "";
    }

    private void setDateParameters(PreparedStatement ps, Date startDate, Date endDate) throws SQLException {
        int parameterIndex = 1;
        if (startDate != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(startDate.getTime()));
        }
        if (endDate != null) {
            ps.setDate(parameterIndex++, new java.sql.Date(endDate.getTime()));
        }
    }

    public int getTongSoHoaDon(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT COUNT(*) FROM HoaDon";
        String whereClause = generateDateWhereClause("NgayTao", startDate, endDate);
        sql += whereClause;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            // Set parameters for the date range
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public double getTongDoanhThu(Date startDate, Date endDate) throws SQLException {
        String sql = "SELECT SUM(TongTien) FROM HoaDon";
        String whereClause = generateDateWhereClause("NgayTao", startDate, endDate);
        sql += whereClause;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return 0.0;
    }
    
    public List<Entry<Date, Double>> getDoanhThuTheoNgay(Date startDate, Date endDate) throws SQLException {
        List<Entry<Date, Double>> doanhThuTheoNgay = new ArrayList<>();
        String sql = "SELECT CAST(NgayTao AS DATE) AS Ngay, SUM(TongTien) AS DoanhThu FROM HoaDon";
        String whereClause = generateDateWhereClause("NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY CAST(NgayTao AS DATE) ORDER BY Ngay";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doanhThuTheoNgay.add(new AbstractMap.SimpleEntry<>(rs.getDate("Ngay"), rs.getDouble("DoanhThu")));
                }
            }
        }
        return doanhThuTheoNgay;
    }

    public List<Entry<String, Double>> getDoanhThuTheoThang(Date startDate, Date endDate) throws SQLException {
        List<Entry<String, Double>> doanhThuTheoThang = new ArrayList<>();
        String sql = "SELECT DATEPART(year, NgayTao) AS Nam, DATEPART(month, NgayTao) AS Thang, SUM(TongTien) AS DoanhThu FROM HoaDon";
        String whereClause = generateDateWhereClause("NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY DATEPART(year, NgayTao), DATEPART(month, NgayTao) ORDER BY Nam, Thang";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String thangNam = String.format("%02d/%d", rs.getInt("Thang"), rs.getInt("Nam"));
                    doanhThuTheoThang.add(new AbstractMap.SimpleEntry<>(thangNam, rs.getDouble("DoanhThu")));
                }
            }
        }
        return doanhThuTheoThang;
    }

    public List<Entry<String, Double>> getDoanhThuTheoNhanVien(Date startDate, Date endDate) throws SQLException {
        List<Entry<String, Double>> doanhThuTheoNhanVien = new ArrayList<>();
        String sql = "SELECT NV.Ho + ' ' + NV.Ten AS TenNV, SUM(HD.TongTien) AS DoanhThu " +
                     "FROM HoaDon HD JOIN NhanVien NV ON HD.MaNV = NV.MaNV";
        String whereClause = generateDateWhereClause("HD.NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY NV.Ho, NV.Ten ORDER BY DoanhThu DESC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doanhThuTheoNhanVien.add(new AbstractMap.SimpleEntry<>(rs.getString("TenNV"), rs.getDouble("DoanhThu")));
                }
            }
        }
        return doanhThuTheoNhanVien;
    }


public List<Entry<String, Integer>> getSanPhamBanChayNhat(Date startDate, Date endDate) throws SQLException {
    List<Entry<String, Integer>> sanPhamBanChay = new ArrayList<>();
    String sql = "SELECT SP.TenSanPham, SUM(CTHD.SoLuong) AS TongSoLuong " +
                 "FROM ChiTietHoaDon CTHD " +
                 "JOIN SanPham SP ON CTHD.MaSP = SP.MaSanPham " +
                 "JOIN HoaDon HD ON CTHD.MaHD = HD.MaHD";
    String whereClause = generateDateWhereClause("HD.NgayTao", startDate, endDate);
    sql += whereClause + " GROUP BY SP.TenSanPham ORDER BY TongSoLuong DESC";

    try (Connection connection = DatabaseConnection.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {
        setDateParameters(ps, startDate, endDate);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                sanPhamBanChay.add(new AbstractMap.SimpleEntry<>(
                    rs.getString("TenSanPham"), rs.getInt("TongSoLuong")
                ));
            }
        }
    }
    return sanPhamBanChay;
}




    public List<Entry<String, Double>> getDoanhThuTheoSanPham(Date startDate, Date endDate) throws SQLException {
        List<Entry<String, Double>> doanhThuTheoSanPham = new ArrayList<>();
        String sql = "SELECT SP.TenSanPham, SUM(CTHD.ThanhTien) AS TongDoanhThu " +
                     "FROM ChiTietHoaDon CTHD " +
                     "JOIN SanPham SP ON CTHD.MaSP = SP.MaSanPham " +
                     "JOIN HoaDon HD ON CTHD.MaHD = HD.MaHD";
        String whereClause = generateDateWhereClause("HD.NgayTao", startDate, endDate);
        sql += whereClause + " GROUP BY SP.TenSanPham ORDER BY TongDoanhThu DESC";

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doanhThuTheoSanPham.add(
                        new AbstractMap.SimpleEntry<>(
                            rs.getString("TenSanPham"),
                            rs.getDouble("TongDoanhThu")
                        )
                    );
                }
            }
        }
        return doanhThuTheoSanPham;
    }


    public String getThongTinTungHoaDon(Date startDate, Date endDate) throws SQLException {
        StringBuilder thongTinHoaDon = new StringBuilder();
        String sql = "SELECT HD.MaHD, NV.Ho + ' ' + NV.Ten AS TenNV, HD.NgayTao, HD.TongTien FROM HoaDon HD JOIN NhanVien NV ON HD.MaNV = NV.MaNV ";
        String whereClause = generateDateWhereClause("HD.NgayTao", startDate, endDate);
        sql += whereClause;

        try (Connection connection = DatabaseConnection.getInstance().getConnection();
                PreparedStatement ps = connection.prepareStatement(sql)) {
            setDateParameters(ps, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    thongTinHoaDon.append("Mã Hóa Đơn: ").append(rs.getString("MaHD"))
                                  .append(", Nhân Viên: ").append(rs.getString("TenNV"))
                                  .append(", Ngày Tạo: ").append(rs.getDate("NgayTao"))
                                  .append(", Tổng Tiền: ").append(rs.getDouble("TongTien"))
                                  .append("\n");
                }
            }
        }
        return thongTinHoaDon.toString();
    }


	public Map<Integer, Double> getDoanhThuTheoThangTrongNam(int nam) {
		Map<Integer, Double> doanhThuTheoThang = new HashMap<>();
		String sql = "SELECT MONTH(NgayTao) AS Thang, SUM(TongTien) AS DoanhThu "
				+ "FROM HoaDon WHERE YEAR(NgayTao) = ? " + "GROUP BY MONTH(NgayTao) ORDER BY Thang";

		try (Connection connection = DatabaseConnection.getInstance().getConnection();
	             PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.setInt(1, nam);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					doanhThuTheoThang.put(rs.getInt("Thang"), rs.getDouble("DoanhThu"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return doanhThuTheoThang;
	}

	
}
