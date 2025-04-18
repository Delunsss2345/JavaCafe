package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.sql.*;
import java.util.List;

import ConnectDB.DatabaseConnection;
import dao.HoaDonCaPheDAO;
import entities.HoaDon;

public class frmQuanLyHoaDon extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;
    private static final long serialVersionUID = 1L;
    private Connection conn;

    public frmQuanLyHoaDon() {
        setLayout(new BorderLayout());

        // Tạo model cho bảng, thêm cột MaSP
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Khách Hàng", "Tổng Tiền"});

        // Tạo bảng và gắn vào ScrollPane
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
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

        // Kết nối cơ sở dữ liệu và khởi tạo DAO
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            hoaDonDAO = new HoaDonCaPheDAO(conn);
            taiLaiDanhSachHoaDon();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

 
    public void taiLaiDanhSachHoaDon() {
        List<HoaDon> danhSach = hoaDonDAO.getAllHoaDon();
		tableModel.setRowCount(0); // Xóa dữ liệu cũ
		for (HoaDon hoaDon : danhSach) {
		    Object[] row = new Object[] {
		        hoaDon.getMaHD(),
		        hoaDon.getNgayTao(),
		     
		        hoaDon.getTongTien()
		    };
		    tableModel.addRow(row);
		}
    }

    // Tải lại dữ liệu (xử lý sự kiện khi nhấn nút "Tải lại")
    private void loadData() {
        taiLaiDanhSachHoaDon();
    }
}
