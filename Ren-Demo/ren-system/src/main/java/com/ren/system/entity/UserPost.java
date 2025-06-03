package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_post")
public class UserPost {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableField(value = "user_id")
    private Long userId;
    /** 岗位ID */
    @TableField(value = "post_id")
    private Long postId;

}