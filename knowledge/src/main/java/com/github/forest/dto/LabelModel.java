package com.github.forest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author sunzy
 * @date 2023/6/4 12:07
 */
@Data
public class LabelModel implements Serializable {

    private String label;

    private String value;

}
