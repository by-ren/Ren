package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.core.domain.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_post")
public class Post extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 岗位ID */
    @TableId(value = "post_id", type = IdType.AUTO)
    private Long postId;
    /** 岗位编码 */
    @TableField(value = "post_code")
    private String postCode;
    /** 岗位名称 */
    @TableField(value = "post_name")
    private String postName;
    /** 显示顺序 */
    @TableField(value = "post_sort")
    private Integer postSort;
    /** 是否停用 */
    @TableField(value = "is_stop")
    private Byte isStop;

}