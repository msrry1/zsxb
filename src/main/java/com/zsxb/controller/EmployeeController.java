package com.zsxb.controller;

import com.zsxb.common.CommonDict;
import com.zsxb.common.JsonResult;
import com.zsxb.entity.Employee;
import com.zsxb.mapper.EmployeeMapper;
import com.zsxb.service.EmployeeService;
import com.zsxb.util.JwtUtil;
import com.zsxb.util.RedisCache;
import com.zsxb.vo.EmployeeReturnVO;
import com.zsxb.vo.LoginVO;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PropertyKey;

/**
 * 管理员Controller
 *
 * @author dz
 * @date 2023-05-09
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理员登录
     * @param loginVO 登录的vo
     * @return  登录的管理员和token
     */
    @PostMapping("/login")
    public JsonResult<EmployeeReturnVO> login(@RequestBody LoginVO loginVO) {

        // 默认用户名密码登录
        Employee employee = employeeService.login(loginVO.getUsername(),
                loginVO.getPassword());

        EmployeeReturnVO employeeReturnVO = new EmployeeReturnVO();
        // 根据管理员前缀和id创建token
        String token = JwtUtil.createJWT(CommonDict.EMPLOYEE_TOKENKEY + employee.getEmpId(),
                CommonDict.TOKEN_EXPIRE_TIME);

        // 拷贝设置返回对象值
        BeanUtils.copyProperties(employee, employeeReturnVO);

        // 设置token
        employeeReturnVO.setToken(token);

        return JsonResult.ok(employeeReturnVO);
    }


}