package com.ren.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.system.*;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.ren.common.domain.model.bo.*;
import com.ren.common.utils.file.FileUtils;
import com.ren.common.utils.ip.IpUtils;
import oshi.SystemInfo;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.NetworkIF;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * 电脑相关信息工具类（CPU、内存、JAVA虚拟机、磁盘信息）
 * 该工具类依赖于Hutool的Oshi模块
 * 
 * @author ren
 */
public class ComputerInfoUtils
{
    /**
     * 获取CPU相关信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author ren
     * @date 2025/06/07 16:54
     */
    public static CpuInfoBO getCpuInfo()
    {
        // 获取Cpu信息
        CpuInfo cpu = OshiUtil.getCpuInfo();
        return new CpuInfoBO()
                .setCpuNum(cpu.getCpuNum())
                .setCpuModel(cpu.getCpuModel())
                .setUsed(NumberUtil.decimalFormat("0.00", cpu.getUsed()) + "%")
                .setWaitUsed(NumberUtil.decimalFormat("0.00", cpu.getWait()) + "%")
                .setUserUsed(NumberUtil.decimalFormat("0.00", cpu.getUser()) + "%")
                .setSysUsed(NumberUtil.decimalFormat("0.00", cpu.getSys()) + "%")
                .setFree(NumberUtil.decimalFormat("0.00", cpu.getFree()) + "%");
    }

    /**
     * 获取电脑内存信息
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author ren
     * @date 2025/06/07 17:18
     */
    public static MemoryInfoBO getMemoryInfo()
    {
        // 系统内存相关信息
        GlobalMemory globalMemory = OshiUtil.getMemory();
        return new MemoryInfoBO()
                .setTotal(FileUtils.convertFileSize(globalMemory.getTotal()))
                .setAvailable(FileUtils.convertFileSize(globalMemory.getAvailable()))
                .setUsed(FileUtils.convertFileSize(globalMemory.getTotal() - globalMemory.getAvailable()))
                .setOccupancyRate(NumberUtil.decimalFormat("0.00%", BigDecimalUtils.toDouble(BigDecimalUtils.divide((globalMemory.getTotal() - globalMemory.getAvailable()), globalMemory.getTotal(), 2))))
                .setJvmTotal(FileUtils.convertFileSize(RuntimeUtil.getTotalMemory()))
                .setJvmAvailable(FileUtils.convertFileSize(RuntimeUtil.getFreeMemory()))
                .setJvmUsed(FileUtils.convertFileSize(RuntimeUtil.getTotalMemory() - RuntimeUtil.getFreeMemory()))
                .setJvmOccupancyRate(NumberUtil.decimalFormat("0.00%", BigDecimalUtils.toDouble(BigDecimalUtils.divide((RuntimeUtil.getTotalMemory() - RuntimeUtil.getFreeMemory()), RuntimeUtil.getTotalMemory(), 2))))
                .setJvmMax(FileUtils.convertFileSize(RuntimeUtil.getMaxMemory()));
    }

    /**
     * 获取Jvm相关信息
     * @return JvmInfoBO
     * @author ren
     * @date 2025/06/07 17:32
     */
    public static JvmInfoBO getJvmInfo()
    {
        JvmInfo jvmInfo = SystemUtil.getJvmInfo();
        JvmSpecInfo jvmSpecInfo = SystemUtil.getJvmSpecInfo();
        return new JvmInfoBO()
                .setJvmName(jvmInfo.getName())
                .setJvmVersion(jvmInfo.getVersion())
                .setJvmVendor(jvmInfo.getVendor())
                .setJvmInfoStr(jvmInfo.getInfo())
                .setJvmSpecName(jvmSpecInfo.getName())
                .setJvmSpecVersion(jvmSpecInfo.getVersion())
                .setJvmSpecVendor(jvmSpecInfo.getVendor())
                .setInputArgs(ManagementFactory.getRuntimeMXBean().getInputArguments().toString());
    }

