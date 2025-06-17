package com.ren.admin.controller.monitor;

import cn.hutool.core.convert.Convert;
import com.ren.common.constant.RedisCacheConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.utils.BigDecimalUtils;
import com.ren.common.utils.redis.RedisMonitorInfoUtils;
import com.ren.common.utils.redis.RedisOperateUtils;
import com.ren.monitor.domain.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/monitor/cache")
@Slf4j
public class CacheController extends BaseController {

	@Autowired
	private RedisOperateUtils redisOperateUtils;
	@Autowired
	private RedisMonitorInfoUtils redisMonitorInfoUtils;

	/**
	 * 缓存列表
	 * @return com.ren.common.domain.model.dto.AjaxResultDTO
	 * @author ren
	 * @date 2025/06/10 15:44
	 */
	@GetMapping("/list")
	public AjaxResultDTO cacheList()
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
     * @return com.ren.common.domain.model.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/10 15:54
     */
	@GetMapping("/key/list")
	public AjaxResultDTO cacheKeyList(String name)
	{
		List<CacheKeyVO> cacheKeyVOList = new ArrayList<>();
		String pattern = name + ":" + "*"; // 匹配所有regresh_token:开头的键
		// 使用SCAN命令安全遍历（避免KEYS阻塞）
		try(Cursor<String> cursor = redisOperateUtils.scan(pattern)){
			while (cursor.hasNext()) {
				String key = cursor.next();
				// 获取键对应的值（JWT Token）
				String refreshToken = redisOperateUtils.getCacheObject(key);
				cacheKeyVOList.add(new CacheKeyVO(key.substring(key.indexOf(":")+1), refreshToken));
			}
		}
		return success()
				.put("keyList",cacheKeyVOList);
	}

	/**
	 * 删除缓存
	 * @param name
	 * @return com.ren.common.domain.model.dto.AjaxResultDTO
	 * @author ren
	 * @date 2025/06/10 16:45
	 */
	@DeleteMapping("/delete")
	public AjaxResultDTO cacheDelete(String name) {
		redisOperateUtils.deleteKeysByPattern(name + ":" + "*");
		return success();
	}

	/**
	 * 删除键值
	 * @param name
	 * @param key
	 * @return com.ren.common.domain.model.dto.AjaxResultDTO
	 * @author ren
	 * @date 2025/06/10 16:51
	 */
	@DeleteMapping("/key/delete")
	public AjaxResultDTO cacheKeyDelete(String name,String key) {
		redisOperateUtils.deleteObject(name + ":" + key);
		return success();
	}

    /**
     * 清空缓存
     * @return com.ren.common.domain.model.dto.AjaxResultDTO
     * @author ren
     * @date 2025/06/10 17:00
     */
	@DeleteMapping("/clean")
	public AjaxResultDTO cacheKeyClean() {
		redisOperateUtils.deleteKeysByPattern("*");
		return success();
	}

	/**
	 * 缓存监控详情
	 * @return com.ren.common.domain.model.dto.AjaxResultDTO
	 * @author ren
	 * @date 2025/06/17 13:37
	 */
	@GetMapping("/detail")
	public AjaxResultDTO cacheDetail()
	{

		Properties redisBasicInfo = redisMonitorInfoUtils.getRedisBasicInfo();
		Object keyNumber = redisMonitorInfoUtils.getRedisKeyNumber();

		//基础信息总列表
		List<List<BasicInfoItem>> basicInfoItemList = new ArrayList<>();
		//基础信息第一行列表
		List<BasicInfoItem> basicInfoItemFirstList = new ArrayList<>();
		basicInfoItemFirstList.add(new BasicInfoItem("Redis版本",redisBasicInfo.getProperty("redis_version")));
		basicInfoItemFirstList.add(new BasicInfoItem("运行模式",redisBasicInfo.getProperty("redis_mode").equals("standalone") ? "单机" : "集群"));
		basicInfoItemFirstList.add(new BasicInfoItem("端口",redisBasicInfo.getProperty("tcp_port")));
		basicInfoItemFirstList.add(new BasicInfoItem("客户端数",redisBasicInfo.getProperty("connected_clients")));
		//基础信息第二行列表
		List<BasicInfoItem> basicInfoItemSecondList = new ArrayList<>();
		basicInfoItemSecondList.add(new BasicInfoItem("运行时间",redisBasicInfo.getProperty("uptime_in_days","")));
		basicInfoItemSecondList.add(new BasicInfoItem("使用内存",redisBasicInfo.getProperty("used_memory_human","")));
		basicInfoItemSecondList.add(new BasicInfoItem("使用CPU", Convert.toStr(BigDecimalUtils.scale(redisBasicInfo.getProperty("used_cpu_sys_children"),2),"")));
		basicInfoItemSecondList.add(new BasicInfoItem("内存配置",redisBasicInfo.getProperty("maxmemory_human","")));
		//基础信息第三行列表
		List<BasicInfoItem> basicInfoItemThirdList = new ArrayList<>();
		basicInfoItemThirdList.add(new BasicInfoItem("AOF是否开启",redisBasicInfo.getProperty("aof_enabled").equals("0") ? "否":"是"));
		basicInfoItemThirdList.add(new BasicInfoItem("RDB是否成功",redisBasicInfo.getProperty("rdb_last_bgsave_status","")));
		basicInfoItemThirdList.add(new BasicInfoItem("Key数量",Convert.toStr(keyNumber,"")));
		basicInfoItemThirdList.add(new BasicInfoItem("网络入口",redisBasicInfo.getProperty("instantaneous_input_kbps","")));
		basicInfoItemList.add(basicInfoItemFirstList);
		basicInfoItemList.add(basicInfoItemSecondList);
		basicInfoItemList.add(basicInfoItemThirdList);

		//命令统计
		Properties redisCommandStatInfo = redisMonitorInfoUtils.getRedisCommandStatInfo();
		List<CommandStatItem> commandStatItemList = new ArrayList<>();
		redisCommandStatInfo.stringPropertyNames().forEach(key -> {
			// 获取命令属性字符串
			String property = redisCommandStatInfo.getProperty(key);
			// 提取命令名称（移除前缀cmdstat_）
			String name = StringUtils.removeStart(key, "cmdstat_");
			// 提取调用次数（从格式为"calls=12,usec=120"中提取12）
			Integer value = Convert.toInt(StringUtils.substringBetween(property, "calls=", ",usec"));
			//加入列表
			commandStatItemList.add(new CommandStatItem(name, value));
		});

		//内存统计
		MemoryStatItem memoryStatItem = new MemoryStatItem(Convert.toDouble(0),Convert.toDouble(1000),Convert.toDouble(redisBasicInfo.getProperty("used_memory_human")));

		return success()
				.put("basicInfoRows",basicInfoItemList)
				.put("commandStats",commandStatItemList)
				.put("memoryStats",memoryStatItem);
	}

}