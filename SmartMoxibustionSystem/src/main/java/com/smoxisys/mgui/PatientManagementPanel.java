package com.smoxisys.mgui;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smoxisys.domain.PatientData;
import com.smoxisys.service.impl.PatientDataServiceImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.text.DateFormat;
import java.util.List;

public class PatientManagementPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private PatientDataServiceImpl patientDataService;

    // 先查，后删改
    private PatientData patientData;

    // 病人信息输入框
    private JTextField IDField, nameField, genderField, queryField;

    // 病人信息显示框
    private JList<PatientData> patientList;
    private DefaultListModel<PatientData> listModel;

    // 查询结果显示狂
    private JList<PatientData> queryPatientList;
    private DefaultListModel<PatientData> queryListModel;

    // 与体温和艾灸工具的接口
    private ControlPanel controlPanel;
    private TemperaturePlotter temperaturePlotter;

    // 存病人信息
    List<PatientData> patientDataList;
    List<PatientData> queryPatientDataList;

    public PatientManagementPanel(PatientDataServiceImpl patientDataService, ControlPanel controlPanel,
                                  TemperaturePlotter temperaturePlotter) {
        this.patientDataService = patientDataService;
        this.controlPanel = controlPanel;
        this.temperaturePlotter = temperaturePlotter;

        setLayout(new BorderLayout());

        // 左侧：病人信息输入和操作区域
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 病人信息输入框
        JLabel IDLabel = new JLabel("ID：");
        IDField = new JTextField();
        IDField.setEditable(false);
        JLabel nameLabel = new JLabel("姓名：");
        nameField = new JTextField();
        JLabel genderLabel = new JLabel("性别：");
        genderField = new JTextField();

        // 这就是最sb的地方
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(IDLabel, gbc);
        gbc.gridx = 1;
        leftPanel.add(IDField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        leftPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        leftPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        leftPanel.add(genderLabel, gbc);
        gbc.gridx = 1;
        leftPanel.add(genderField, gbc);

        // 操作按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addButton = new JButton("添加病人");
        JButton updateButton = new JButton("更新病人");
        JButton deleteButton = new JButton("删除病人");
        addButton.addActionListener(e -> addPatient());
        updateButton.addActionListener(e -> updatePatient());
        deleteButton.addActionListener(e -> deletePatient());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        leftPanel.add(buttonPanel, gbc);

        // 右侧：病人列表和详细信息
        listModel = new DefaultListModel<>();
        patientList = new JList<>(listModel);
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(patientList);

        // 自定义渲染模式，以便输出主要数据
        patientList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            DateFormat df1 = DateFormat.getDateInstance();
            DateFormat df3 = DateFormat.getTimeInstance();
            if (value != null) {
                JLabel label = new JLabel("ID: " + value.getId() +
                        " | 姓名: " + value.getName() +
                        " | 性别: " + value.getGender() +
                        " | 体温: " + String.format("%.2f", value.getTemperature()) +
                        "℃ | 日期: " + df1.format(value.getTime()) +
                        " | 时间: " + df3.format(value.getTime()));
                // 自定义字体颜色或背景
                if (isSelected) {
                    label.setBackground(Color.RED); // 选中项的背景色
                    label.setForeground(Color.GREEN); // 选中项的文字颜色
                } else {
                    label.setBackground(Color.WHITE); // 默认背景色
                    label.setForeground(Color.DARK_GRAY); // 默认文字颜色
                }
                return label;
            }
            return null;
        });

        // 添加选择事件监听器
        patientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                patientData = (PatientData) patientList.getSelectedValue();
                if (patientData != null) {
                    IDField.setText(patientData.getId() + "");
                    nameField.setText(patientData.getName());
                    genderField.setText(patientData.getGender());
                }
                queryPatientList.clearSelection();
            }
        });

        // 查询显示显示
        queryListModel = new DefaultListModel<>();
        queryPatientList = new JList<>(queryListModel);
        queryPatientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane queryScrollPane = new JScrollPane(queryPatientList);

        // 自定义渲染模式，以便输出主要数据
        queryPatientList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            DateFormat df1 = DateFormat.getDateInstance();
            DateFormat df3 = DateFormat.getTimeInstance();
            if (value != null) {
                JLabel label = new JLabel("ID: " + value.getId() +
                        " | 姓名: " + value.getName() +
                        " | 性别: " + value.getGender() +
                        " | 体温: " + String.format("%.2f", value.getTemperature()) +
                        "℃ | 日期: " + df1.format(value.getTime()) +
                        " | 时间: " + df3.format(value.getTime()));
                // 自定义字体颜色或背景
                if (isSelected) {
                    label.setBackground(Color.RED); // 选中项的背景色
                    label.setForeground(Color.GREEN); // 选中项的文字颜色
                } else {
                    label.setBackground(Color.WHITE); // 默认背景色
                    label.setForeground(Color.DARK_GRAY); // 默认文字颜色
                }
                return label;
            }
            return null;
        });

        // 添加选择事件监听器
        queryPatientList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) return;
                patientData = (PatientData) queryPatientList.getSelectedValue();
                if (patientData != null) {
                    IDField.setText(patientData.getId() + "");
                    nameField.setText(patientData.getName());
                    genderField.setText(patientData.getGender());
                }
                patientList.clearSelection();
            }
        });

        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScrollPane, queryScrollPane);
        rightSplitPane.setDividerLocation(200);

        // 查询面板
        JPanel queryPanel = new JPanel(new BorderLayout());
        queryField = new JTextField();
        JButton queryButton = new JButton("查询病人");
        queryButton.addActionListener(e -> queryPatient(queryField.getText()));
        queryPanel.add(new JLabel("查询姓名："), BorderLayout.WEST);
        queryPanel.add(queryField, BorderLayout.CENTER);
        queryPanel.add(queryButton, BorderLayout.EAST);

        // 主界面布局
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplitPane);
        mainSplitPane.setDividerLocation(300);

        add(queryPanel, BorderLayout.NORTH);
        add(mainSplitPane, BorderLayout.CENTER);

        loadPatientList(queryField.getText());
    }

    // 加载病人列表，并更新查询列表
    private boolean loadPatientList(String name) {
        patientDataList = patientDataService.list();
        listModel.clear();

        for (PatientData patient : patientDataList) {
            listModel.addElement(patient);
        }

        queryPatientDataList = patientDataService.getPatientData(new QueryWrapper<PatientData>().eq("name", name));
        queryListModel.clear();
        if (!queryPatientDataList.isEmpty()) {
            for (PatientData patient : queryPatientDataList) {
                queryListModel.addElement(patient);
            }
        }else{
            return false;
        }

        return true;
    }

    // 添加病人
    private void addPatient() {
        String name = nameField.getText();
        String gender = genderField.getText();

        if (name.isEmpty() || gender.isEmpty()) {
            JOptionPane.showMessageDialog(this, "所有字段必须填写！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 还原！
            PatientData patientData = new PatientData();
            patientData.setName(name);
            patientData.setGender(gender);
            patientData.setTemperature(37.0);
            patientData.setTime(new java.util.Date());

            if (patientDataService.save(patientData)) {
                JOptionPane.showMessageDialog(this, "病人添加成功！");
                controlPanel.controlPanelDataUpdate();
                temperaturePlotter.TemperaturePlotterUpdate();
                clearFields();
                loadPatientList(queryField.getText());
            } else {
                JOptionPane.showMessageDialog(this, "添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "体温格式无效！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 更新病人
    private void updatePatient() {
        if (patientData == null) {
            JOptionPane.showMessageDialog(this, "未找到病人！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        patientData.setName(nameField.getText());
        patientData.setGender(genderField.getText());
        int patientDataId = patientData.getId();
        if (patientDataService.updateById(patientData)) {
            JOptionPane.showMessageDialog(this, "病人信息更新成功！");
            controlPanel.controlPanelDataUpdate();
            temperaturePlotter.TemperaturePlotterUpdate();
            temperaturePlotter.DeletePatient(patientDataId);
            clearFields();
            loadPatientList(queryField.getText());
        } else {
            JOptionPane.showMessageDialog(this, "更新失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 删除病人
    private void deletePatient() {
        if (patientData == null) {
            JOptionPane.showMessageDialog(this, "未找到病人！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (patientDataService.removeById(patientData.getId())) {
            JOptionPane.showMessageDialog(this, "病人删除成功！");
            controlPanel.controlPanelDataUpdate();
            temperaturePlotter.TemperaturePlotterUpdate();
            temperaturePlotter.DeletePatient(patientData.getId());
            clearFields();
            loadPatientList(queryField.getText());
        } else {
            JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 查询病人
    private void queryPatient(String name) {
        queryListModel.clear();
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入病人姓名！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

//        queryPatientDataList = patientDataService.getPatientData(new QueryWrapper<PatientData>().eq("name", name));

        if (!loadPatientList(name)){
            JOptionPane.showMessageDialog(this, "未找到病人: " + name, "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // 清空输入框
    private void clearFields() {
        nameField.setText("");
        genderField.setText("");
        IDField.setText("");
    }
}
