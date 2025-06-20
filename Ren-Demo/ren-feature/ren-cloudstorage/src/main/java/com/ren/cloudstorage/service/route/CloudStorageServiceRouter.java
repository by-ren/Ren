package com.ren.cloudstorage.service.route;

import com.ren.cloudstorage.service.CloudStorageService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 该类主要用于根据不同的业务类型选择对应的服务
 * @author ren
 * @date 2025/06/20 10:02
 */
@Service
public class CloudStorageServiceRouter {

	// 注册所有实现（假设已通过Spring注入）
	private final Map<String, CloudStorageService> serviceMap;

	public CloudStorageServiceRouter(List<CloudStorageService> services) {
		// 构建业务类型->服务映射
		this.serviceMap = services.stream()
				.collect(Collectors.toMap(
						CloudStorageService::getServiceType,
						Function.identity()
				));
	}

	/**
	 * 获取对应业务的存储服务
	 * @param cloudStorageVendors 业务分类
	 * @return 存储服务实例
	 */
	public CloudStorageService getService(String cloudStorageVendors) {
		return serviceMap.getOrDefault(cloudStorageVendors, getDefaultService());
	}

	/**
	 * 获取默认的存储服务
	 * @return com.ren.cloudstorage.service.CloudStorageService
	 * @author ren
	 * @date 2025/06/20 10:24
	 */
	private CloudStorageService getDefaultService() {
		// 返回默认服务（如阿里云）
		CloudStorageService service = serviceMap.get("aliyun");
		if (service == null) {
			throw new IllegalStateException("默认云存储服务(aliyun)未注册");
		}
		return service;
	}


}