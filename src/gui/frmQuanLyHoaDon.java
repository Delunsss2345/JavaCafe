package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import ConnectDB.DatabaseConnection;
import dao.HoaDonCaPheDAO;
import entities.HoaDon;

public class frmQuanLyHoaDon extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;
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
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Khách Hàng", "Tổng Tiền"});

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
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

        try {
            hoaDonDAO = new HoaDonCaPheDAO(DatabaseConnection.getInstance().getConnection());
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

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
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu: " + e.getMessage());
        }
    }

    // Giả sử phương thức này sẽ lấy tên khách hàng dựa vào MaKH
    private String getTenKhachHang(int maKH) throws SQLException {
        // Thực hiện truy vấn lấy tên khách hàng theo MaKH
        // Ví dụ, bạn có thể gọi một phương thức trong DAO để lấy tên khách hàng từ bảng KhachHang
        return hoaDonDAO.getTenKhachHangByMaKH(maKH);  // Cập nhật phương thức trong DAO nếu cần
    }
}
