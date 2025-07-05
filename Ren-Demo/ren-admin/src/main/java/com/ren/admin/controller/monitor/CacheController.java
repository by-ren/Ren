package com.ren.admin.controller.monitor;

import cn.hutool.core.convert.Convert;
import com.ren.common.core.constant.RedisCacheConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.core.response.AjaxResult;
import com.ren.common.utils.BigDecimalUtils;
import com.ren.common.manager.redis.RedisMonitorInfoManager;
import com.ren.common.manager.redis.RedisOperateManager;
import com.ren.monitor.core.domain.vo.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/monitor/cache")
@Slf4j
@Tag(name = "监控-缓存", description = "缓存相关监控")
public class CacheController extends BaseController {

	@Autowired
	private RedisOperateManager redisOperateManager;
	@Autowired
	private RedisMonitorInfoManager redisMonitorInfoManager;

	/**
	 * 缓存列表
	 * @return com.ren.common.domain.model.dto.AjaxResult
	 * @author ren
	 * @date 2025/06/10 15:44
	 */
	@GetMapping("/list")
	@Operation(summary = "缓存列表", description = "缓存列表")
	public AjaxResult cacheList()
	{
		List<CacheVO> cacheVOList = new ArrayList<>();
		cacheVOList.add(new CacheVO(RedisCacheConstants.REFRESH_TOKEN_KEY,  "用户信息"));
		cacheVOList.add(new CacheVO(RedisCacheConstants.BLACK_TOKEN_KEY,  "登录黑名单"));
		return success()
				.put("cacheList",cacheVOList);
	}

    /**
     * 键值对列表
     * @param name
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/10 15:54
     */
	@GetMapping("/key/list")
	@Operation(summary = "键值对列表", description = "键值对列表")
	public AjaxResult cacheKeyList(String name)
	{
		List<CacheKeyVO> cacheKeyVOList = new ArrayList<>();
		String pattern = name + ":" + "*"; // 匹配所有regresh_token:开头的键
		// 使用SCAN命令安全遍历（避免KEYS阻塞）
		try(Cursor<String> cursor = redisOperateManager.scan(pattern)){
			while (cursor.hasNext()) {
				String key = cursor.next();
				// 获取键对应的值（JWT Token）
				String refreshToken = redisOperateManager.getCacheObject(key);
				cacheKeyVOList.add(new CacheKeyVO(key.substring(key.indexOf(":")+1), refreshToken));
			}
		}
		return success()
				.put("keyList",cacheKeyVOList);
	}

	/**
	 * 删除缓存
	 * @param name
	 * @return com.ren.common.domain.model.dto.AjaxResult
	 * @author ren
	 * @date 2025/06/10 16:45
	 */
	@DeleteMapping("/delete")
	@Operation(summary = "删除缓存", description = "删除缓存")
	public AjaxResult cacheDelete(String name) {
		redisOperateManager.deleteKeysByPattern(name + ":" + "*");
		return success();
	}

	/**
	 * 删除键值
	 * @param name
	 * @param key
	 * @return com.ren.common.domain.model.dto.AjaxResult
	 * @author ren
	 * @date 2025/06/10 16:51
	 */
	@DeleteMapping("/key/delete")
	@Operation(summary = "删除键值", description = "删除键值")
	public AjaxResult cacheKeyDelete(String name, String key) {
		redisOperateManager.deleteObject(name + ":" + key);
		return success();
	}

    /**
     * 清空缓存
     * @return com.ren.common.domain.model.dto.AjaxResult
     * @author ren
     * @date 2025/06/10 17:00
     */
	@DeleteMapping("/clean")
	@Operation(summary = "清空缓存", description = "清空缓存")
	public AjaxResult cacheKeyClean() {
		redisOperateManager.deleteKeysByPattern("*");
		return success();
	}

