package gui;
//Người làm Phạm Thanh Huy
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import entities.ChiTietHoaDonCaPhe;
import entities.SanPham;

import dao.SanPhamDAO;

public class ManHinhTrangChu extends JPanel {
    private static final long serialVersionUID = 1L;
    private JPanel panelSanPham, panelGioHang;
    private DefaultTableModel modelGioHang;
    private JTable tableGioHang;
    private SanPhamDAO sanPhamDAO;
    private List<SanPham> danhSachSanPham;

    public ManHinhTrangChu() {
        setLayout(new BorderLayout());
        sanPhamDAO = new SanPhamDAO();

        taoPanelSanPham();
        taoPanelGioHang();
        taiDanhSachSanPham();
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
        System.out.println("Bắt đầu hiển thị sản phẩm..."); // Debug
        System.out.println("Tổng số sản phẩm: " + danhSachSanPham.size()); // Debug
        
        for (SanPham sanPham : danhSachSanPham) {
        	System.out.println("Xử lý sản phẩm: " + sanPham.getTenSanPham()); // Debug
            if (!"Đang bán".equalsIgnoreCase(sanPham.getTrangThai())) {
                continue; 
            }

            JPanel panelItem = new JPanel();
            panelItem.setLayout(new BorderLayout());
            panelItem.setBackground(Color.WHITE);
            panelItem.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
            panelItem.setPreferredSize(new Dimension(200, 250));

          
            JLabel lblHinhAnh = new JLabel();
            if (sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
            	File file = new File(sanPham.getHinhAnh());
            	 if (file.exists()) {
            	        ImageIcon icon = new ImageIcon(sanPham.getHinhAnh());
            	        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            	        lblHinhAnh.setIcon(new ImageIcon(img));
            	    } else {
            	        ImageIcon icon = new ImageIcon("src\\images\\image-notfound.png");
            	        Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            	        lblHinhAnh.setIcon(new ImageIcon(img));
            	    }
            } else {
                ImageIcon icon = new ImageIcon("src\\images\\image-notfound.png");
                Image img = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblHinhAnh.setIcon(new ImageIcon(img));
            }
            lblHinhAnh.setHorizontalAlignment(SwingConstants.CENTER);
            lblHinhAnh.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

           
            JPanel panelThongTin = new JPanel(new BorderLayout());
            panelThongTin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));
            
            JLabel lblTen = new JLabel(sanPham.getTenSanPham(), SwingConstants.CENTER);
            lblTen.setFont(new Font("Arial", Font.BOLD, 14));
            
            JLabel lblGia = new JLabel(String.format("%,.0f VNĐ", sanPham.getGia()), SwingConstants.CENTER);
            lblGia.setFont(new Font("Arial", Font.PLAIN, 13));
            lblGia.setForeground(new Color(200, 0, 0));
            
            panelThongTin.add(lblTen, BorderLayout.NORTH);
            panelThongTin.add(lblGia, BorderLayout.SOUTH);
            panelThongTin.setBackground(new Color(255, 255, 255));
           
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

        String[] cot = {"Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        modelGioHang = new DefaultTableModel(cot, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };
        
        tableGioHang = new JTable(modelGioHang);
        tableGioHang.setRowHeight(30);
        tableGioHang.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JTextField()));
        
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
                int soLuong = (int) modelGioHang.getValueAt(i, 2) + 1;
                modelGioHang.setValueAt(soLuong, i, 2);
                modelGioHang.setValueAt(String.format("%,.0f VNĐ", sanPham.getGia().doubleValue() * soLuong), i, 3);
                daTonTai = true;
                break;
            }
        }
        
        if (!daTonTai) {
            Object[] row = {
                sanPham.getTenSanPham(),
                String.format("%,.0f VNĐ", sanPham.getGia()),
                1,
                String.format("%,.0f VNĐ", sanPham.getGia())
            };
            modelGioHang.addRow(row);
        }
    }

    private void xoaSanPhamKhoiGio() {
        int selectedRow = tableGioHang.getSelectedRow();
        if (selectedRow >= 0) {
            modelGioHang.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    void thanhToan() {
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tạo danh sách chi tiết hóa đơn từ giỏ hàng
        List<ChiTietHoaDonCaPhe> gioHang = new ArrayList<>();
        double tongTien = 0;

        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            String tenSP = modelGioHang.getValueAt(i, 0).toString();
            String donGiaStr = modelGioHang.getValueAt(i, 1).toString().replaceAll("[^\\d.]", "");
            String soLuongStr = modelGioHang.getValueAt(i, 2).toString();

            double donGia = Double.parseDouble(donGiaStr);
            int soLuong = Integer.parseInt(soLuongStr);
            double thanhTien = donGia * soLuong;
            tongTien += thanhTien;

            ChiTietHoaDonCaPhe ct = new ChiTietHoaDonCaPhe();
            ct.setDonGia(donGia);
            ct.setSoLuong(soLuong);
            ct.setThanhTien(thanhTien);

            gioHang.add(ct);
        }

        frmLapHoaDon frm = new frmLapHoaDon(gioHang, tongTien);
        frm.setVisible(true);
    }
}