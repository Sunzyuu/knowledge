package com.github.forest.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 赞赏表 
 * </p>
 *
 * @author sunzy
 * @since 2023-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("forest_sponsor")
public class Sponsor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据主键
     */
    private Long dataId;

    /**
     * 赞赏人
     */
    private Long sponsor;

    /**
     * 赞赏日期
     */
    private LocalDateTime sponsorshipTime;

    /**
     * 赞赏金额
     */
    private BigDecimal sponsorshipMoney;


}
