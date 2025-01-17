package com.smoxisys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smoxisys.domain.PatientData;
import com.smoxisys.service.PatientDataService;
import com.smoxisys.mapper.PatientDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【patient_data】的数据库操作Service实现
* @createDate
*/
@Service
public class PatientDataServiceImpl extends ServiceImpl<PatientDataMapper, PatientData>
    implements PatientDataService{
    public List<PatientData> getPatientData(QueryWrapper<PatientData> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }
}




