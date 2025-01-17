package com.smoxisys.mgui;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smoxisys.domain.Users;
import com.smoxisys.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

// 使用 @Component 标注类，Spring 会自动扫描并管理该类的实例
@Component
public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private Image backgroundImage;
    // 这里用不用Autowired没什么区别吧，你这个Autowired应该写在LoginPanel上兄弟。
    @Autowired
    private UsersServiceImpl usersService;
    private MainWindow mainWindow;

    public LoginPanel(UsersServiceImpl usersService, MainWindow mainWindow) {
        this.usersService = usersService;
        this.mainWindow = mainWindow;

        // 设置登录界面背景
        backgroundImage = new ImageIcon("D:\\Program\\Java\\SmartMoxibustionSystem1\\src\\pic\\background.png").getImage();

        // 使用 GridBagLayout 来优化布局
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);  // 设置组件之间的间距

        // 设置用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;  // 标签对齐方式为右对齐
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;  // 输入框填充整个单元格
        add(usernameField, gbc);

        // 设置密码标签和输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        // 设置登录按钮
        loginButton = new JButton("登录");
        loginButton.addActionListener(e -> login());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // 设置按钮跨越两列
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(loginButton, gbc);

        // 设置注册按钮
        registerButton = new JButton("注册");
        registerButton.addActionListener(e -> openRegisterDialog());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(registerButton, gbc);

        // 设置面板边距
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 设置面板边距
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // 验证输入是否正确合法
    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名不能为空！");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "密码不能为空！");
            return false;
        }
        if (username.length() > 50 || password.length() > 50) {
            JOptionPane.showMessageDialog(this, "用户名或密码长度不能超过50个字符！");
            return false;
        }
        return true;
    }

    // 登录判断逻辑
    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // 验证输入
        if (!validateInput(username, password)) {
            return;
        }

        // 调用服务层的登录方法
        Users user = usersService.login(username, password);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "登录成功!");
            mainWindow.loadMainInterface();
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误!");
        }
    }

    private void openRegisterDialog() {
        // 打开注册对话框
        RegisterDialog registerDialog = new RegisterDialog(usersService);
        registerDialog.setVisible(true);
    }
}
