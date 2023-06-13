package com.github.forest.handler.event;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author sunzy
 * @date 2023/6/12 17:35
 */
@Data
@AllArgsConstructor
public class FollowEvent {

    private Long followingId;

    private Long idFollow;

    private String summary;
}