package com.ren.quartz.controller;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ren.common.controller.BaseController;
import com.ren.common.core.enums.BusinessType;
import com.ren.common.core.interfaces.OperLogAnn;
import com.ren.common.core.interfaces.Pageable;
import com.ren.common.core.domain.bo.LoginUser;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.core.page.TableDataInfo;
import com.ren.common.utils.StringUtils;
import com.ren.quartz.core.domain.entity.TimedTask;
import com.ren.quartz.core.exception.QuartzException;
import com.ren.quartz.manager.QuartzManager;
import com.ren.quartz.service.TimedTaskService;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/timedTask")
@Slf4j
@Tag(name = "定时任务相关", description = "定时任务相关")
public class TimedTaskController extends BaseController {

    @Autowired
    TimedTaskService timedTaskService;
    // Quartz调度器
    @Autowired
    private Scheduler scheduler;

    /**
     * 初始化任务
     *
     * @author ren
     * @date 2025/06/26 20:37
     */
    @PostConstruct
    @Operation(summary = "初始化任务", description = "初始化任务")
    public void init() throws SchedulerException, QuartzException {
        scheduler.clear();
        List<TimedTask> timedTaskList = timedTaskService.listTimedTaskByParam(null);
        for (TimedTask timedTask : timedTaskList) {
            QuartzManager.createScheduleJob(scheduler, timedTask);
        }
    }

    /**
     * 分页获取任务列表
     * 
     * @param paramMap
     * @return com.ren.common.domain.page.TableDataInfo
     * @author ren
     * @date 2025/06/23 15:12
     */
    @GetMapping("/list/page")
    @Pageable // 注意，如果要开启字典类型，请添加该注解
    @Operation(summary = "分页获取任务列表", description = "分页获取任务列表")
    public TableDataInfo listTimedTaskByPage(@RequestParam Map<String, Object> paramMap) {
        IPage<TimedTask> dictTypeList = timedTaskService.listTimedTaskByPage(paramMap);
        return getDataTable(dictTypeList);
    }

    /**
     * 添加定时任务
     * 
     * @param loginUser
     * @param timedTask
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/23 15:20
     */
    @PostMapping("/add")
    @OperLogAnn(title = "定时任务模块", businessType = BusinessType.INSERT)
    @Operation(summary = "添加定时任务", description = "添加定时任务")
    public AjaxResult addTimedTask(@AuthenticationPrincipal LoginUser loginUser,
								   @RequestBody(required = false) TimedTask timedTask) {
        // 判断任务是否合规
        String result = QuartzManager.isTaskCompliant(timedTask);
        if (!StringUtils.equals("任务合规", result))
            return error(result);
        try {
            // 添加任务
            timedTaskService.addTimedTask(timedTask, loginUser.getUsername());
            return success();
        } catch (SchedulerException | QuartzException e) {
            return error("添加定时任务失败，请稍后重试");
        }
    }

    /**
     * 编辑定时任务
     * 
     * @param loginUser
     * @param timedTask
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/25 14:09
     */
    @PostMapping("/modify")
    @OperLogAnn(title = "定时任务模块", businessType = BusinessType.INSERT)
    @Operation(summary = "编辑定时任务", description = "编辑定时任务")
    public AjaxResult modifyTimedTask(@AuthenticationPrincipal LoginUser loginUser,
									  @RequestBody(required = false) TimedTask timedTask) {
        // 判断任务是否合规
        String result = QuartzManager.isTaskCompliant(timedTask);
        if (!StringUtils.equals("任务合规", result))
            return error(result);
        try {
            // 编辑任务
            timedTaskService.modifyTimedTaskById(timedTask, loginUser.getUsername());
            return success();
        } catch (SchedulerException | QuartzException e) {
            return error("编辑定时任务失败，请稍后重试");
        }
    }

    /**
     * 修改定时任务状态
     * 
     * @param loginUser
     * @param paramMap
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/25 14:17
     */
    @PostMapping("/modifyStatus")
    @OperLogAnn(title = "定时任务模块", businessType = BusinessType.INSERT)
    @Operation(summary = "修改定时任务状态", description = "修改定时任务状态")
    public AjaxResult modifyTimedTaskStatus(@AuthenticationPrincipal LoginUser loginUser,
											@RequestBody(required = false) Map<String, Object> paramMap) {
        try {
            timedTaskService.modifyTimedTaskStatusById(Convert.toLong(paramMap.get("taskId")),
                Convert.toByte(paramMap.get("status")), loginUser.getUsername());
            return success();
        } catch (SchedulerException e) {
            return error("操作失败，请稍后重试");
        }
    }

    /**
     * 删除定时任务
     * 
     * @param taskId
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/25 14:18
     */
    @DeleteMapping("/delete")
    @OperLogAnn(title = "定时任务模块", businessType = BusinessType.DELETE)
    @Operation(summary = "删除定时任务", description = "删除定时任务")
    public AjaxResult deleteTimedTask(long taskId) {
        try {
            timedTaskService.removeTimedTaskById(taskId);
            return success();
        } catch (SchedulerException e) {
            return error("操作失败，请稍后重试");
        }
    }

}