    /**
     * 获取Java相关信息
     * @return com.ren.common.domain.model.bo.JavaInfoBO
     * @author ren
     * @date 2025/06/07 17:50
     */
    public static JavaInfoBO getJavaInfo()
    {
        JavaInfo javaInfo = SystemUtil.getJavaInfo();
        JavaRuntimeInfo javaRuntimeInfo = SystemUtil.getJavaRuntimeInfo();
        JavaSpecInfo javaSpecInfo = SystemUtil.getJavaSpecInfo();
        return new JavaInfoBO()
                .setJavaVersion(javaInfo.getVersion())
                .setJavaVersionInt(javaInfo.getVersionInt())
                .setJavaVendor(javaInfo.getVendor())
                .setRuntimeName(javaRuntimeInfo.getName())
                .setRuntimeVersion(javaRuntimeInfo.getVersion())
                .setJavaHome(javaRuntimeInfo.getHomeDir())
                .setJavaSpecName(javaSpecInfo.getName())
                .setJavaSpecVersion(javaSpecInfo.getVersion())
                .setJavaSpecVendor(javaSpecInfo.getVendor());
    }

    /**
     * 获取电脑系统相关信息
     * @return com.ren.common.domain.model.bo.ComputerSystemInfoBO
     * @author ren
     * @date 2025/06/07 17:50
     */
    public static ComputerSystemInfoBO getComputerSystem()
    {

        ComputerSystem computerSystem = OshiUtil.getSystem();
        OperatingSystem os = OshiUtil.getOs();
        return new ComputerSystemInfoBO()
                .setManufacturer(computerSystem.getManufacturer())
                .setModel(computerSystem.getModel())
                .setSerialNumber(computerSystem.getSerialNumber())
                .setFamily(os.getFamily())
                .setOsManufacturer(os.getManufacturer())
                .setVersion(os.getVersionInfo().getVersion())
                .setBitness(os.getBitness())
                .setSystemUptime(os.getSystemUptime())
                .setProcessCount(os.getProcessCount())
                .setComputerName(IpUtils.getHostName())
                .setComputerIp(IpUtils.getHostIp())
                .setOsArch(System.getProperties().getProperty("os.arch"));
    }

    /**
     * 获取项目相关信息
     * @return com.ren.common.domain.model.bo.ProjectInfoBO
     * @author ren
     * @date 2025/06/07 18:40
     */
    public static ProjectInfoBO getProjectInfo()
    {
        // 获取项目路径 (工作目录)
        String projectDir = SystemUtil.getUserInfo().getCurrentDir();
        // 获取JVM启动时间(项目启动时间)
        long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
        // 格式化为可读的日期字符串
        String startTimeStr = DateUtils.timestampToStrDefault(startTime);
        // 计算运行时间(当前时间-启动时间)
        long runningTime = DateUtils.current() - startTime;
        // 获取两个时间之间的间隔，并将间隔转换为人类可读的形式
        String runningTimeStr = DateUtils.formatBetween(runningTime);
        return new ProjectInfoBO()
                .setProjectDir(projectDir)
                .setStartTimeStr(startTimeStr)
                .setRunningTimeStr(runningTimeStr);
    }

    /**
     * 获取磁盘信息
     * @return DiskInfoBO
     * @author ren
     * @date 2025/06/07 17:32
     */
    public static List<DiskInfoBO> getDiskInfo() {
        SystemInfo si = new SystemInfo();
        FileSystem fileSystem = si.getOperatingSystem().getFileSystem();
        List<OSFileStore> fsArray = fileSystem.getFileStores();
        return fsArray.stream().map(DiskInfoBO :: new).toList();
    }

    /**
     * 获取网络相关信息
     * @return java.util.List<com.ren.common.domain.model.bo.NetWorkInfoBO>
     * @author ren
     * @date 2025/06/07 17:54
     */
    public static List<NetWorkInfoBO> getNetWorkInfo()
    {
        //可能返回多个网卡（例如Wifi接口，多个以太网接口）
        List<NetworkIF> networkIFList = OshiUtil.getNetworkIFs();
        return networkIFList.stream().map(NetWorkInfoBO :: new).toList();
    }

}