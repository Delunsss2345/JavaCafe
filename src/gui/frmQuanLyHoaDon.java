package gui;
// Nguoi Lam Nguyen Tuan Phat
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.awt.event.ActionEvent;

import ConnectDB.DatabaseConnection;
import dao.ChiTietHoaDonCaPheDAO;
import dao.HoaDonCaPheDAO;
import dao.KhachHangDAO;
import dao.NhanVienDAO;
import entities.ChiTietHoaDonCaPhe;
import entities.HoaDon;
import entities.KhachHang;
import entities.NhanVien;

public class frmQuanLyHoaDon extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private HoaDonCaPheDAO hoaDonDAO;
    private ChiTietHoaDonCaPheDAO chiTietHoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private NhanVienDAO nhanVienDAO;
    private static final long serialVersionUID = 1L;

    public frmQuanLyHoaDon() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Mã HĐ", "Ngày Lập", "Giờ lập", "Mã NV", "Tên NV", "Mã KH", "Tên KH", "Tổng Tiền", "Tiền khách trả", "Tiền thối"});

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 220, 240));
        header.setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel pnlControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlControls.setBackground(Color.WHITE);

        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlSearch.setBackground(Color.WHITE);
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        JTextField txtSearch = new JTextField(15);
        JComboBox<String> cboSearchType = new JComboBox<>(new String[]{"Mã HĐ", "Mã KH", "Mã NV", "Ngày lập"});
        JButton btnSearch = new JButton("Tìm");
        btnSearch.setBackground(new Color(66, 139, 202));
        btnSearch.setForeground(Color.WHITE);
        
        pnlSearch.add(lblSearch);
        pnlSearch.add(txtSearch);
        pnlSearch.add(cboSearchType);
        pnlSearch.add(btnSearch);
        
        add(pnlSearch, BorderLayout.NORTH);

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
        
        JButton btnIn = new JButton("In hóa đơn");
        btnIn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnIn.setBackground(new Color(220, 53, 69));
        btnIn.setForeground(Color.WHITE);
        btnIn.setFocusPainted(false);
        btnIn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnIn.addActionListener(e -> inHoaDon());

        pnlControls.add(btnXemChiTiet);
        pnlControls.add(btnIn);
        pnlControls.add(btnReload);
        add(pnlControls, BorderLayout.SOUTH);

        try {
            hoaDonDAO = new HoaDonCaPheDAO();
            chiTietHoaDonDAO = new ChiTietHoaDonCaPheDAO();
            khachHangDAO = new KhachHangDAO();
            nhanVienDAO = new NhanVienDAO();
            
            taiLaiDanhSachHoaDon();
            
            btnSearch.addActionListener(e -> timKiemHoaDon(txtSearch.getText(), cboSearchType.getSelectedItem().toString()));
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khởi tạo: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void timKiemHoaDon(String keyword, String searchType) {
        if (keyword.trim().isEmpty()) {
            taiLaiDanhSachHoaDon();
            return;
        }
        
        try {
            List<HoaDon> danhSach;
            
            switch (searchType) {
                case "Mã HĐ":
                    try {
                        int maHD = Integer.parseInt(keyword);
                        HoaDon hoaDon = hoaDonDAO.getHoaDonByMaHD(maHD);
                        danhSach = hoaDon != null ? List.of(hoaDon) : List.of();
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Mã hóa đơn phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case "Mã KH":
                    try {
                        int maKH = Integer.parseInt(keyword);
                        danhSach = hoaDonDAO.getHoaDonByMaKH(maKH);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Mã khách hàng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                case "Mã NV":
                    try {
                        int maNV = Integer.parseInt(keyword);
                        danhSach = hoaDonDAO.getHoaDonByMaNV(maNV);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "Mã nhân viên phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }	
                    break;
                case "Ngày lập":
                    danhSach = hoaDonDAO.getHoaDonByDate(keyword);
                    break;
                default:
                    danhSach = hoaDonDAO.getAllHoaDon();
            }
            
            hienThiDanhSachHoaDon(danhSach);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void hienThiDanhSachHoaDon(List<HoaDon> danhSach) {
        tableModel.setRowCount(0);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        
        for (HoaDon hoaDon : danhSach) {
            String tenKH = "";
            String tenNV = "";
            
            try {
                KhachHang kh = khachHangDAO.getKhachHangById(hoaDon.getMaKH());
                if (kh != null) {
                	tenKH = kh.getHo() + " " + kh.getTen();
                }
                
                NhanVien nv = nhanVienDAO.getNhanVienByMa(hoaDon.getMaNV());
                if (nv != null) {
                    tenNV = nv.getHo() + " " + nv.getTen();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Object[] row = new Object[] {
                hoaDon.getMaHD(),
                hoaDon.getNgayTao().format(dateFormatter),
                hoaDon.getNgayTao().format(timeFormatter),
                hoaDon.getMaNV(),
                tenNV,
                hoaDon.getMaKH(),
                tenKH,
                String.format("%,.0f", hoaDon.getTongTien()),
                String.format("%,.0f", hoaDon.getTienKhachTra()),
                String.format("%,.0f", hoaDon.getTienThua())
            };
            tableModel.addRow(row);
        }
    }

    public void taiLaiDanhSachHoaDon() {
        try {
            List<HoaDon> danhSach = hoaDonDAO.getAllHoaDon();
            hienThiDanhSachHoaDon(danhSach);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            taiLaiDanhSachHoaDon();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải lại dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void xemChiTietHoaDon() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để xem chi tiết!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int maHD = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
            
            HoaDon hoaDon = hoaDonDAO.getHoaDonByMaHD(maHD);
            
            if (hoaDon != null) {
                List<ChiTietHoaDonCaPhe> chiTietList = chiTietHoaDonDAO.getChiTietByMaHoaDon(maHD);
                
                JDialog dlgChiTiet = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn #" + maHD, true);
                dlgChiTiet.setLayout(new BorderLayout());
                dlgChiTiet.setSize(700, 500);
                dlgChiTiet.setLocationRelativeTo(null);
                
                JPanel pnlThongTin = new JPanel();
                pnlThongTin.setLayout(new BoxLayout(pnlThongTin, BoxLayout.Y_AXIS));
                pnlThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin hóa đơn"));
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                
                String tenKH = "";
                String tenNV = "";
                
                try {
                    KhachHang kh = khachHangDAO.getKhachHangById(hoaDon.getMaKH());
                    if (kh != null) {
                    	tenKH = kh.getHo() + " " + kh.getTen();

                    }
                    
                    NhanVien nv = nhanVienDAO.getNhanVienByMa(hoaDon.getMaNV());
                    if (nv != null) {
                        tenNV = nv.getHo() + " " + nv.getTen();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                JPanel pnlGrid = new JPanel(new GridLayout(7, 2, 5, 5));
                pnlGrid.add(new JLabel("Mã hóa đơn:"));
                pnlGrid.add(new JLabel(String.valueOf(hoaDon.getMaHD())));
                
                pnlGrid.add(new JLabel("Ngày tạo:"));
                pnlGrid.add(new JLabel(hoaDon.getNgayTao().format(formatter)));
                
                pnlGrid.add(new JLabel("Nhân viên:"));
                pnlGrid.add(new JLabel(tenNV + " (" + hoaDon.getMaNV() + ")"));
                
                pnlGrid.add(new JLabel("Khách hàng:"));
                pnlGrid.add(new JLabel(tenKH + " (" + hoaDon.getMaKH() + ")"));
                
                pnlGrid.add(new JLabel("Tổng tiền:"));
                pnlGrid.add(new JLabel(String.format("%,.0f VNĐ", hoaDon.getTongTien())));
                
                pnlGrid.add(new JLabel("Tiền khách trả:"));
                pnlGrid.add(new JLabel(String.format("%,.0f VNĐ", hoaDon.getTienKhachTra())));
                
                pnlGrid.add(new JLabel("Tiền thối lại:"));
                pnlGrid.add(new JLabel(String.format("%,.0f VNĐ", hoaDon.getTienThua())));
                
                pnlThongTin.add(pnlGrid);
                
                dlgChiTiet.add(pnlThongTin, BorderLayout.NORTH);
                
                DefaultTableModel modelChiTiet = new DefaultTableModel();
                modelChiTiet.setColumnIdentifiers(new String[]{"STT", "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"});
                
                JTable tblChiTiet = new JTable(modelChiTiet);
                tblChiTiet.setRowHeight(25);
                
                int stt = 1;
                for (ChiTietHoaDonCaPhe ct : chiTietList) {
                    modelChiTiet.addRow(new Object[] {
                        stt++,
                        ct.getMaSanPham(),
                        ct.getTenSanPham(),
                        String.format("%,.0f VNĐ", ct.getDonGia()),
                        ct.getSoLuong(),
                        String.format("%,.0f VNĐ", ct.getThanhTien())
                    });
                }
                
                JScrollPane scrollChiTiet = new JScrollPane(tblChiTiet);
                scrollChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết các món"));
                dlgChiTiet.add(scrollChiTiet, BorderLayout.CENTER);
                
                JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                JButton btnDong = new JButton("Đóng");
                btnDong.addActionListener(e -> dlgChiTiet.dispose());
                
                JButton btnInChiTiet = new JButton("In hóa đơn");
                btnInChiTiet.addActionListener(e -> {
                    inHoaDonChiTiet(maHD);
                    dlgChiTiet.dispose();
                });
                
                pnlButton.add(btnInChiTiet);
                pnlButton.add(btnDong);
                dlgChiTiet.add(pnlButton, BorderLayout.SOUTH);
                
                dlgChiTiet.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi truy xuất chi tiết hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void inHoaDon() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn để in!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int maHD = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        inHoaDonChiTiet(maHD);
    }
    
    private void inHoaDonChiTiet(int maHD) {
        try {
            HoaDon hoaDon = hoaDonDAO.getHoaDonByMaHD(maHD);
            if (hoaDon == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<ChiTietHoaDonCaPhe> danhSachMon = chiTietHoaDonDAO.getChiTietByMaHoaDon(maHD);
            
            KhachHang kh = khachHangDAO.getKhachHangById(hoaDon.getMaKH());
            NhanVien nv = nhanVienDAO.getNhanVienByMa(hoaDon.getMaNV());
            
            String tenKH = kh != null ? (kh.getHo() + " " + kh.getTen()) : "";
            String tenNV = nv != null ? (nv.getHo() + " " + nv.getTen()) : "";
            
            frmChiTietHoaDon formIn = new frmChiTietHoaDon(
                String.valueOf(hoaDon.getMaNV()),
                tenNV,
                String.valueOf(hoaDon.getMaKH()),
                tenKH,
                hoaDon.getTongTien(),
                hoaDon.getNgayTao(),
                hoaDon.getNgayTao(),
                danhSachMon,
                maHD,
                hoaDon.getTienKhachTra()
            );
            
            formIn.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void capNhatSauKhiLuuHoaDon() {
        loadData();
    }
}
