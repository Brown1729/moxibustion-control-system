package com.smoxisys.mgui;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smoxisys.domain.PatientData;
import com.smoxisys.service.PatientDataService;
import com.smoxisys.service.impl.PatientDataServiceImpl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel {
    private PatientDataServiceImpl patientDataService;
    class SingleControlPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private JButton riseButton, descendButton; // 上升和下降按钮
        private JComboBox patientComboBox;
        private int yPosition; // 艾灸工具的垂直位置
        private PatientDataService patientDataService;  // 调整哪个病人的艾灸工具
        private PatientData patientData;
        private final int topYPosition = 50;

        JLabel depthLabel;  // 显示地下那个深度

        private ControlPanel controlPanel;

        public SingleControlPanel(PatientDataServiceImpl patientDataService, ControlPanel controlPanel) {
            // 确定病人
            this.patientDataService = patientDataService;

            // 设置初始位置
            yPosition = topYPosition; // 初始位置（可以调整）

            // 设置回到函数全局类
            this.controlPanel = controlPanel;

            // 设置面板布局
            setLayout(new BorderLayout());

            // 创建下拉条选择病人，默认是0号病人
            patientComboBox = new JComboBox();
            List<PatientData> patientDataList = patientDataService.list();
            for (PatientData patientData : patientDataList) {
                patientComboBox.addItem(patientData);
            }

            add(patientComboBox, BorderLayout.NORTH);

            // 显示条渲染，自定义显示内容
            patientComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                if (value instanceof PatientData) {
                    JLabel label = new JLabel("ID: " + ((PatientData) value).getId() + ", 姓名: " + ((PatientData) value).getName());
                    return label;
                }
                return null;
            });

            if (patientComboBox.getItemCount() > 0) {
                patientComboBox.setSelectedIndex(0);
                patientData = (PatientData) patientComboBox.getSelectedItem();
            }

            controlPanel.controlPanelAllRepaint();

            // 添加Combox的事件监听器
            patientComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    patientData = (PatientData) patientComboBox.getSelectedItem();
                    if (patientData != null) {
                        patientData.setDepth(patientDataService.getOne(new QueryWrapper<PatientData>().eq("id", patientData.getId())).getDepth());
//                        System.out.println("选中的对象：" + patientData.getId() + "，" + patientData.getName());
                        repaint();
                    }
                }
            });

//            // 创建Combox的Label
//            JLabel patientLabel = new JLabel("病人");
//            patientLabel.setFont(new Font("Microsoft YaHei", Font.BOLD, 15)); // 设置字体
//            patientLabel.setForeground(Color.BLACK);
//            add(patientLabel, BorderLayout.NORTH);

            // 控制按钮面板
            JPanel controlButtonPanel = new JPanel();
            controlButtonPanel.setLayout(new BorderLayout());

            riseButton = new JButton("上升");
            descendButton = new JButton("下降");
            depthLabel = new JLabel("艾灸工具深度：0");

            controlButtonPanel.add(depthLabel, BorderLayout.NORTH);
            controlButtonPanel.add(riseButton, BorderLayout.WEST);
            controlButtonPanel.add(descendButton, BorderLayout.EAST);

            // 添加按钮面板
            add(controlButtonPanel, BorderLayout.SOUTH);

            // 设置按钮点击事件
            riseButton.addActionListener(e -> {
                // 上升按钮按下时，艾灸工具上升
                if (yPosition > 50 && !patientDataList.isEmpty()) { // 限制最小位置

                    patientData.setDepth(patientData.getDepth() - 1);
                    patientDataService.updateById(patientData);
                }
                controlPanel.controlPanelAllRepaint(); // 重绘面板
            });

            descendButton.addActionListener(e -> {
                // 下降按钮按下时，艾灸工具下降
                if (yPosition < getHeight() - 50 && !patientDataList.isEmpty()) { // 限制最大位置
                    patientData.setDepth(patientData.getDepth() + 1);
                    patientDataService.updateById(patientData);
                }
                controlPanel.controlPanelAllRepaint(); // 重绘面板
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // 更新坐标
            if (patientData != null) {
                yPosition = topYPosition + patientData.getDepth() * 10;
                depthLabel.setText("艾灸工具深度：" + patientData.getDepth());
            }

            // 绘制背景
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // 设置画笔颜色
            g.setColor(Color.RED);

            // 绘制直线来模拟艾灸工具
            g.drawLine(getWidth() / 2, 50, getWidth() / 2, yPosition);

            // 绘制艾灸工具的头部（可以是一个圆形）
            g.fillOval(getWidth() / 2 - 5, yPosition - 10, 10, 10);
        }

        public void controlPanelDataUpdate() {
            patientComboBox.removeAllItems();
            List<PatientData> patientDataList = patientDataService.list();
            for (PatientData patientData : patientDataList) {
                patientComboBox.addItem(patientData);
            }

            repaint();
        }
    }
//------------------------------------------------------------------------------------------
    private List<SingleControlPanel> controlPanelList = new ArrayList<>();
    private final int numberOfControlPanels = 4;  // 艾灸工具的个数

    //当有新数据插入时更新下拉框
    public void controlPanelDataUpdate() {
        for (SingleControlPanel controlPanel : controlPanelList) {
            controlPanel.controlPanelDataUpdate();
        }
    }

    // 全局更新绘画面板以保持一致
    public void controlPanelAllRepaint()
    {
        PatientData patientData;
        for (SingleControlPanel controlPanel : controlPanelList) {
            patientData = (PatientData) controlPanel.patientComboBox.getSelectedItem();
            if (patientData != null) {
                patientData.setDepth(patientDataService.getOne(new QueryWrapper<PatientData>().eq("id", patientData.getId())).getDepth());
            }
            controlPanel.repaint();
        }
    }

    public ControlPanel(PatientDataServiceImpl patientDataService) {
        this.patientDataService = patientDataService;
        for (int i = 0; i < numberOfControlPanels; ++i) {
            controlPanelList.add(new SingleControlPanel(patientDataService, this));
        }
        setLayout(new GridLayout(1, numberOfControlPanels, 10, 10));
        for (SingleControlPanel controlPanel : controlPanelList) {
            add(controlPanel);
        }

    }

//    public static void main(String[] args) {
//        // 创建控制面板的 JFrame
//        JFrame frame = new JFrame("艾灸工具控制面板");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 500);
//        frame.add(new ControlPanel());
//        frame.setVisible(true);
//    }
}