package com.ren.admin.controller.monitor;

import com.ren.common.constant.RedisCacheConstants;
import com.ren.common.controller.BaseController;
import com.ren.common.domain.model.dto.AjaxResultDTO;
import com.ren.common.utils.RedisCacheUtils;
import com.ren.monitor.domain.vo.CacheKeyVO;
import com.ren.monitor.domain.vo.CacheVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/monitor/cache")
@Slf4j
public class CacheController extends BaseController {

	@Autowired
	private RedisCacheUtils redisCacheUtils;

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
		try(Cursor<String> cursor = redisCacheUtils.scan(pattern)){
			while (cursor.hasNext()) {
				String key = cursor.next();
				// 获取键对应的值（JWT Token）
				String refreshToken = redisCacheUtils.getCacheObject(key);
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
		redisCacheUtils.deleteKeysByPattern(name + ":" + "*");
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
		redisCacheUtils.deleteObject(name + ":" + key);
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
		redisCacheUtils.deleteKeysByPattern("*");
		return success();
	}

}