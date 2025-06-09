package com.ren.admin.controller.monitor;

import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.bo.*;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.utils.ComputerInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitor/server")
@Slf4j
public class ServerController extends BaseController {

    /**
     * 服务监控页面
     * @return com.ren.common.domain.model.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/08 13:46
     */
    @GetMapping("/view")
    public AjaxResultDTO serverMonitorView()
    {
        //获取Cpu相关信息
        CpuInfoBO cpuInfo = ComputerInfoUtils.getCpuInfo();
        //获取内存信息
        MemoryInfoBO memoryInfo = ComputerInfoUtils.getMemoryInfo();
        //获取服务器信息
        ComputerSystemInfoBO computerSystemInfo = ComputerInfoUtils.getComputerSystem();
        //获取Java虚拟机信息
        JvmInfoBO jvmInfo = ComputerInfoUtils.getJvmInfo();
        //获取Java信息
        JavaInfoBO javaInfo = ComputerInfoUtils.getJavaInfo();
        //获取项目信息
        ProjectInfoBO projectInfo = ComputerInfoUtils.getProjectInfo();
        //获取磁盘信息
        List<DiskInfoBO> diskInfoList = ComputerInfoUtils.getDiskInfo();
        return success()
                .put("cpuInfo",cpuInfo)
                .put("memoryInfo",memoryInfo)
                .put("computerSystemInfo",computerSystemInfo)//
                .put("jvmInfo",jvmInfo)
                .put("javaInfo",javaInfo)
                .put("projectInfo",projectInfo)
                .put("diskInfoList",diskInfoList);
    }

}