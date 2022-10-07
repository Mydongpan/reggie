package com.mayuan.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayuan.reggie.entity.Employee;
import com.mayuan.reggie.mapper.EmployeeMapper;
import com.mayuan.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
