package com.csy.druid.demo.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.br.dynamic.loglevelspringbootstarter.service.LoglevelSettingService;
import com.csy.druid.demo.beans.Contract;
import com.csy.druid.demo.mapper.ContractMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 合同表
 *
 * @author csy 2019-09-06
 */
@Slf4j
@RestController
@RequestMapping("/contract")
public class ContractController {

    @Resource
    private ContractMapper contractMapper;

    /**
     * 新增或编辑
     */
    @PostMapping("/save")
    public Object save(Contract contract) {
        Contract contract1 = contractMapper.selectOne(new QueryWrapper<Contract>().eq("id", 1));
        if (contract1 != null) {
            contractMapper.updateById(contract);
        } else {
            contractMapper.insert(contract);
        }
        return "success";
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    public Object delete(int id) {
        Contract contract = contractMapper.selectOne(new QueryWrapper<Contract>().eq("id", id));
        if (contract != null) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 查询
     */
    @GetMapping("/find")
    public Object find(int id) {
        Contract contract = contractMapper.selectOne(new QueryWrapper<Contract>().eq("id", id));
        if (contract != null) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 分页查询
     */
    @GetMapping("/list")
    public Object list(Contract contract,
                       @RequestParam(required = false, defaultValue = "0") int pageNo,
                       @RequestParam(required = false, defaultValue = "10") int pageSize) {
        //分页构造器
        Page<Contract> page = new Page<>(pageNo, pageSize);
        //条件构造器
        QueryWrapper<Contract> queryWrapperw = new QueryWrapper<>(contract);
        //执行分页
        IPage<Contract> pageList = contractMapper.selectPage(page, queryWrapperw);
        //返回结果
        return "success";
    }

    @Resource
    private LoglevelSettingService loglevelSettingService;

    @GetMapping("/testLog")
    public void testLog(String level) {
        System.out.println("修改前" + log.getName());
        log.debug("当前模式为DEBUG");
        log.info("当前模式为INFO");
        log.error("当前模式为ERROR");
        log.warn("当前模式为WARN");
        loglevelSettingService.setRootLoggerLevel(level);
        System.out.println("修改后" + log.getName());
        log.debug("当前模式为DEBUG");
        log.info("当前模式为INFO");
        log.error("当前模式为ERROR");
        log.warn("当前模式为WARN");
    }

}
