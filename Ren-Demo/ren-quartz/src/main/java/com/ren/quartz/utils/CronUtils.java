package com.ren.quartz.utils;

import java.text.ParseException;
import java.util.Date;

import com.ren.common.utils.DateUtils;
import org.quartz.CronExpression;

/**
 * Cron表达式工具类
 *
 * 功能说明：
 *  1. 验证Cron表达式的有效性
 *  2. 获取Cron表达式无效时的错误信息
 *  3. 计算Cron表达式的下一次触发时间
 *
 * @author admin
 */
public class CronUtils
{
	/**
	 * 验证Cron表达式的有效性
	 *
	 * @param cronExpression 需要验证的Cron表达式
	 * @return boolean - true表示表达式有效，false表示无效
	 */
	public static boolean isValid(String cronExpression)
	{
		return CronExpression.isValidExpression(cronExpression);
	}

	/**
	 * 获取Cron表达式无效时的错误信息
	 *
	 * @param cronExpression 需要检查的Cron表达式
	 * @return String - 如果表达式无效返回错误描述信息，有效则返回null
	 */
	public static String getInvalidMessage(String cronExpression)
	{
		try
		{
			// 尝试创建CronExpression对象，如果成功说明表达式有效
			new CronExpression(cronExpression);
			return null;
		}
		catch (ParseException pe)
		{
			// 捕获解析异常并返回错误信息
			return pe.getMessage();
		}
	}

	/**
	 * 计算Cron表达式的下一个触发时间
	 *
	 * @param cronExpression 需要计算的Cron表达式
	 * @return Date - 表达式下一次触发的时间点
	 * @throws IllegalArgumentException 当表达式无效时抛出此异常
	 *
	 * 注意：该方法返回基于当前系统时间的下一次触发时间
	 */
	public static Date getNextExecution(String cronExpression)
	{
		try
		{
			// 解析Cron表达式
			CronExpression cron = new CronExpression(cronExpression);
			// 获取当前时间后的第一个有效触发时间
			return cron.getNextValidTimeAfter(new Date(DateUtils.current()));
		}
		catch (ParseException e)
		{
			// 表达式无效时抛出运行时异常
			throw new IllegalArgumentException("无效的Cron表达式: " + e.getMessage());
		}
	}
}