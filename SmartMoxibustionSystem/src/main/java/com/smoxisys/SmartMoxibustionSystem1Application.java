package com.smoxisys;

import com.smoxisys.mgui.MainWindow;
import com.smoxisys.service.impl.PatientDataServiceImpl;
import com.smoxisys.service.impl.TemperatureDataServiceImpl;
import com.smoxisys.service.impl.UsersServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.swing.*;

@SpringBootApplication
@EnableScheduling
// MyBatis-Plus：在SpringBoot中添加注释，扫描mapper文件夹
@MapperScan("com.smoxisys.mapper")
public class SmartMoxibustionSystem1Application implements CommandLineRunner {

    // Autowired：注入
    @Autowired
    private PatientDataServiceImpl patientDataService;
    @Autowired
    private UsersServiceImpl usersService;
    @Autowired
    private TemperatureDataServiceImpl temperatureDataService;

    public static void main(String[] args) {
        SpringApplication.run(SmartMoxibustionSystem1Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动 GUI 界面
        // 将任务提交到 EDT（事件调度线程）中，当 EDT 空闲时执行任务
        SwingUtilities.invokeLater(() -> {
            // 创建主窗口并启动
            MainWindow mainWindow = new MainWindow(patientDataService, usersService, temperatureDataService);
            mainWindow.setVisible(true);  // 在 run() 方法中显示窗口
        });
    }
}
