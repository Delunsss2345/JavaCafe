<<<<<<< HEAD
=======
// Nguyễn Tuấn Phát
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
<<<<<<< HEAD
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

=======
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
import ConnectDB.DatabaseConnection;
import dao.HoaDonCaPheDAO;

<<<<<<< HEAD
=======
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
public class frmQuanLyHoaDon extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;
<<<<<<< HEAD
    private static final long serialVersionUID = 1L;

    public frmQuanLyHoaDon() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Tiêu đề
        JLabel lblTitle = new JLabel("QUẢN LÝ HÓA ĐƠN", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(33, 115, 70));
        add(lblTitle, BorderLayout.NORTH);

        // Bảng dữ liệu
=======
    private Connection conn;
    private static final long serialVersionUID = 1L;

    public frmQuanLyHoaDon() {
        setLayout(new BorderLayout());

        // Tạo model cho bảng, thêm cột MaSP
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Khách Hàng", "Tổng Tiền", "Mã SP"}); // Thêm "Mã SP"

        // Tạo bảng và gắn vào ScrollPane
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
<<<<<<< HEAD
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));

        add(scrollPane, BorderLayout.CENTER);

        // Panel chức năng
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlControls.setBackground(Color.WHITE);

        JButton btnReload = new JButton("Tải lại");
        btnReload.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnReload.setBackground(new Color(33, 115, 70));
        btnReload.setForeground(Color.WHITE);
        btnReload.setFocusPainted(false);
        btnReload.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReload.addActionListener(e -> loadData());

        pnlControls.add(btnReload);
        add(pnlControls, BorderLayout.SOUTH);

=======
        add(scrollPane, BorderLayout.CENTER);

        // Kết nối DB
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            hoaDonDAO = new HoaDonCaPheDAO(conn);
            taiLaiDanhSachHoaDon(); // Tải dữ liệu ngay khi mở form
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    private void loadData() {
        try {
            // Lấy danh sách hóa đơn
            List<HoaDon> dsHoaDon = hoaDonDAO.getAllHoaDon();
            tableModel.setRowCount(0);
            for (HoaDon hd : dsHoaDon) {
                // Nếu bạn cần lấy thêm thông tin khách hàng, có thể thêm truy vấn vào đây
                String tenKhachHang = getTenKhachHang(hd.getMaKhachHang());  // Tạo phương thức lấy tên khách hàng

                tableModel.addRow(new Object[]{
                        hd.getMaHoaDon(),
                        hd.getNgayLap(),
                        tenKhachHang,
                        String.format("%,.0f VND", hd.getTongTien())
                });
=======
    /**
     * Lấy danh sách hóa đơn từ CSDL
     */
    public List<Object[]> getAllHoaDon() {
        List<Object[]> list = new ArrayList<>();

        // Sửa lại SQL: bỏ JOIN DonHang, thay bằng JOIN trực tiếp với KhachHang qua MaKH
        String sql = """
            SELECT hd.MaHD, hd.NgayTao, (kh.Ho + ' ' + kh.Ten) AS HoTen, hd.TongTien, cthd.MaSP
            FROM HoaDon hd
            JOIN KhachHang kh ON hd.MaKH = kh.MaKH
            JOIN ChiTietHoaDon cthd ON hd.MaHD = cthd.MaHD
            ORDER BY hd.NgayTao DESC
        """;

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[] {
                    rs.getInt("MaHD"),
                    rs.getTimestamp("NgayTao").toLocalDateTime(),
                    rs.getString("HoTen"),
                    rs.getDouble("TongTien"),
                    rs.getString("MaSP")
                };
                list.add(row);
>>>>>>> 9f7c4dae8014534fdc391636cd6b0e6eb16f1e62
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

    // Giả sử phương thức này sẽ lấy tên khách hàng dựa vào MaKH
    private String getTenKhachHang(int maKH) throws SQLException {
        // Thực hiện truy vấn lấy tên khách hàng theo MaKH
        // Ví dụ, bạn có thể gọi một phương thức trong DAO để lấy tên khách hàng từ bảng KhachHang
        return hoaDonDAO.getTenKhachHangByMaKH(maKH);  // Cập nhật phương thức trong DAO nếu cần
    }
}
