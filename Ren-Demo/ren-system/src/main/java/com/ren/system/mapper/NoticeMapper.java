
package com.ren.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ren.system.entity.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {

    /*
     * 添加通知公告
     * @param notice
     * @return int
     * @author ren
     * @date 2025/05/18 13:49
     */
    void insertNotice(Notice notice);

    /*
     * 删除通知公告
     * @param noticeId
     * @author ren
     * @date 2025/05/18 13:49
     */
    void deleteNotice(long noticeId);

    /*
     * 编辑通知公告
     * @param notice
     * @author ren
     * @date 2025/05/18 13:49
     */
    void updateNotice(Notice notice);

    /*
     * 分页获取通知公告列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Notice>
     * @author ren
     * @date 2025/05/18 13:50
     */
    IPage<Notice> listNoticeByPage(Page<Notice> page, @Param("paramMap")Map<String, Object> paramMap);

}