package gui;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import entities.*;
import dao.*;

public class InvoiceManagement extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableDonHang;
    private DefaultTableModel modelDonHang;
    private ArrayList<DonHang> danhSachDonHang;
    private DonHangDAO donHangDAO;
    
    // Các thành phần giao diện
    private JButton btnThemDonHang;
    private JButton btnXemChiTiet;
    private JButton btnXuatExcel;
    private JButton btnTimKiem;
    private JTextField txtTimKiem;
    private JButton btnCapNhatTrangThai;

    public InvoiceManagement() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Khởi tạo DAO
        donHangDAO = new DonHangDAO();
        
        // Khởi tạo danh sách đơn hàng
        danhSachDonHang = new ArrayList<>();

        // Tạo giao diện
        taoPanelChucNang();
        taoBangDonHang();

        // Tải dữ liệu từ CSDL
        taiDuLieuDonHang();
    }

    private void taoPanelChucNang() {
        JPanel panelChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelChucNang.setBorder(BorderFactory.createTitledBorder("Chức năng"));

        // Nút thêm đơn hàng
        btnThemDonHang = new JButton("Thêm đơn hàng");
        btnThemDonHang.setBackground(new Color(50, 150, 50));
        btnThemDonHang.setForeground(Color.WHITE);
        btnThemDonHang.addActionListener(e -> themDonHangMoi());

        // Nút xem chi tiết
        btnXemChiTiet = new JButton("Xem chi tiết");
        btnXemChiTiet.setBackground(new Color(70, 130, 180));
        btnXemChiTiet.setForeground(Color.WHITE);
        btnXemChiTiet.addActionListener(e -> xemChiTietDonHang());

        // Nút cập nhật trạng thái
        btnCapNhatTrangThai = new JButton("Cập nhật trạng thái");
        btnCapNhatTrangThai.setBackground(new Color(218, 165, 32));
        btnCapNhatTrangThai.setForeground(Color.WHITE);
        btnCapNhatTrangThai.addActionListener(e -> capNhatTrangThai());

        // Nút xuất Excel
        btnXuatExcel = new JButton("Xuất Excel");
        btnXuatExcel.setBackground(new Color(34, 139, 34));
        btnXuatExcel.setForeground(Color.WHITE);
        btnXuatExcel.addActionListener(e -> xuatDonHangExcel());

        // Ô tìm kiếm
        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBackground(new Color(100, 100, 100));
        btnTimKiem.setForeground(Color.WHITE);
        btnTimKiem.addActionListener(e -> timKiemDonHang());

        panelChucNang.add(btnThemDonHang);
        panelChucNang.add(btnXemChiTiet);
        panelChucNang.add(btnCapNhatTrangThai);
        panelChucNang.add(btnXuatExcel);
        panelChucNang.add(new JLabel("Tìm kiếm:"));
        panelChucNang.add(txtTimKiem);
        panelChucNang.add(btnTimKiem);

        add(panelChucNang, BorderLayout.NORTH);
    }

    private void taoBangDonHang() {
        String[] cot = {"Mã ĐH", "Ngày tạo", "Khách hàng", "Nhân viên", "Tổng tiền", "Trạng thái"};
        modelDonHang = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                if (columnIndex == 4) return Double.class;
                return String.class;
            }
        };

        tableDonHang = new JTable(modelDonHang);
        tableDonHang.setRowHeight(30);
        tableDonHang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Sắp xếp theo cột
        tableDonHang.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tableDonHang);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách đơn hàng"));

        add(scrollPane, BorderLayout.CENTER);
    }

    private void taiDuLieuDonHang() {
        try {
            // Xóa dữ liệu cũ
            modelDonHang.setRowCount(0);
            danhSachDonHang.clear();

            // Lấy dữ liệu từ DAO
            danhSachDonHang = donHangDAO.getAllDonHang();

            // Thêm vào bảng
            for (DonHang dh : danhSachDonHang) {
                modelDonHang.addRow(new Object[]{
                    dh.getMaDonHang(),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dh.getNgayTao()),
                    dh.getKhachHang().getTen(),
                    dh.getNhanVien().getTen(),
                    dh.getTongTien(),
                    dh.getTrangThai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu đơn hàng: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void themDonHangMoi() {
        // Mở form thêm đơn hàng mới
        ThemDonHangDialog dialog = new ThemDonHangDialog((Frame) SwingUtilities.getWindowAncestor(this));
        dialog.setVisible(true);
        
        // Sau khi đóng dialog, cập nhật lại dữ liệu
        if (dialog.isSuccess()) {
            taiDuLieuDonHang();
        }
    }

    private void xemChiTietDonHang() {
        int selectedRow = tableDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần xem", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maDH = (int) modelDonHang.getValueAt(selectedRow, 0);
        DonHang donHang = layDonHangTheoMa(maDH);

        if (donHang != null) {
            // Hiển thị chi tiết đơn hàng
            XemChiTietDonHangDialog dialog = new XemChiTietDonHangDialog(
                (Frame) SwingUtilities.getWindowAncestor(this), donHang);
            dialog.setVisible(true);
        }
    }

    private void capNhatTrangThai() {
        int selectedRow = tableDonHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn hàng cần cập nhật", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maDH = (int) modelDonHang.getValueAt(selectedRow, 0);
        String trangThaiHienTai = (String) modelDonHang.getValueAt(selectedRow, 5);
        
        // Tạo dialog cập nhật trạng thái
        CapNhatTrangThaiDialog dialog = new CapNhatTrangThaiDialog(
            (Frame) SwingUtilities.getWindowAncestor(this), maDH, trangThaiHienTai);
        dialog.setVisible(true);
        
        // Sau khi cập nhật, làm mới dữ liệu
        if (dialog.isDaCapNhat()) {
            taiDuLieuDonHang();
        }
    }

    private DonHang layDonHangTheoMa(int maDH) {
        for (DonHang dh : danhSachDonHang) {
            if (dh.getMaDonHang() == maDH) {
                return dh;
            }
        }
        return null;
    }

    private void xuatDonHangExcel() {
        try {
            // Xuất danh sách đơn hàng ra Excel
            XuatExcelUtil.xuatDonHang(danhSachDonHang);
            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void timKiemDonHang() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            taiDuLieuDonHang();
            return;
        }

        try {
            modelDonHang.setRowCount(0);
            danhSachDonHang = donHangDAO.timKiemDonHang(tuKhoa);

            for (DonHang dh : danhSachDonHang) {
                modelDonHang.addRow(new Object[]{
                    dh.getMaDonHang(),
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dh.getNgayTao()),
                    dh.getKhachHang().getTen(),
                    dh.getNhanVien().getTen(),
                    dh.getTongTien(),
                    dh.getTrangThai()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm đơn hàng: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}