	/**
	 * 缓存监控详情
	 * @return com.ren.common.domain.model.dto.AjaxResult
	 * @author ren
	 * @date 2025/06/17 13:37
	 */
	@GetMapping("/detail")
	@Operation(summary = "缓存监控详情", description = "缓存监控详情")
	public AjaxResult cacheDetail()
	{

		Properties redisBasicInfo = redisMonitorInfoManager.getRedisBasicInfo();
		Object keyNumber = redisMonitorInfoManager.getRedisKeyNumber();

		//基础信息总列表
		List<List<BasicInfoItemVO>> basicInfoItemList = new ArrayList<>();
		//基础信息第一行列表
		List<BasicInfoItemVO> basicInfoItemVOFirstList = new ArrayList<>();
		basicInfoItemVOFirstList.add(new BasicInfoItemVO("Redis版本",redisBasicInfo.getProperty("redis_version")));
		basicInfoItemVOFirstList.add(new BasicInfoItemVO("运行模式",redisBasicInfo.getProperty("redis_mode").equals("standalone") ? "单机" : "集群"));
		basicInfoItemVOFirstList.add(new BasicInfoItemVO("端口",redisBasicInfo.getProperty("tcp_port")));
		basicInfoItemVOFirstList.add(new BasicInfoItemVO("客户端数",redisBasicInfo.getProperty("connected_clients")));
		//基础信息第二行列表
		List<BasicInfoItemVO> basicInfoItemVOSecondList = new ArrayList<>();
		basicInfoItemVOSecondList.add(new BasicInfoItemVO("运行时间",redisBasicInfo.getProperty("uptime_in_days","")));
		basicInfoItemVOSecondList.add(new BasicInfoItemVO("使用内存",redisBasicInfo.getProperty("used_memory_human","")));
		basicInfoItemVOSecondList.add(new BasicInfoItemVO("使用CPU", Convert.toStr(BigDecimalUtils.scale(redisBasicInfo.getProperty("used_cpu_sys_children"),2),"")));
		basicInfoItemVOSecondList.add(new BasicInfoItemVO("内存配置",redisBasicInfo.getProperty("maxmemory_human","")));
		//基础信息第三行列表
		List<BasicInfoItemVO> basicInfoItemVOThirdList = new ArrayList<>();
		basicInfoItemVOThirdList.add(new BasicInfoItemVO("AOF是否开启",redisBasicInfo.getProperty("aof_enabled").equals("0") ? "否":"是"));
		basicInfoItemVOThirdList.add(new BasicInfoItemVO("RDB是否成功",redisBasicInfo.getProperty("rdb_last_bgsave_status","")));
		basicInfoItemVOThirdList.add(new BasicInfoItemVO("Key数量",Convert.toStr(keyNumber,"")));
		basicInfoItemVOThirdList.add(new BasicInfoItemVO("网络入口",redisBasicInfo.getProperty("instantaneous_input_kbps","")));
		basicInfoItemList.add(basicInfoItemVOFirstList);
		basicInfoItemList.add(basicInfoItemVOSecondList);
		basicInfoItemList.add(basicInfoItemVOThirdList);

		//命令统计
		Properties redisCommandStatInfo = redisMonitorInfoManager.getRedisCommandStatInfo();
		List<CommandStatItemVO> commandStatItemVOList = new ArrayList<>();
		redisCommandStatInfo.stringPropertyNames().forEach(key -> {
			// 获取命令属性字符串
			String property = redisCommandStatInfo.getProperty(key);
			// 提取命令名称（移除前缀cmdstat_）
			String name = StringUtils.removeStart(key, "cmdstat_");
			// 提取调用次数（从格式为"calls=12,usec=120"中提取12）
			Integer value = Convert.toInt(StringUtils.substringBetween(property, "calls=", ",usec"));
			//加入列表
			commandStatItemVOList.add(new CommandStatItemVO(name, value));
		});

		//内存统计
		MemoryStatItemVO memoryStatItemVO = new MemoryStatItemVO(Convert.toDouble(0),Convert.toDouble(1000),Convert.toDouble(redisBasicInfo.getProperty("used_memory_human")));

		return success()
				.put("basicInfoRows",basicInfoItemList)
				.put("commandStats", commandStatItemVOList)
				.put("memoryStats", memoryStatItemVO);
	}

}