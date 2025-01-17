package com.smoxisys.mgui;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smoxisys.domain.PatientData;
import com.smoxisys.domain.TemperatureData;
import com.smoxisys.service.impl.PatientDataServiceImpl;
import com.smoxisys.service.impl.TemperatureDataServiceImpl;
import org.springframework.scheduling.annotation.Scheduled;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TemperaturePlotter extends JPanel {
    class SingleTemperaturePlotter extends JPanel {

        private static final long serialVersionUID = 1L;
        private static final int MAX_DATA_POINTS = 100; // 最多显示 100 个数据点
        private static final int TIMER_DELAY = 1000; // 定时器间隔（毫秒）

        // 画温度曲线用的
        private List<Double> temperatureDoubleList = new ArrayList<>();
        private TemperatureDataServiceImpl temperatureDataService;

        // 病人下拉框
        private JComboBox patientComboBox;
        private PatientDataServiceImpl patientDataService;
        private PatientData patientData;

        public SingleTemperaturePlotter(
                PatientDataServiceImpl patientDataService,
                TemperatureDataServiceImpl temperatureDataService
        ) {
            this.patientDataService = patientDataService;
            this.temperatureDataService = temperatureDataService;

            // 面板的调整
            setPreferredSize(new Dimension(800, 400)); // 增大默认窗口尺寸
            setLayout(new BorderLayout());
            patientComboBox = new JComboBox();
            add(patientComboBox, BorderLayout.NORTH);

            // 设置渲染，自定义显示
            patientComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                if (value instanceof PatientData) {
                    JLabel label = new JLabel("ID: " + ((PatientData) value).getId() + ", 姓名: " + ((PatientData) value).getName());
                    return label;
                }
                return null;
            });
            // 默认设置为第一
            if (patientComboBox.getItemCount() > 0) {
                patientComboBox.setSelectedIndex(0);
                patientData = (PatientData) patientComboBox.getSelectedItem();
            }
            // 监听函数
            patientComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    patientData = (PatientData) patientComboBox.getSelectedItem();
                }
            });
            // 下拉插入
            SingleTemperaturePlotterUpdate();

            // 定时器，每隔 TIMER_DELAY 毫秒生成随机温度并更新曲线
            Timer timer = new Timer(TIMER_DELAY, e -> {
                if (patientData != null) {
                    updateTemperature(temperatureDataService.getTemperatureList(patientData.getId()));
                }
            });
            timer.start();
        }

        public void SingleTemperaturePlotterUpdate() {
            patientComboBox.removeAllItems();
            List<PatientData> patientDataList = patientDataService.list();
            for (PatientData patientData : patientDataList) {
                patientComboBox.addItem(patientData);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // 绘制背景
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // 设置中文字体避免乱码
            Font titleFont = new Font("Microsoft YaHei", Font.BOLD, 20); // 标题字体
            Font labelFont = new Font("Microsoft YaHei", Font.PLAIN, 14); // 标尺字体

            // 绘制标题
            g2d.setColor(Color.BLACK);
            g2d.setFont(titleFont);
//        g2d.drawString("实时温度曲线", getWidth() / 2 - 50, 30);

            // 绘制 Y 轴标尺和刻度
            int maxTemperature = 50; // 最大温度
            int minTemperature = 37; // 最小温度
            int rulerSpacing = getHeight() / (maxTemperature - minTemperature); // 根据窗口高度动态调整标尺间距

            g2d.setFont(labelFont);
            for (int i = 0; i <= (maxTemperature - minTemperature); i++) {
                int y = getHeight() - (i * rulerSpacing);

                // 绘制水平辅助线
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawLine(50, y, getWidth() - 10, y);

                // 绘制刻度值
                g2d.setColor(Color.BLACK);
                g2d.drawString((minTemperature + i) + "°C", 10, y + 5);
            }

            // 绘制 X 轴和 Y 轴
            g2d.setColor(Color.BLACK);
            g2d.drawLine(50, 0, 50, getHeight()); // Y 轴
            g2d.drawLine(50, getHeight() - 1, getWidth() - 10, getHeight() - 1); // X 轴

            // 绘制温度曲线
            g2d.setColor(Color.RED);
            if (temperatureDoubleList.size() > 1) {
                int prevX = 50;
                int prevY = getHeight() - (int) ((temperatureDoubleList.get(0) - minTemperature) * rulerSpacing);
                for (int i = 1; i < temperatureDoubleList.size(); ++i) {
                    int x = 50 + i * (getWidth() - 60) / MAX_DATA_POINTS;
                    int y = getHeight() - (int) ((temperatureDoubleList.get(i) - minTemperature) * rulerSpacing);
                    g2d.drawLine(prevX, prevY, x, y);
                    prevX = x;
                    prevY = y;
                }
            }
        }

        // 更新温度数据
        public void updateTemperature(List<Double> temperatureDoubleList) {
            this.temperatureDoubleList = temperatureDoubleList;
            repaint();
        }
    }
//----------------------------------------------------------------------
    private List<SingleTemperaturePlotter> temperaturePlotterList = new ArrayList<SingleTemperaturePlotter>();
    private final int numberOfTemperaturePlotters = 3;  // 可以自行设定

    private PatientDataServiceImpl patientDataService;
    private TemperatureDataServiceImpl temperatureDataService;

    //当有新数据插入时更新下拉框
    public void TemperaturePlotterUpdate() {
        for (SingleTemperaturePlotter temperaturePlotter : temperaturePlotterList) {
            temperaturePlotter.SingleTemperaturePlotterUpdate();
        }
    }

    public void DeletePatient(int patientId) {
        temperatureDataService.remove(new QueryWrapper<TemperatureData>().eq("patient_id", patientId));
    }

    public TemperaturePlotter(
            PatientDataServiceImpl patientDataService,
            TemperatureDataServiceImpl temperatureDataService) {
        this.patientDataService = patientDataService;
        this.temperatureDataService = temperatureDataService;
        for (int i = 0; i < numberOfTemperaturePlotters; i++) {
            temperaturePlotterList.add(new SingleTemperaturePlotter(patientDataService, temperatureDataService));
        }
        setLayout(new GridLayout(numberOfTemperaturePlotters, 1, 10, 10));
        for (SingleTemperaturePlotter temperaturePlotter : temperaturePlotterList) {
            add(temperaturePlotter);
        }

//        temperatureDataService.recordTemperature();

    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("温度曲线实时显示");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        TemperaturePlotter plotter = new TemperaturePlotter();
//        frame.add(plotter);
//        frame.pack();
//        frame.setVisible(true);
//    }
}
