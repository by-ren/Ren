package com.ren.cloudstorage.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true) // 开启链式调用
public class ImageLog {
	/** 主键ID */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/** OSS存储桶名称 */
	@TableField(value = "bucket")
	private String bucket;
	/** OSS返回的ETag */
	@TableField(value = "etag")
	private String etag;
	/** 文件名 */
	@TableField(value = "name")
	private String name;
	/** 文件大小(字节) */
	@TableField(value = "file_size")
	private Long fileSize;
	/** 文件MIME类型 */
	@TableField(value = "mime_type")
	private String mimeType;
	/** 文件访问URL */
	@TableField(value = "image_url")
	private String imageUrl;
	/** 云服务名称 */
	@TableField(value = "cloud_name")
	private String cloudName;
	/** 业务归属分类 */
	@TableField(value = "belong")
	private String belong;
	/** 创建时间 (秒时间戳) */
	@TableField(value = "create_time")
	private Long createTime;

	/*==================================================以下为冗余字段===================================================*/
	/** 创建时间Str */
	@TableField(exist = false)
	private String createTimeStr;

}