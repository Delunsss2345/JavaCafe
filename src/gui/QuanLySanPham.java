package gui;
//Người làm Phạm Thanh Huy
import dao.LoaiSanPhamDAO;
import dao.SanPhamDAO;
import entities.LoaiSanPham;
import entities.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class QuanLySanPham extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable tableLoaiSanPham, tableSanPham;
    private DefaultTableModel modelLoaiSanPham, modelSanPham;
    private LoaiSanPhamDAO loaiSanPhamDAO;
    private SanPhamDAO sanPhamDAO;
    private DecimalFormat dinhDangTien;
    private JPanel panelButton;  
    private JButton btnThem, btnSua, btnXoa;  

    public QuanLySanPham() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dinhDangTien = new DecimalFormat("#,### VNĐ");

        try {
            loaiSanPhamDAO = new LoaiSanPhamDAO();
            sanPhamDAO = new SanPhamDAO();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        createCategoryPanel();
        createProductPanel();
        loadLoaiSanPhamData();
    }

    private void createCategoryPanel() {
        JPanel panelCategory = new JPanel(new BorderLayout());
        panelCategory.setBorder(BorderFactory.createTitledBorder("Danh mục loại sản phẩm"));

        String[] columnNames = {"Mã loại", "Tên loại", "Mô tả", "Icon"};
        modelLoaiSanPham = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableLoaiSanPham = new JTable(modelLoaiSanPham);
        tableLoaiSanPham.setRowHeight(25);
        tableLoaiSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableLoaiSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableLoaiSanPham.getSelectedRow();
                if (selectedRow >= 0) {
                    int maLoai = (int) modelLoaiSanPham.getValueAt(selectedRow, 0);
                    loadSanPhamTheoLoai(maLoai);
                    showActionButtons(true);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tableLoaiSanPham);
        panelCategory.add(scrollPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadLoaiSanPhamData());
        panelCategory.add(btnRefresh, BorderLayout.SOUTH);

        add(panelCategory, BorderLayout.WEST);
    }

    private void createProductPanel() {
        JPanel panelProduct = new JPanel(new BorderLayout());
        panelProduct.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));

        String[] columnNames = {"Mã SP", "Tên SP", "Giá", "Trạng thái", "Hình ảnh"};
        modelSanPham = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableSanPham = new JTable(modelSanPham);
        tableSanPham.setRowHeight(25);
        tableSanPham.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tableSanPham);
        panelProduct.add(scrollPane, BorderLayout.CENTER);

        JLabel lblCurrentCategory = new JLabel("Chọn một loại sản phẩm để xem chi tiết");
        lblCurrentCategory.setHorizontalAlignment(SwingConstants.CENTER);
        panelProduct.add(lblCurrentCategory, BorderLayout.NORTH);

        JPanel panelActions = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnThem = new JButton("Thêm Sản Phẩm");
        btnThem.addActionListener(e -> openAddProductDialog());
        panelActions.add(btnThem);

        btnSua = new JButton("Sửa Sản Phẩm");
        btnSua.addActionListener(e -> openEditProductDialog());
        panelActions.add(btnSua);

        btnXoa = new JButton("Xóa Sản Phẩm");
        btnXoa.addActionListener(e -> openDeleteProductDialog());
        panelActions.add(btnXoa);

        panelProduct.add(panelActions, BorderLayout.SOUTH);

        add(panelProduct, BorderLayout.CENTER);
    }

    private void showActionButtons(boolean show) {
        btnThem.setEnabled(show);
        btnSua.setEnabled(show);
        btnXoa.setEnabled(show);
    }

    private void loadLoaiSanPhamData() {
        try {
            List<LoaiSanPham> loaiSanPhamList = loaiSanPhamDAO.getAllLoaiSanPham();
            modelLoaiSanPham.setRowCount(0);

            for (LoaiSanPham loaiSanPham : loaiSanPhamList) {
                Object[] rowData = {
                    loaiSanPham.getMaLoai(),
                    loaiSanPham.getTenLoai(),
                    loaiSanPham.getMoTa(),
                    loaiSanPham.getIcon()
                };
                modelLoaiSanPham.addRow(rowData);
            }

            modelSanPham.setRowCount(0);
            showActionButtons(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh mục sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void loadSanPhamTheoLoai(int maLoai) {
        List<SanPham> sanPhamList = sanPhamDAO.getSanPhamByLoai(maLoai);
        modelSanPham.setRowCount(0);  // Reset bảng

        for (SanPham sanPham : sanPhamList) {
            ImageIcon imageIcon = new ImageIcon(sanPham.getHinhAnh());
            Image image = imageIcon.getImage();
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(scaledImage);

            Object[] rowData = {
                sanPham.getMaSanPham(),
                sanPham.getTenSanPham(),
                dinhDangTien.format(sanPham.getGia()),
                sanPham.getTrangThai(),
                scaledImageIcon
            };
            modelSanPham.addRow(rowData);  // Thêm dòng mới vào bảng
        }

        tableSanPham.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        tableSanPham.setRowHeight(60);
    }

    private void openAddProductDialog() {
        JDialog addProductDialog = new JDialog((Frame) null, "Thêm Sản Phẩm", true);
        addProductDialog.setLayout(new GridLayout(7, 2));  // Tăng số hàng lên 7 để thêm chọn ảnh

        JTextField txtTenSanPham = new JTextField();
        JTextField txtGia = new JTextField();
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang Bán", "Hết Hàng"});
        JTextField txtMoTa = new JTextField();
        JTextField txtMaLoai = new JTextField();

        JLabel lblHinhAnh = new JLabel("Chưa chọn ảnh");
        JButton btnChonAnh = new JButton("Chọn ảnh");

        final String[] hinhAnhPath = {""};  // Biến dùng để lưu tên hoặc đường dẫn ảnh được chọn

        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(addProductDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                hinhAnhPath[0] = selectedFile.getName();  // Hoặc dùng getAbsolutePath() nếu cần đường dẫn đầy đủ
                lblHinhAnh.setText(hinhAnhPath[0]);
            }
        });

        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                String tenSanPham = txtTenSanPham.getText();
                BigDecimal gia = new BigDecimal(txtGia.getText());
                String trangThai = (String) cboTrangThai.getSelectedItem();
                String moTa = txtMoTa.getText();
                int maLoai = Integer.parseInt(txtMaLoai.getText());
                String hinhAnh = hinhAnhPath[0];

                LocalDateTime now = LocalDateTime.now();

                SanPham sp = new SanPham(0, tenSanPham, maLoai, gia, moTa, trangThai, hinhAnh, now, now);
                sanPhamDAO.addSanPham(sp);

                loadSanPhamTheoLoai(maLoai);
                addProductDialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addProductDialog, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Thêm các trường nhập liệu vào form
        addProductDialog.add(new JLabel("Tên sản phẩm:"));
        addProductDialog.add(txtTenSanPham);
        addProductDialog.add(new JLabel("Giá:"));
        addProductDialog.add(txtGia);
        addProductDialog.add(new JLabel("Trạng thái:"));
        addProductDialog.add(cboTrangThai);
        addProductDialog.add(new JLabel("Mô tả sản phẩm:"));
        addProductDialog.add(txtMoTa);
        addProductDialog.add(new JLabel("Mã loại sản phẩm:"));
        addProductDialog.add(txtMaLoai);
        addProductDialog.add(btnChonAnh);
        addProductDialog.add(lblHinhAnh);
        addProductDialog.add(btnSave);

        addProductDialog.setSize(450, 350);
        addProductDialog.setLocationRelativeTo(null);
        addProductDialog.setVisible(true);
    }

    private void openEditProductDialog() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy giá trị từ bảng và kiểm tra
        int maSanPham = (int) modelSanPham.getValueAt(selectedRow, 0);
        String tenSanPham = (String) modelSanPham.getValueAt(selectedRow, 1);
        String gia = (String) modelSanPham.getValueAt(selectedRow, 2);
        String trangThai = (String) modelSanPham.getValueAt(selectedRow, 3);
        String maLoai = (String) modelSanPham.getValueAt(selectedRow, 4);

        // Kiểm tra trường hợp giá trị null
        if (tenSanPham == null || gia == null || trangThai == null || maLoai == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog editProductDialog = new JDialog((Frame) null, "Sửa Sản Phẩm", true);
        editProductDialog.setLayout(new GridLayout(7, 2));

        JTextField txtTenSanPham = new JTextField(tenSanPham);
        JTextField txtGia = new JTextField(gia);
        JTextField txtMaLoai = new JTextField(maLoai); // Điền sẵn mã loại vào ô nhập
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang bán", "Hết bán"});
        cboTrangThai.setSelectedItem(trangThai);

        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            String tenMoi = txtTenSanPham.getText().trim();
            String giaStr = txtGia.getText().replaceAll("[^\\d.]", ""); // Xoá ký tự không hợp lệ

            BigDecimal giaMoi;
            try {
                giaMoi = new BigDecimal(giaStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editProductDialog, "Giá không hợp lệ. Vui lòng nhập số hợp lệ (ví dụ: 100000 hoặc 100000.00)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String trangThaiMoi = (String) cboTrangThai.getSelectedItem();
            LocalDateTime ngayCapNhat = LocalDate.now().atStartOfDay();

            // Kiểm tra mã loại hợp lệ trước khi lưu
            int maLoaiMoi;
            try {
                maLoaiMoi = Integer.parseInt(txtMaLoai.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editProductDialog, "Mã loại không hợp lệ. Vui lòng nhập một số nguyên hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Cập nhật sản phẩm
            sanPhamDAO.updateSanPham(new SanPham(maSanPham, tenMoi, maLoaiMoi, giaMoi, null, trangThaiMoi, null, ngayCapNhat));

            // Tải lại sản phẩm theo loại
            loadSanPhamTheoLoai(maSanPham);
            editProductDialog.dispose();
        });

        // Thêm các thành phần vào cửa sổ chỉnh sửa
        editProductDialog.add(new JLabel("Tên sản phẩm:"));
        editProductDialog.add(txtTenSanPham);
        editProductDialog.add(new JLabel("Giá:"));
        editProductDialog.add(txtGia);
        editProductDialog.add(new JLabel("Trạng thái:"));
        editProductDialog.add(cboTrangThai);
        editProductDialog.add(new JLabel("Mã loại:"));
        editProductDialog.add(txtMaLoai); // Hiển thị ô nhập mã loại
        editProductDialog.add(new JLabel()); // Dòng trống
        editProductDialog.add(btnSave);

        // Thiết lập kích thước và hiển thị
        editProductDialog.setSize(400, 300);
        editProductDialog.setLocationRelativeTo(null);
        editProductDialog.setVisible(true);
    }

    private void openDeleteProductDialog() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maSanPham = (int) modelSanPham.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            sanPhamDAO.deleteSanPham(maSanPham);
            loadSanPhamTheoLoai(maSanPham);
        }
    }

    private class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public void setValue(Object value) {
            if (value instanceof ImageIcon) {
                setIcon((ImageIcon) value);
                setText("");
            } else {
                super.setValue(value);
            }
        }
    }
}
