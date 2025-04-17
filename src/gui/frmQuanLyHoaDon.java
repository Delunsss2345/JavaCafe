// Nguyễn Tuấn Phát
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ConnectDB.DatabaseConnection;
import dao.HoaDonCaPheDAO;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class frmQuanLyHoaDon extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;
    private Connection conn;
    private static final long serialVersionUID = 1L;

    public frmQuanLyHoaDon() {
        setLayout(new BorderLayout());

        // Tạo model cho bảng
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Khách Hàng", "Tổng Tiền"});

        // Tạo bảng và gắn vào ScrollPane
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Kết nối DB
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            hoaDonDAO = new HoaDonCaPheDAO(conn);
            taiLaiDanhSachHoaDon(); // Tải dữ liệu ngay khi mở form
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách hóa đơn từ CSDL
     */
    public List<Object[]> getAllHoaDon() {
        List<Object[]> list = new ArrayList<>();
        String sql = """
            SELECT hd.MaHD, hd.NgayTao, (kh.Ho + ' ' + kh.Ten) AS HoTen, hd.TongTien
            FROM HoaDon hd
            JOIN DonHang dh ON hd.MaDH = dh.MaDH
            JOIN KhachHang kh ON dh.MaKH = kh.MaKH
            ORDER BY hd.NgayTao DESC
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("MaHD"),
                    rs.getTimestamp("NgayTao").toLocalDateTime(),
                    rs.getString("HoTen"), // sử dụng alias đã đặt trong SQL
                    rs.getDouble("TongTien")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * Tải lại dữ liệu hóa đơn lên bảng
     */
    public void taiLaiDanhSachHoaDon() {
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        List<Object[]> danhSach = getAllHoaDon();
        for (Object[] row : danhSach) {
            tableModel.addRow(row);
        }
    }
}
