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
    private int currentMaLoai; // Track the current category ID
    

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
                    currentMaLoai = (int) modelLoaiSanPham.getValueAt(selectedRow, 0);
                    loadSanPhamTheoLoai(currentMaLoai);
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
        List<SanPham> sanPhamList = sanPhamDAO.getSanPhamByLoai(maLoai) ;
        modelSanPham.setRowCount(0);  

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
            modelSanPham.addRow(rowData); 
        }

        tableSanPham.getColumnModel().getColumn(4).setCellRenderer(new ImageRenderer());
        tableSanPham.setRowHeight(60);
    }

 // Phương thức tạo và cấu hình form sản phẩm chung (rất bá đạo và chăm chút kaka)
    private JPanel createProductForm(SanPham sanPham, JTextField txtTenSanPham, JTextField txtGia,
                                   JComboBox<String> cboTrangThai, JTextArea txtMoTa,
                                   JTextField txtMaLoai, JTextField txtTenLoai,
                                   String[] hinhAnhPath, JLabel lblHinhAnh, JLabel lblPreviewImage,
                                   boolean isEdit) {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        txtMoTa.setLineWrap(true);
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        
        txtTenLoai.setEditable(false);
        txtMaLoai.setEditable(!isEdit);
        
        lblPreviewImage.setPreferredSize(new Dimension(150, 150));
        lblPreviewImage.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreviewImage.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton btnChonAnh = new JButton(isEdit ? "Thay đổi hình ảnh" : "Chọn ảnh");
        
        // Hiển thị hình ảnh hiện tại nếu có
        if (isEdit && sanPham.getHinhAnh() != null && !sanPham.getHinhAnh().isEmpty()) {
            displayImage(sanPham.getHinhAnh(), lblPreviewImage);
        }
        
        btnChonAnh.addActionListener(e -> selectImage(formPanel, hinhAnhPath, lblHinhAnh, lblPreviewImage));
        
        // Thêm các components vào form
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(txtTenSanPham, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Giá:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(txtGia, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Trạng thái:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(cboTrangThai, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Mô tả sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(scrollMoTa, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Mã loại sản phẩm:"), gbc);
        
        gbc.gridx = 1;
        if (isEdit) {
            JPanel loaiPanel = new JPanel(new BorderLayout());
            loaiPanel.add(txtMaLoai, BorderLayout.CENTER);
            JButton btnTimLoai = new JButton("Tìm");
            loaiPanel.add(btnTimLoai, BorderLayout.EAST);
            formPanel.add(loaiPanel, gbc);
            
            btnTimLoai.addActionListener(e -> searchProductCategory(formPanel, txtMaLoai, txtTenLoai));
        } else {
            formPanel.add(txtMaLoai, gbc);
        }
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Tên loại:"), gbc);
        
        gbc.gridx = 1;
        formPanel.add(txtTenLoai, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(btnChonAnh, gbc);
        
        gbc.gridx = 1;
        formPanel.add(lblHinhAnh, gbc);
        
        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(lblPreviewImage, gbc);
        
        return formPanel;
    }

    // Phương thức chọn hình ảnh
    private void selectImage(Component parent, String[] hinhAnhPath, JLabel lblHinhAnh, JLabel lblPreviewImage) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Hình ảnh", "jpg", "jpeg", "png", "gif"));
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            hinhAnhPath[0] = selectedFile.getAbsolutePath();
            lblHinhAnh.setText(selectedFile.getName());
            displayImage(hinhAnhPath[0], lblPreviewImage);
        }
    }

    // Phương thức hiển thị hình ảnh
    private void displayImage(String imagePath, JLabel imageLabel) {
        try {
            ImageIcon imageIcon = new ImageIcon(imagePath);
            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception ex) {
            imageLabel.setIcon(null);
            imageLabel.setText("Không thể tải hình ảnh");
        }
    }

    // Phương thức tìm kiếm loại sản phẩm
    private void searchProductCategory(Component parent, JTextField txtMaLoai, JTextField txtTenLoai) {
        try {
            int maLoai = Integer.parseInt(txtMaLoai.getText().trim());
            try {
                LoaiSanPham loaiSP = loaiSanPhamDAO.getLoaiSanPhamById(maLoai);
                if (loaiSP != null) {
                    txtTenLoai.setText(loaiSP.getTenLoai());
                } else {
                    txtTenLoai.setText("Không tìm thấy");
                    JOptionPane.showMessageDialog(parent, "Không tìm thấy loại sản phẩm với mã " + maLoai, 
                            "Thông báo", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
            	ex.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Mã loại không hợp lệ", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Lỗi: " + ex.getMessage(), 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức xác thực dữ liệu form
    private SanPham validateAndCreateProduct(Component parent, int maSanPham, String[] hinhAnhPath,
                                           JTextField txtTenSanPham, JTextField txtGia, JComboBox<String> cboTrangThai,
                                           JTextArea txtMoTa, JTextField txtMaLoai, LocalDateTime ngayTao) throws Exception {
        String tenSanPham = txtTenSanPham.getText().trim();
        if (tenSanPham.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Tên sản phẩm không được để trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        BigDecimal gia;
        try {
            gia = new BigDecimal(txtGia.getText().trim().replace(",", ""));
            if (gia.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(parent, "Giá không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Giá không hợp lệ", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        String trangThai = (String) cboTrangThai.getSelectedItem();
        String moTa = txtMoTa.getText().trim();
        int maLoai = Integer.parseInt(txtMaLoai.getText().trim());
        String hinhAnh = hinhAnhPath[0];
        
        if (hinhAnh.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Vui lòng chọn hình ảnh cho sản phẩm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        // Lấy đầy đủ thông tin loại sản phẩm
        try  {
            LoaiSanPham loaiSanPham = loaiSanPhamDAO.getLoaiSanPhamById(maLoai);
            if (loaiSanPham == null) {
                JOptionPane.showMessageDialog(parent, "Mã loại sản phẩm không tồn tại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            LocalDateTime ngayCapNhat = LocalDateTime.now();
            return new SanPham(maSanPham, tenSanPham, loaiSanPham, gia, moTa, trangThai, hinhAnh, 
                              ngayTao == null ? ngayCapNhat : ngayTao, ngayCapNhat);
        }
        catch (Exception ex) {
        	ex.printStackTrace();
        }
		return null;
    }

    // Phương thức để mở dialog thêm sản phẩm
    private void openAddProductDialog() {
        JDialog addProductDialog = new JDialog((Frame) null, "Thêm Sản Phẩm", true);
        addProductDialog.setLayout(new BorderLayout());
        
        // Khởi tạo các component
        JTextField txtTenSanPham = new JTextField(20);
        JTextField txtGia = new JTextField(20);
        JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang Bán", "Hết Hàng"});
        JTextArea txtMoTa = new JTextArea(3, 20);
        
        // Hiển thị loại sản phẩm đã chọn
        LoaiSanPham loaiDaChon = null;
        try{
            loaiDaChon = loaiSanPhamDAO.getLoaiSanPhamById(currentMaLoai);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        JTextField txtMaLoai = new JTextField(String.valueOf(currentMaLoai));
        JTextField txtTenLoai = new JTextField(loaiDaChon != null ? loaiDaChon.getTenLoai() : "");
        JLabel lblHinhAnh = new JLabel("Chưa chọn ảnh");
        lblHinhAnh.setPreferredSize(new Dimension(200, 30));
        
        final String[] hinhAnhPath = {""};
        final JLabel lblPreviewImage = new JLabel();
        
        // Tạo form
        JPanel formPanel = createProductForm(null, txtTenSanPham, txtGia, cboTrangThai, txtMoTa, 
                                           txtMaLoai, txtTenLoai, hinhAnhPath, lblHinhAnh, 
                                           lblPreviewImage, false);
        
        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        
        btnCancel.addActionListener(e -> addProductDialog.dispose());
        
        btnSave.addActionListener(e -> {
            try {
                SanPham sanPham = validateAndCreateProduct(addProductDialog, 0, hinhAnhPath, 
                                                        txtTenSanPham, txtGia, cboTrangThai, txtMoTa, 
                                                        txtMaLoai, null);
                if (sanPham != null) {
                    try  {
                        sanPhamDAO.addSanPham(sanPham);
                        loadSanPhamTheoLoai(currentMaLoai);
                        JOptionPane.showMessageDialog(addProductDialog, "Thêm sản phẩm thành công!", 
                                                   "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        addProductDialog.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(addProductDialog, "Lỗi: " + ex.getMessage(), 
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
 }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addProductDialog, "Lỗi: " + ex.getMessage(), 
                                           "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        addProductDialog.add(formPanel, BorderLayout.CENTER);
        addProductDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        addProductDialog.pack();
        addProductDialog.setSize(450, 550);
        addProductDialog.setLocationRelativeTo(null);
        addProductDialog.setVisible(true);
    }

    // Phương thức để mở dialog sửa sản phẩm
    private void openEditProductDialog() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maSanPham = (int) modelSanPham.getValueAt(selectedRow, 0);
        
        try {
            SanPham sanPham = sanPhamDAO.getSanPhamById(maSanPham);
            
            if (sanPham == null) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog editProductDialog = new JDialog((Frame) null, "Sửa Sản Phẩm", true);
            editProductDialog.setLayout(new BorderLayout());
            
            // Khởi tạo các component với giá trị hiện tại
            JTextField txtTenSanPham = new JTextField(sanPham.getTenSanPham(), 20);
            JTextField txtGia = new JTextField(sanPham.getGia().toString(), 20);
            JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang Bán", "Hết Hàng"});
            cboTrangThai.setSelectedItem(sanPham.getTrangThai());
            JTextArea txtMoTa = new JTextArea(sanPham.getMoTa(), 3, 20);
            
            JTextField txtMaLoai = new JTextField(String.valueOf(sanPham.getLoaiSanPham().getMaLoai()));
            JTextField txtTenLoai = new JTextField(sanPham.getLoaiSanPham().getTenLoai());
            
            JLabel lblHinhAnh = new JLabel(sanPham.getHinhAnh() != null ? 
                                       new File(sanPham.getHinhAnh()).getName() : "Không có hình ảnh");
            lblHinhAnh.setPreferredSize(new Dimension(200, 30));
            
            final String[] hinhAnhPath = {sanPham.getHinhAnh()};
            final JLabel lblPreviewImage = new JLabel();
            
            // Tạo form
            JPanel formPanel = createProductForm(sanPham, txtTenSanPham, txtGia, cboTrangThai, txtMoTa, 
                                               txtMaLoai, txtTenLoai, hinhAnhPath, lblHinhAnh, 
                                               lblPreviewImage, true);
            
            // Panel chứa các nút
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSave = new JButton("Lưu");
            JButton btnCancel = new JButton("Hủy");
            
            btnCancel.addActionListener(e -> editProductDialog.dispose());
            
            btnSave.addActionListener(e -> {
                try {
                    SanPham sanPhamCapNhat = validateAndCreateProduct(editProductDialog, maSanPham, hinhAnhPath, 
                                                                 txtTenSanPham, txtGia, cboTrangThai, txtMoTa, 
                                                                 txtMaLoai, sanPham.getNgayTao());
                    if (sanPhamCapNhat != null) {
                        try {
                            sanPhamDAO.updateSanPham(sanPhamCapNhat);
                            loadSanPhamTheoLoai(currentMaLoai);
                            JOptionPane.showMessageDialog(editProductDialog, "Cập nhật sản phẩm thành công!", 
                                                       "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            editProductDialog.dispose();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(editProductDialog, "Lỗi: " + ex.getMessage(), 
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                        	}
                        
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(editProductDialog, "Lỗi: " + ex.getMessage(), 
                                               "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });
            
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
            
            editProductDialog.add(formPanel, BorderLayout.CENTER);
            editProductDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            editProductDialog.pack();
            editProductDialog.setSize(450, 550);
            editProductDialog.setLocationRelativeTo(null);
            editProductDialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi lấy thông tin sản phẩm: " + ex.getMessage(), 
                                       "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức xóa sản phẩm
    private void openDeleteProductDialog() {
        int selectedRow = tableSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maSanPham = (int) modelSanPham.getValueAt(selectedRow, 0);

        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?", 
                                               "Xóa sản phẩm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
               boolean success = sanPhamDAO.deleteSanPham(maSanPham);
                
                if (success) {
                    loadSanPhamTheoLoai(currentMaLoai);
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!", 
                                               "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm.", 
                                               "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm: " + ex.getMessage(), 
                                           "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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