package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
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

        // Tạo model cho bảng với các cột phù hợp
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Giờ lập", "Mã NV", "Mã KH", "Tổng Tiền", "Tiền khách trả", "Tiền thối"});

        // Tạo bảng và thiết lập giao diện
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel chứa các nút chức năng
        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlControls.setBackground(Color.WHITE);

        JButton btnReload = new JButton("Tải lại");
        btnReload.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnReload.setBackground(new Color(33, 115, 70));
        btnReload.setForeground(Color.WHITE);
        btnReload.setFocusPainted(false);
        btnReload.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReload.addActionListener(e -> loadData());

        JButton btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnXemChiTiet.setBackground(new Color(30, 100, 180));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.setFocusPainted(false);
        btnXemChiTiet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnXemChiTiet.addActionListener(e -> xemChiTietHoaDon());

        pnlControls.add(btnXemChiTiet);
        pnlControls.add(btnReload);
        add(pnlControls, BorderLayout.SOUTH);

        // Kết nối CSDL và khởi tạo DAO
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            hoaDonDAO = new HoaDonCaPheDAO(conn);
            taiLaiDanhSachHoaDon();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Tải danh sách hóa đơn từ CSDL
    public void taiLaiDanhSachHoaDon() {
        List<HoaDon> danhSach = hoaDonDAO.getAllHoaDon();
        tableModel.setRowCount(0); // Xóa dữ liệu cũ
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (HoaDon hoaDon : danhSach) {
            Object[] row = new Object[] {
                hoaDon.getMaHD(),
                hoaDon.getNgayTao().format(dateFormatter),
                hoaDon.getNgayTao().format(timeFormatter),
                hoaDon.getMaNV(),
                hoaDon.getMaKH(),
                String.format("%,.0f", hoaDon.getTongTien()),
                String.format("%,.0f", hoaDon.getTienKhachTra()),
                String.format("%,.0f", hoaDon.getTienThua())
            };
            tableModel.addRow(row);
        }
    }

    // Tải lại dữ liệu (xử lý sự kiện khi nhấn nút "Tải lại")
    private void loadData() {
        taiLaiDanhSachHoaDon();
    }
    
    // Xem chi tiết hóa đơn đang chọn
    private void xemChiTietHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xem chi tiết!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int maHD = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        
        try {
            // Lấy thông tin hóa đơn từ CSDL
            HoaDon hoaDon = hoaDonDAO.getHoaDonByMaHD(maHD);
            
            if (hoaDon != null) {
                // Thông báo chức năng đang phát triển - bạn có thể mở rộng để hiển thị chi tiết hóa đơn
                JOptionPane.showMessageDialog(this, 
                    "Chi tiết hóa đơn #" + maHD + "\n" +
                    "Tổng tiền: " + String.format("%,.0f VNĐ", hoaDon.getTongTien()) + "\n" +
                    "Ngày lập: " + hoaDon.getNgayTao() + "\n" +
                    "Mã KH: " + hoaDon.getMaKH() + "\n" +
                    "Mã NV: " + hoaDon.getMaNV(),
                    "Chi tiết hóa đơn", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy xuất chi tiết hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức này được gọi sau khi có hóa đơn mới được tạo
    public void capNhatSauKhiLuuHoaDon() {
        loadData();
    }
}
