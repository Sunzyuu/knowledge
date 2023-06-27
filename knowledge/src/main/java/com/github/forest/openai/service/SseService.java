package com.github.forest.openai.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @author sunzy
 * @date 2023/6/27 12:37
 */
public interface SseService {
    SseEmitter connect(Long idUser);

    boolean send(Long idUser, String content);

    void close(Long idUser);
}
