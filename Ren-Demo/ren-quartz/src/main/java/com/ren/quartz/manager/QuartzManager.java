package com.ren.quartz.manager;

import cn.hutool.core.util.ObjUtil;
import com.ren.common.domain.constant.Constants;
import com.ren.common.manager.SpringManager;
import com.ren.common.utils.StringUtils;
import com.ren.quartz.domain.constant.QuartzContents;
import com.ren.quartz.domain.entity.TimedTask;
import com.ren.quartz.domain.exception.QuartzException;
import com.ren.quartz.domain.job.QuartzDisallowConcurrentExecution;
import com.ren.quartz.domain.job.QuartzJobExecution;
import com.ren.quartz.utils.CronUtils;
import com.ren.quartz.utils.JobInvokeUtil;
import org.quartz.*;
import org.springframework.beans.BeansException;

public class QuartzManager {

	/**
	 * 创建定时任务
	 * @param scheduler（Quartz的核心组件之一（调度任务的总控制器，负责管理所有任务和触发器），除他之外还有Job（定义具体的业务逻辑，实现execute()方法执行实际任务），Trigger（设置任务触发规则（如时间、频率）。CronTrigger是其具体实现之一））
	 * @param timedTask 自定义的任务对象
	 * @author ren
	 * @date 2025/06/25 09:19
	 */
	public static void createScheduleJob(Scheduler scheduler, TimedTask timedTask) throws SchedulerException, QuartzException
	{
		// 根据所要创建任务是否允许并发选择不同的策略
		Class<? extends Job> jobClass = getQuartzJobClass(timedTask);

		// 创建一个Quartz的任务，并设置任务唯一标识
		Long taskId = timedTask.getTaskId();
		String taskGroup = timedTask.getTaskGroup();
		JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(taskId, taskGroup)).build();

		// 将整个任务信息放入任务参数中，方便后续调用任务时获取相关参数
		jobDetail.getJobDataMap().put(QuartzContents.TASK_PROPERTIES, timedTask);

		// 根据所传任务创建一个CronExpression表达式构建器
		CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(timedTask.getCronExpression());
		// 根据自定义任务中选择的策略，为CronScheduleBuilder绑定不同的执行策略
		cronScheduleBuilder = handleCronScheduleMisfirePolicy(timedTask, cronScheduleBuilder);

		// 将CronExpression表达式构建器绑定金Trigger中，CronTrigger是Trigger的一个实现类
		// CronTrigger 是 Quartz 调度框架中基于 cron 表达式的时间触发器实现，它负责精确控制任务的执行时间
		CronTrigger trigger = TriggerBuilder.newTrigger()   // 1. 创建触发器构建器
				.withIdentity(getTriggerKey(taskId, taskGroup)) // 2. 设置触发器的唯一标识
				.withSchedule(cronScheduleBuilder)              // 3. 设置触发器的时间表（cron表达式规则）
				.build();                                       // 4. 构建触发器实例

		// 判断任务是否已经存在
		if (scheduler.checkExists(getJobKey(taskId, taskGroup)))
		{
			// 如果已经存 先移除，然后在执行创建操作（防止创建时存在数据问题）
			scheduler.deleteJob(getJobKey(taskId, taskGroup));
		}

		// 获取下次任务执行时间，如果不为空，则表示未过期
		if (ObjUtil.isNotNull(CronUtils.getNextExecution(timedTask.getCronExpression())))
		{
			// 任务未过期，有效任务，将任务与触发器关联起来，实行注册
			scheduler.scheduleJob(jobDetail, trigger);
		}

