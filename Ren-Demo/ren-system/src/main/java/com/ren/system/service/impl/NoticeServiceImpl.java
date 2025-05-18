package com.ren.system.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ren.common.utils.PageUtils;
import com.ren.system.entity.Notice;
import com.ren.system.mapper.NoticeMapper;
import com.ren.system.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    /*
     * 添加通知公告
     * @param notice
     * @return int
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public long addNotice(Notice notice,String createBy) {
        notice.setCreateBy(createBy);
        notice.setCreateTime(DateUtil.currentSeconds());
        noticeMapper.insertNotice(notice);
        return notice.getNoticeId();
    }

    /*
     * 删除通知公告
     * @param noticeId
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public void removeNotice(long noticeId) {
        noticeMapper.deleteNotice(noticeId);
    }

    /*
     * 编辑通知公告
     * @param notice
     * @author admin
     * @date 2025/05/18 13:49
     */
    @Override
    public void modifyNotice(Notice notice,String updateBy) {
        notice.setUpdateBy(updateBy);
        notice.setUpdateTime(DateUtil.currentSeconds());
        noticeMapper.updateNotice(notice);
    }

    /*
     * 分页获取通知公告列表
     * @param paramMap
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.ren.system.entity.Notice>
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public IPage<Notice> listNoticeByPage(Map<String, Object> paramMap) {
        if(paramMap != null && paramMap.containsKey("searchLike") && StrUtil.isNotBlank(Convert.toStr(paramMap.get("searchLike")))){
            paramMap.put("searchLike", StrUtil.format("%%{}%%",paramMap.get("searchLike")));
        }
        IPage<Notice> noticeList = noticeMapper.listNoticeByPage(PageUtils.createPage(Notice.class),paramMap);
        return noticeList;
    }

    /*
     * 获取通知公告详情
     * @param noticeId
     * @return com.ren.system.entity.Notice
     * @author admin
     * @date 2025/05/18 13:50
     */
    @Override
    public Notice getNoticeById(long noticeId) {
        Notice notice = noticeMapper.selectById(noticeId);
        return notice;
    }
}