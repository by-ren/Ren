package com.ren.admin.controller.monitor;

import com.ren.common.controller.BaseController;
import com.ren.common.core.domain.vo.*;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.utils.ComputerInfoUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/monitor/server")
@Slf4j
@Tag(name = "监控-服务", description = "服务相关监控")
public class ServerController extends BaseController {

    /**
     * 服务监控页面
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/08 13:46
     */
    @GetMapping("/view")
    @Operation(summary = "服务监控", description = "服务监控")
    public AjaxResult serverMonitorView()
    {
        //获取Cpu相关信息
        CpuInfoVO cpuInfo = ComputerInfoUtils.getCpuInfo();
        //获取内存信息
        MemoryInfoVO memoryInfo = ComputerInfoUtils.getMemoryInfo();
        //获取服务器信息
        ComputerSystemInfoVO computerSystemInfo = ComputerInfoUtils.getComputerSystem();
        //获取Java虚拟机信息
        JvmInfoVO jvmInfo = ComputerInfoUtils.getJvmInfo();
        //获取Java信息
        JavaInfoVO javaInfo = ComputerInfoUtils.getJavaInfo();
        //获取项目信息
        ProjectInfoVO projectInfo = ComputerInfoUtils.getProjectInfo();
        //获取磁盘信息
        List<DiskInfoVO> diskInfoList = ComputerInfoUtils.getDiskInfo();
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