package com.supinfood.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
public class Lottery extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String topic;

    /**
     * 活动状态，1-上线，2-下线
     */
    private Integer state;

    private String link;

    private String images;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createTime;


}
