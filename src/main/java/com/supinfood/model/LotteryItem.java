package com.supinfood.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LotteryItem extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 活动id
     */
    private Integer lotteryId;

    /**
     * 奖项名称
     */
    private String itemName;

    /**
     * 奖项等级
     */
    private Integer level;

    /**
     * 奖项概率
     */
    private BigDecimal percent;

    /**
     * 奖品id
     */
    private Integer prizeId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否是默认的奖项, 0-不是 ， 1-是
     */
    private Integer defaultItem;


}
