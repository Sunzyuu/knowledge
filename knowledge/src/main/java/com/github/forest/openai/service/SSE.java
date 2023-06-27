package com.github.forest.openai.service;

/**
 * @author sunzy
 * @date 2023/6/27 12:36
 */
public class SSE {
    private static final String DONE_DATA = "[DONE]";

    private final String data;

    public SSE(String data){
        this.data = data;
    }

    public String getData(){
        return this.data;
    }

    public byte[] toBytes(){
        return String.format("data: %s\n\n", this.data).getBytes();
    }

    public boolean isDone(){
        return DONE_DATA.equalsIgnoreCase(this.data);
    }
}

