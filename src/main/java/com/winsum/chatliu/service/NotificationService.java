package com.winsum.chatliu.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winsum.chatliu.dto.NotificationDTO;
import com.winsum.chatliu.dto.PageResultDTO;
import com.winsum.chatliu.enums.NotificationStatusEnum;
import com.winsum.chatliu.enums.NotificationTypeEnum;
import com.winsum.chatliu.exception.CustomizeErrorCode;
import com.winsum.chatliu.exception.CustomizeException;
import com.winsum.chatliu.mapper.NotificationMapper;
import com.winsum.chatliu.mapper.UserMapper;
import com.winsum.chatliu.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {


    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 列出所有通知
     * @param id
     * @param page
     * @param size
     * @return
     */
    public PageResultDTO<NotificationDTO> list(Integer id, Integer page, Integer size) {

        PageHelper.startPage(page, size);

        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id.longValue());
        notificationExample.setOrderByClause("gmt_create desc");
        List<Notification> notifications = notificationMapper.selectByExample(notificationExample);

        //得到分页的结果
        PageInfo<Notification> notificationPageInfo = new PageInfo<>(notifications);

        ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();
        if (notificationPageInfo.getList() != null && notificationPageInfo.getList().size() > 0) // list 不为空
        {
            for (Notification notification : notificationPageInfo.getList()) {
                NotificationDTO notificationDTO = new NotificationDTO();
                BeanUtils.copyProperties(notification, notificationDTO);
                notificationDTO.setTypeName(NotificationTypeEnum.getNameByType(notification.getType()));
                notificationDTOS.add(notificationDTO);
            }
        }
        return new PageResultDTO<>(notificationPageInfo.getTotal(), notificationPageInfo.getPages(), notificationPageInfo.getPageNum(), notificationDTOS);
    }

    /**
     * 通知消息已经
     * @param id
     * @param user
     * @return
     */
    public NotificationDTO read(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if (notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }

        if (!Objects.equals(notification.getReceiver(),user.getId().longValue())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }


        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        NotificationDTO notificationDTO = new NotificationDTO();
        BeanUtils.copyProperties(notification, notificationDTO);
        notificationDTO.setTypeName(NotificationTypeEnum.getNameByType(notification.getType()));

        return notificationDTO;
    }


    /**
     * 未读的数量
     * @param userId
     * @return
     */
    public Long unreadCount(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }
}
