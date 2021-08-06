package com.supinfood.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.supinfood.model.LotteryRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author mic
 * @since 2021-08-03
 */
public interface LotteryRecordMapper extends BaseMapper<LotteryRecord> {
    @Select("select count(id) from lottery_record where  account_ip=#{accountIp}")
    int SelectCountByAccountIp(@Param("accountIp") String accountIp);
}
