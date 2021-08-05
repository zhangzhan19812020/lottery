package com.supinfood;

import com.supinfood.mapper.SysUserMapper;
import com.supinfood.model.SysUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootTest
class LotteryApplicationTests {

    @Resource
    SysUserMapper mapper;

    @Resource
    PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
        List<SysUser> sysUsers = mapper.selectList(null);
        sysUsers.forEach(System.out::println);
    }

    @Test
    void contextLoads2() {
        System.out.println(passwordEncoder.encode("111111"));
    }

    @Test
    void contextLoads3() {
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(dtf.format(time));
    }

    @Test
    void contextLoads4() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = "2009-09-19 14:34:23";
        LocalDateTime time = LocalDateTime.parse(timeStr,dtf);

        System.out.println("time=" + time);
    }

//    @Test
//    void contextLoads5() {
//        LocalDateTime time = LocalDateTime.now();
//        System.out.println("time=" + time);
//        org.joda.time.format.DateTimeFormatter dtf = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//        System.out.println("formatted_time=" + );
//    }

    @Test
    void contextLoads6() {
        org.joda.time.format.DateTimeFormatter dtf = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        String timeStr = "2009-09-19 14:34:23";
        org.joda.time.LocalDateTime time = dtf.parseLocalDateTime(timeStr);
        System.out.println("time=" + time);
    }

    @Test
    void testShuffle() {
        List<Integer> myList = new ArrayList(6);
        for(int i = 0; i < 6; i++) {
            myList.add(i+1);
        }
        System.out.println(myList);
        Collections.shuffle(myList);
        System.out.println(myList);
    }

    @Test
    void contextLoads8() {
        int MULTIPLE = 2;
        BigDecimal p = BigDecimal.valueOf(0.02);
        System.out.println(new BigDecimal(p.floatValue()).multiply(new BigDecimal(MULTIPLE)).intValue());
        int luckyNumber = new Random().nextInt(MULTIPLE);
        System.out.println(luckyNumber);


    }

}