		// 判断该任务是否处于暂停状态，如果是，则在注册后执行暂停操作
		if (timedTask.getStatus().equals(QuartzContents.Status.PAUSE.getValue()))
		{
			scheduler.pauseJob(getJobKey(taskId, taskGroup));
		}
	}

	/**
	 * 根据所创建任务是否允许并发选择不同的策略
	 * @param timedTask
	 * @return java.lang.Class<? extends org.quartz.Job>
	 * @author ren
	 * @date 2025/06/23 16:56
	 */
	private static Class<? extends Job> getQuartzJobClass(TimedTask timedTask)
	{
		boolean isConcurrent = timedTask.getConcurrent() == QuartzContents.CONCURRENT_ALLOW;
		return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
	}

	/**
	 * 根绝任务名称（固定的字符加TaskId）与任务组创建Job（定时任务）所需的Key，用于绑定唯一的Job
	 * @param taskId
	 * @param taskGroup
	 * @return org.quartz.JobKey
	 * @author ren
	 * @date 2025/06/23 17:00
	 */
	public static JobKey getJobKey(Long taskId, String taskGroup)
	{
		return JobKey.jobKey(QuartzContents.TASK_CLASS_NAME + taskId, taskGroup);
	}

	/**
	 * 根绝任务名称（固定的字符加TaskId）与任务组获取Trigger（触发器）所需的Key，用于绑定唯一的Trigger
	 * @param taskId
	 * @param taskGroup
	 * @return org.quartz.TriggerKey 触发器唯一标识
	 * @author ren
	 * @date 2025/06/23 17:08
	 */
	public static TriggerKey getTriggerKey(Long taskId, String taskGroup)
	{
		return TriggerKey.triggerKey(QuartzContents.TASK_CLASS_NAME + taskId, taskGroup);
	}

	/**
	 * 处理定时任务的错过触发策略
	 *
	 * @param timedTask 定时任务对象，包含任务配置信息
	 * @param cb Cron表达式调度构建器
	 * @return 配置了对应错过触发策略的CronScheduleBuilder
	 * @throws QuartzException 当传入不支持的策略时抛出配置错误异常
	 */
	public static CronScheduleBuilder handleCronScheduleMisfirePolicy(TimedTask timedTask, CronScheduleBuilder cb)
			throws QuartzException
	{
		// 根据任务中配置的错过触发策略，设置不同的处理方式
		switch (timedTask.getMisfirePolicy())
		{
			// 策略1：使用Quartz默认的错过触发处理方式
			case QuartzContents.MISFIRE_DEFAULT:
				return cb; // 保持原有策略不变
			// 策略2：忽略所有错过的触发，并立即触发一次执行
			case QuartzContents.MISFIRE_IGNORE_MISFIRES:
				// 示例场景：每日报表任务因系统维护错过，恢复后立即补发一次报表
				return cb.withMisfireHandlingInstructionIgnoreMisfires();
			// 策略3：立即触发一次错过的执行（仅第一次错过），然后继续正常调度
			case QuartzContents.MISFIRE_FIRE_AND_PROCEED:
				// 示例场景：每小时的监控检查任务错过一次，立即执行当前检查，后续保持正常节奏
				return cb.withMisfireHandlingInstructionFireAndProceed();
			// 策略4：什么都不做，等待下次触发
			case QuartzContents.MISFIRE_DO_NOTHING:
				// 示例场景：非关键性日志清理任务，错过就跳过本次
				return cb.withMisfireHandlingInstructionDoNothing();
			// 错误处理：当传入无法识别的策略时
			default:
				// 抛出明确的配置错误异常
				throw new QuartzException("The task misfire policy '" + timedTask.getMisfirePolicy()
						+ "' cannot be used in cron schedule tasks", QuartzException.Code.CONFIG_ERROR);
		}
	}


	/**
	 * 检查任务调用目标方法是否在白名单包路径内（安全校验）
	 *
	 * <p>核心逻辑：
	 * 1. 识别目标方法是Spring Bean方法调用还是完整类路径调用
	 * 2. 确保目标类在允许的安全包范围内
	 * 3. 排除禁止的危险包路径
	 *
	 * @param invokeTarget 目标方法调用表达式，格式为：
	 *                     - Spring Bean调用："beanName.methodName()"
	 *                     - 完整类路径调用："com.example.service.ReportService.generateReport()"
	 * @return true: 允许执行, false: 禁止执行
	 */
	public static boolean whiteList(String invokeTarget)
	{
		// 提取方法调用路径（移除参数部分）："beanName.methodName()" -> "beanName.methodName"
		String packageName = StringUtils.subBefore(invokeTarget, "(", false);

		// 处理完整类路径调用（如com.example.Service.method）
		// 判断调用类型是完整类型调用还是SpringBean调用
		if (JobInvokeUtil.isValidClassName(packageName))
		{
			// 直接检查完整路径是否包含允许的白名单包路径(忽略大小写)（自己写的包肯定不可能在Java内置的包下面，无需检查黑名单）
			return StringUtils.containsAnyIgnoreCase(invokeTarget, Constants.JOB_WHITELIST_STR);
		}

		// 处理Spring Bean调用（如reportService.generate）
		// 从"beanName.methodName"中提取bean名称（第一个点前的部分）
		String beanName = StringUtils.split(invokeTarget, ".").get(0);

		try {
			// 从Spring容器中获取Bean实例
			Object obj = SpringManager.getBean(beanName);
			// 获取Bean实现类的实际包名
			String beanPackageName = obj.getClass().getPackage().getName();

			// 双重安全校验：
			//     a) 包路径必须在白名单范围内
			//     b) 包路径不能在黑名单内（因为根据SpringBean获取包路径，很有可能获取到Java中内置的一些Bean，而不是自定义的Bean，如果获取到了内置的Bean，则不允许进行创建）
			return StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_WHITELIST_STR)
					&& !StringUtils.containsAnyIgnoreCase(beanPackageName, Constants.JOB_ERROR_STR);
		}catch (BeansException e){
			return false;
		}
	}

	/**
	 * 判断任务是否正确
	 * @param timedTask
	 * @return java.lang.String
	 * @author ren
	 * @date 2025/06/25 10:28
	 */
	public static String isTaskCompliant(TimedTask timedTask){
		String reason = "";
		if (!CronUtils.isValid(timedTask.getCronExpression()))
		{
			reason = "Cron表达式无效";
		}
		else if (StringUtils.containsIgnoreCase(timedTask.getInvokeTarget(), Constants.LOOKUP_RMI))
		{
			reason = "目标字符串不允许'rmi'调用";
		}
		else if (StringUtils.containsAnyIgnoreCase(timedTask.getInvokeTarget(), new String[] { Constants.LOOKUP_LDAP, Constants.LOOKUP_LDAPS }))
		{
			reason = "目标字符串不允许'ldap(s)'调用";
		}
		else if (StringUtils.containsAnyIgnoreCase(timedTask.getInvokeTarget(), new String[] { Constants.HTTP, Constants.HTTPS }))
		{
			reason = "目标字符串不允许'http(s)'调用";
		}
		else if (StringUtils.containsAnyIgnoreCase(timedTask.getInvokeTarget(), Constants.JOB_ERROR_STR))
		{
			reason = "目标字符串存在违规";
		}
		else if (!QuartzManager.whiteList(timedTask.getInvokeTarget()))
		{
			reason = "目标字符串不在白名单内";
		}
		return StringUtils.isNotBlank(reason) ? StringUtils.format("新增任务{}失败，{}", timedTask.getTaskName(),reason) : "任务合规";
	}

	/**
	 * 删除任务
	 * @param scheduler
	 * @param taskId
	 * @param taskGroup
	 * @return boolean
	 * @author ren
	 * @date 2025/06/25 10:13
	 */
	public static boolean removeJob(Scheduler scheduler, long taskId, String taskGroup) throws SchedulerException {
		boolean isSuccess = false;
		//获取原任务Key
		JobKey jobKey = QuartzManager.getJobKey(taskId, taskGroup);
		//如果原任务存在，则删除
		if(scheduler.checkExists(jobKey)){
			isSuccess = scheduler.deleteJob(jobKey);
		}
		return isSuccess;
	}

}