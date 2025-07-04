package com.ren.quartz.core.constant;

public class QuartzContents {

	/** 任务名称 */
	public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

	/** 执行目标key */
	public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

	/*=====================================================任务策略=====================================================*/

	/** 默认 */
	public static final byte MISFIRE_DEFAULT = 0;
	/** 立即触发执行 */
	public static final byte MISFIRE_IGNORE_MISFIRES = 1;
	/** 触发一次执行 */
	public static final byte MISFIRE_FIRE_AND_PROCEED = 2;
	/** 不触发立即执行 */
	public static final byte MISFIRE_DO_NOTHING = 3;

	/*=====================================================是否并发=====================================================*/

	/** 是否并发执行_允许 */
	public static final byte CONCURRENT_ALLOW = 0;
	/** 是否并发执行_禁止 */
	public static final byte CONCURRENT_PROHIBIT = 1;

	public enum Status
	{
		/**
		 * 正常
		 */
		NORMAL("0"),
		/**
		 * 暂停
		 */
		PAUSE("1");

		private String value;

		private Status(String value)
		{
			this.value = value;
		}

		public String getValue()
		{
			return value;
		}
	}

}