//package com.smoxisys.mgui;
//
//import com.smoxisys.service.TemperatureDataService;
//import com.smoxisys.service.impl.TemperatureDataServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class TemperatureDisplayRunner implements CommandLineRunner {
//
//    @Autowired
//    private TemperatureDataServiceImpl temperatureService;
//
//    private TemperaturePlotter temperaturePlotter;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 初始化温度显示窗口
//        temperaturePlotter = new TemperaturePlotter();
//        temperaturePlotter.setVisible(true);
//
//        // 启动温度采集服务
//        temperatureService.recordTemperature();
//
//        // 更新温度到图表
//        new Thread(() -> {
//            while (true) {
//                Double latestTemperature = temperatureService.generateTemperature(); // 获取最新的温度
//                temperaturePlotter.updateTemperature(latestTemperature); // 更新到图表
//                try {
//                    Thread.sleep(1000); // 每秒更新一次
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//}
