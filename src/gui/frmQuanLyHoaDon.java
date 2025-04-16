//Nguyễn Tuấn Phát
package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import ConnectDB.DatabaseConnection;
import dao.HoaDonCaPheDAO;
import entities.HoaDon;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class frmQuanLyHoaDon extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;

    public frmQuanLyHoaDon() {
        setTitle("Quản Lý Hóa Đơn");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Khách Hàng", "Tổng Tiền"});

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

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
            List<HoaDon> dsHoaDon = hoaDonDAO.getAllHoaDon();
            tableModel.setRowCount(0);
            for (HoaDon hd : dsHoaDon) {
                tableModel.addRow(new Object[]{
                    hd.getMaHoaDon(),
                    hd.getNgayLap(),
                    hd.getTenKhachHang(),
                    hd.getTongTien()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi load dữ liệu: " + e.getMessage());
        }
    }
}
