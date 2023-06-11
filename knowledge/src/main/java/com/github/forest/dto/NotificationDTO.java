package com.github.forest.dto;

import com.github.forest.entity.Notification;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sunzy
 * @date 2023/6/11 17:32
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotificationDTO extends Notification {

    private Long idNotification;

    private String dataTitle;

    private String dataUrl;

    private Author author;

}

