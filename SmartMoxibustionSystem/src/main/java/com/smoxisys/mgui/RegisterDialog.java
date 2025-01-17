package com.smoxisys.mgui;

import com.smoxisys.domain.Users;
import com.smoxisys.service.UsersService;
import com.smoxisys.service.impl.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.List;

@Component
public class RegisterDialog extends JDialog {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JButton registerButton;
    @Autowired
    private UsersServiceImpl userService;

    public RegisterDialog(UsersServiceImpl userService) {
        this.userService = userService;
        setTitle("注册");
        setSize(300, 200);
        setLayout(new GridLayout(4, 2));

        add(new JLabel("用户名:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("邮箱:"));
        emailField = new JTextField();
        add(emailField);

        registerButton = new JButton("注册");
        registerButton.addActionListener(e -> register());
        add(registerButton);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        Users user = new Users();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setCreatedAt(new Date());
        if (userService.save(user)) {
            JOptionPane.showMessageDialog(this, "注册成功!");
            this.dispose(); // 关闭注册对话框
        } else {
            JOptionPane.showMessageDialog(this, "用户名已存在!");
        }
    }
}
