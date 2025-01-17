package com.smoxisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smoxisys.domain.PatientData;
import com.smoxisys.domain.TemperatureData;
import com.smoxisys.service.PatientDataService;
import com.smoxisys.service.TemperatureDataService;
import com.smoxisys.mapper.TemperatureDataMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
* @author Administrator
* @description 针对表【temperature_data】的数据库操作Service实现
* @createDate
*/
@Service
public class TemperatureDataServiceImpl extends ServiceImpl<TemperatureDataMapper, TemperatureData>
    implements TemperatureDataService{

    private static final Random random = new Random();
    private static final int MAX_DATA_POINTS = 100; // 最多显示 100 个数据点

    private PatientDataService patientDataService;

    public TemperatureDataServiceImpl(PatientDataService patientDataService) {
        this.patientDataService = patientDataService;
    }
    // 模拟温度生成：37°C 到 50°C 之间的随机值
    public Double generateTemperature() {
        return 37 + random.nextDouble() * (50 - 37);
    }

    // 生成一个新的温度数据并保存到数据库
    @Scheduled(fixedRate = 1000)
    public void recordTemperature() {
        List<PatientData> patientDataList = patientDataService.list();
        List<TemperatureData> temperatureDataList;

        Double temperature;
        for (PatientData patientData : patientDataList) {
            temperature = generateTemperature();
            TemperatureData data = new TemperatureData();

            data.setPatientName(patientData.getName());  // 可以根据实际需求修改病人名称
            data.setPatientId(patientData.getId());
            data.setTemperature(temperature);
            data.setRecordedTime(LocalDateTime.now());

            patientData.setTemperature(temperature);
            patientDataService.updateById(patientData);

            // 优化不了了，就这样吧（
            temperatureDataList = baseMapper.selectList(
                    new QueryWrapper<TemperatureData>().eq("patient_id", patientData.getId())
            );

            // 超过100个数据点就删掉
            if (temperatureDataList.size() >= MAX_DATA_POINTS) {
                baseMapper.deleteById(temperatureDataList.get(0).getId());
            }
            this.save(data);
        }
    }

    // 最好给我传病人的ID，否则重名会造成可怕的后果*-*
    public List<Double> getTemperatureList(int patientId) {
        List<Double> temperatureDoubleList = new ArrayList<>();
        List<TemperatureData> temperatureDataList = baseMapper.selectList(
                (new QueryWrapper<TemperatureData>().eq("patient_id", patientId))
        );

        // 如果是空的
        if (temperatureDataList.isEmpty())
        {
            return null;
        }

        // 如果不是空的
        for (TemperatureData temperatureData : temperatureDataList) {
            temperatureDoubleList.add(temperatureData.getTemperature());
        }
        return temperatureDoubleList;
    }
}




