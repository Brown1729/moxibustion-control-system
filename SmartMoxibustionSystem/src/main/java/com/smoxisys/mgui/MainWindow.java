package com.smoxisys.mgui;

import com.smoxisys.service.PatientDataService;
import com.smoxisys.service.impl.PatientDataServiceImpl;
import com.smoxisys.service.impl.TemperatureDataServiceImpl;
import com.smoxisys.service.impl.UsersServiceImpl;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class MainWindow extends JFrame {
    private static final long serialVersionUID = 1L;

    private PatientDataServiceImpl patientDataService;
    private UsersServiceImpl usersService;
    private TemperatureDataServiceImpl temperatureDataService;

    private ControlPanel controlPanel;
    private TemperaturePlotter plotter;

    private JPanel mainPanel;
    private JTabbedPane choicePanel;
    private LoginPanel loginPanel;
    private Image backgroundImage;  // 考虑给JFrame加背景图，但是要把每个组件都设透明有点烦，所以就不弄了。

    public MainWindow(
            PatientDataServiceImpl patientDataService,
            UsersServiceImpl usersService,
            TemperatureDataServiceImpl temperatureDataService) {
        this.patientDataService = patientDataService;
        this.usersService = usersService;

        // 设置窗口基本属性
        setTitle("智能艾灸床控制系统");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建登录界面
        loginPanel = new LoginPanel(usersService, this);
        add(loginPanel, BorderLayout.CENTER);

        // 初始化主面板 (隐藏直到用户登录成功)
        mainPanel = new JPanel(new BorderLayout());
        choicePanel = new JTabbedPane();
        mainPanel.add(choicePanel, BorderLayout.CENTER);

        // 温度显示面板
        plotter = new TemperaturePlotter(patientDataService, temperatureDataService);

        // 控制面板
        controlPanel = new ControlPanel(patientDataService);

        // 病人管理面板
        PatientManagementPanel patientManagementPanel = new PatientManagementPanel(patientDataService,
                controlPanel, plotter);

        choicePanel.add(patientManagementPanel);
        choicePanel.addTab("病人管理", patientManagementPanel);


        choicePanel.add(plotter);
        choicePanel.addTab("温度显示", plotter);


        choicePanel.add(controlPanel);
        choicePanel.addTab("艾灸工具", controlPanel);

        // 创建退出登录按钮
        JButton logoutButton = new JButton("退出登录");
        logoutButton.addActionListener(e -> logout());  // 点击按钮调用 logout 方法
        JPanel logoutPanel = new JPanel();
        logoutPanel.add(logoutButton);
        mainPanel.add(logoutPanel, BorderLayout.SOUTH);  // 将退出登录按钮放到顶部

        // 不再在构造器中显示窗口
    }

    // 用户登录成功后加载主界面
    public void loadMainInterface() {
        remove(loginPanel);  // 移除登录界面
        add(mainPanel);      // 添加主面板
        validate();
        repaint();
    }

    // 用户退出登录后返回登录界面
    public void logout() {
        remove(mainPanel);   // 移除主面板
        add(loginPanel);     // 显示登录界面
        validate();
        repaint();
    }
}
