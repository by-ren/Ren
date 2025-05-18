package com.ren.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ren.common.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_notice")
public class Notice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;
    /**公告标题*/
    @TableField(value = "notice_title")
    private String noticeTitle;
    /**公告类型（1：通知 2：公告）*/
    @TableField(value = "notice_type")
    private String noticeType;
    /**公告内容*/
    @TableField(value = "notice_content")
    private String noticeContent;
    /**是否关闭（1：是 0：否）*/
    @TableField(value = "is_close")
    private Byte isClose;

}