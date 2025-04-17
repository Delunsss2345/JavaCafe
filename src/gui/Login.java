package gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Login extends JFrame {
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JLabel titleLabel;
    private JLabel logoLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JLabel forgotPasswordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox rememberMeCheckbox;
    private ImageIcon logoIcon;
    private ImageIcon userIcon;
    private ImageIcon lockIcon;

    public Login() {
        initComponents();
        setLocationRelativeTo(null); // Đặt cửa sổ ở giữa màn hình
    }

    private void initComponents() {
        // Thiết lập cửa sổ
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Đăng Nhập - Hệ Thống Quản Lý Quán Cafe");
        setSize(1200, 550);
        setResizable(false);
        
        // Khởi tạo các thành phần
        mainPanel = new JPanel(new GridLayout(1, 2));
        leftPanel = createLeftPanel();
        rightPanel = createRightPanel();
        
        // Thêm các panel vào panel chính
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        
        // Thêm panel chính vào frame
        getContentPane().add(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (logoIcon != null) {
                    Image img = logoIcon.getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(255, 102, 0));

        try {
            URL imageUrl = getClass().getResource("/images/background-cafe.png");

            if (imageUrl != null) {
                logoIcon = new ImageIcon(imageUrl);
            } else {
                System.out.println("Không tìm thấy ảnh!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thêm tiêu đề phía dưới
        titleLabel = new JLabel("COFFEE CAFE");
        titleLabel.setFont(new Font("Segoe Script", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);

        return panel;
    }


    
    private JPanel createRightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 50, 40, 50));
        
        // Tiêu đề đăng nhập
        JLabel loginTitle = new JLabel("ĐĂNG NHẬP");
        loginTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        loginTitle.setForeground(new Color(51, 51, 51));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Tiêu đề phụ
        JLabel subTitle = new JLabel("Vui lòng đăng nhập để tiếp tục");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subTitle.setForeground(new Color(102, 102, 102));
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Panel đệm
        JPanel spacer1 = new JPanel();
        spacer1.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        spacer1.setOpaque(false);
        
        // Panel đệm
        JPanel spacer2 = new JPanel();
        spacer2.setMaximumSize(new Dimension(Short.MAX_VALUE, 15));
        spacer2.setOpaque(false);
        
        // Tạo trường tên đăng nhập
        JPanel usernamePanel = createInputPanel("Tài khoản", "/resources/user-icon.png");
        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        usernamePanel.add(usernameField);
        
     
        JPanel passwordPanel = createInputPanel("Mật khẩu", "/resources/lock-icon.png");
        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        passwordPanel.add(passwordField);
        
      
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.X_AXIS));
        optionsPanel.setOpaque(false);
        optionsPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 30));
        
      
        
        loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(255, 102, 0));
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(250, 40));
        
       
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(230, 92, 0));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(new Color(255, 102, 0));
            }
        });
        
        // Thêm sự kiện cho nút đăng nhập
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                handleLogin(evt);
            }
        });
        
      
        panel.add(loginTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(subTitle);
        panel.add(spacer1);
        panel.add(usernamePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(passwordPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(optionsPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(loginButton);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createInputPanel(String labelText, String iconPath) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 70));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(51, 51, 51));
        
        JPanel inputContainer = new JPanel();
        inputContainer.setLayout(new BoxLayout(inputContainer, BoxLayout.X_AXIS));
        inputContainer.setOpaque(false);
        
    
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                icon = new ImageIcon(img);
                JLabel iconLabel = new JLabel(icon);
                iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                inputContainer.add(iconLabel);
            }
        } catch (Exception e) {
            
        }
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(inputContainer);
        
        return panel;
    }

    private void handleLogin(ActionEvent evt) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String validUsername = "admin";
        String validPassword = "123";

        if (username.equals(validUsername) && password.equals(validPassword)) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            HeThongQuanLyQuanCaPhe cafeSys = new HeThongQuanLyQuanCaPhe();
            cafeSys.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập hoặc mật khẩu không đúng.", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    public static void main(String[] args) {

        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}