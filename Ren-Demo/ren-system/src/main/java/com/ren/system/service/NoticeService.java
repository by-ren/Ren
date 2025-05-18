
package com.ren.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ren.system.entity.Notice;

import java.util.Map;

public interface NoticeService extends IService<Notice> {

    /*
     * 添加通知公告
     * @param notice
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    long addNotice(Notice notice,String createBy);

    /*
     * 删除通知公告
     * @param noticeId
     * @author admin
     * @date 2025/05/18 13:49
     */
    void removeNotice(long noticeId);

    /*
     * 编辑通知公告
     * @param notice
     * @author admin
     * @date 2025/05/18 13:49
     */
    void modifyNotice(Notice notice,String updateBy);

    /*
     * 分页获取通知公告列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Notice>
     * @author admin
     * @date 2025/05/18 13:50
     */
    IPage<Notice> listNoticeByPage(Map<String,Object> paramMap);

    /*
     * 获取通知公告详情
     * @param noticeId
     * @return com.ren.system.entity.Notice
     * @author admin
     * @date 2025/05/18 13:50
     */
    Notice getNoticeById(long noticeId);
}