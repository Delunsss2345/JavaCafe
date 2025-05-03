package gui;
// Người làm Phạm Thanh Huy

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import entities.ChiTietHoaDonCaPhe;
import entities.SanPham;
import entities.TaiKhoan;

import dao.SanPhamDAO;

public class ManHinhTrangChu extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel panelSanPham, panelGioHang;
    private DefaultTableModel modelGioHang;
    private JTable tableGioHang;
    private SanPhamDAO sanPhamDAO;
    private List<SanPham> danhSachSanPham;
    private TaiKhoan taiKhoan; // Thêm biến lưu tài khoản

    /**
     * Thay đổi constructor để nhận TaiKhoan
     */
    public ManHinhTrangChu(TaiKhoan taiKhoan) {
        this.taiKhoan = taiKhoan;
        setLayout(new BorderLayout());
        sanPhamDAO = new SanPhamDAO();

        taoPanelSanPham();
        taoPanelGioHang();
        taiDanhSachSanPham();
    }

    /**
     * Giữ lại constructor không tham số cho phát triển, nếu cần
     */
    public ManHinhTrangChu() {
        this(null);
    }

    public void taiDanhSachSanPham()  {
        danhSachSanPham = sanPhamDAO.getAllSanPham();
        hienThiSanPham();
    }

    private void taoPanelSanPham() {
        panelSanPham = new JPanel();
        panelSanPham.setLayout(new GridLayout(0, 4, 15, 15));
        panelSanPham.setBackground(new Color(245, 245, 220));
        panelSanPham.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(panelSanPham);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void hienThiSanPham() {
        panelSanPham.removeAll();
        for (SanPham sanPham : danhSachSanPham) {
            if (!"Đang bán".equalsIgnoreCase(sanPham.getTrangThai())) continue;

            JPanel panelItem = new JPanel(new BorderLayout());
            panelItem.setBackground(Color.WHITE);
            panelItem.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            panelItem.setPreferredSize(new Dimension(200, 250));

            JLabel lblHinhAnh = new JLabel();
            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
                File file = new File(sanPham.getHinhAnh());
                ImageIcon icon = new ImageIcon(file.exists()? sanPham.getHinhAnh(): "src/images/image-notfound.png");
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(img));
            } else {
                ImageIcon icon = new ImageIcon("src/images/image-notfound.png");
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(img));
            }
            lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblHinhAnh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel panelThongTin = new JPanel(new BorderLayout());
            panelThongTin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
            panelThongTin.setBackground(Color.WHITE);

            JLabel lblTen = new JLabel(sanPham.getTenSanPham(), SwingConstants.CENTER);
            lblTen.setFont(new Font("Arial", Font.BOLD, 14));
            JLabel lblGia = new JLabel(String.format("%,.0f VNĐ", sanPham.getGia()), SwingConstants.CENTER);
            lblGia.setFont(new Font("Arial", Font.PLAIN, 13));
            lblGia.setForeground(new Color(200, 0, 0));

            panelThongTin.add(lblTen, BorderLayout.NORTH);
            panelThongTin.add(lblGia, BorderLayout.SOUTH);

            JButton btnThem = new JButton("Thêm vào giỏ");
            btnThem.setBackground(new Color(50, 150, 50));
            btnThem.setForeground(Color.WHITE);
            btnThem.setFocusPainted(false);
            btnThem.addActionListener(e -> themVaoGioHang(sanPham));

            panelItem.add(lblHinhAnh, BorderLayout.CENTER);
            panelItem.add(panelThongTin, BorderLayout.NORTH);
            panelItem.add(btnThem, BorderLayout.SOUTH);

            panelSanPham.add(panelItem);
        }
        panelSanPham.revalidate();
        panelSanPham.repaint();
    }

    private void taoPanelGioHang() {
        panelGioHang = new JPanel(new BorderLayout());
        panelGioHang.setPreferredSize(new Dimension(350, 0));
        panelGioHang.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelGioHang.setBackground(new Color(255, 248, 220));

        JLabel lblTieuDe = new JLabel("GIỎ HÀNG", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 16));
        lblTieuDe.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelGioHang.add(lblTieuDe, BorderLayout.NORTH);

        String[] cot = {"Sản phẩm", "Mã SP", "Đơn giá", "Số lượng", "Thành tiền"};
        modelGioHang = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        tableGioHang = new JTable(modelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JTextField()));
        // Ẩn cột mã SP
        tableGioHang.getColumnModel().getColumn(1).setMinWidth(0);
        tableGioHang.getColumnModel().getColumn(1).setMaxWidth(0);
        JScrollPane scrollPane = new JScrollPane(tableGioHang);
        panelGioHang.add(scrollPane, BorderLayout.CENTER);

        JPanel panelNut = new JPanel(new GridLayout(1, 2, 10, 0));
        panelNut.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        JButton btnXoa = new JButton("Xóa");
        btnXoa.setBackground(new Color(200, 50, 50));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.addActionListener(e -> xoaSanPhamKhoiGio());
        JButton btnThanhToan = new JButton("Thanh toán");
        btnThanhToan.setBackground(new Color(50, 100, 200));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.addActionListener(e -> thanhToan());
        panelNut.add(btnXoa);
        panelNut.add(btnThanhToan);
        panelGioHang.add(panelNut, BorderLayout.SOUTH);

        add(panelGioHang, BorderLayout.EAST);
    }

    private void themVaoGioHang(SanPham sanPham) {
        boolean daTonTai = false;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            if (modelGioHang.getValueAt(i, 0).equals(sanPham.getTenSanPham())) {
                int soLuong = (int) modelGioHang.getValueAt(i, 3) + 1;
                modelGioHang.setValueAt(soLuong, i, 3);
                modelGioHang.setValueAt(String.format("%,.0f VNĐ", sanPham.getGia().doubleValue() * soLuong), i,
                        4);
                daTonTai = true;
                break;
            }
        }
        if (!daTonTai) {
            Object[] row = { sanPham.getTenSanPham(), sanPham.getMaSanPham(),
                    String.format("%,.0f VNĐ", sanPham.getGia()), 1,
                    String.format("%,.0f VNĐ", sanPham.getGia()) };
            modelGioHang.addRow(row);
        }
    }

    private void xoaSanPhamKhoiGio() {
        int selectedRow = tableGioHang.getSelectedRow();
        if (selectedRow >= 0) {
            modelGioHang.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void thanhToan() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<ChiTietHoaDonCaPhe> gioHang = new ArrayList<>();
        double tongTien = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            String tenSP = modelGioHang.getValueAt(i, 0).toString();
            int maSP = (int) modelGioHang.getValueAt(i, 1);
            String donGiaStr = modelGioHang.getValueAt(i, 2).toString().replaceAll("[^\\d.]", "");
            String soLuongStr = modelGioHang.getValueAt(i, 3).toString();
            double donGia = Double.parseDouble(donGiaStr);
            int soLuong = Integer.parseInt(soLuongStr);
            double thanhTien = donGia * soLuong;
            tongTien += thanhTien;
            ChiTietHoaDonCaPhe ct = new ChiTietHoaDonCaPhe();
            ct.setTenSanPham(tenSP);
            ct.setMaSanPham(maSP);
            ct.setDonGia(donGia);
            ct.setSoLuong(soLuong);
            ct.setThanhTien(thanhTien);
            gioHang.add(ct);
        }

        frmLapHoaDon frm = new frmLapHoaDon(gioHang, taiKhoan);
        frm.setVisible(true);
    }
}
