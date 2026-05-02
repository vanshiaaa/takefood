package com.sky.controller.admin;

import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;
    //设置店铺营业状态
    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        log.info("设置店铺营业状态：{}",status == 1 ? "营业中" : "打烊了");
        //将店铺营业状态保存到redis中
        redisTemplate.opsForValue().set("shop:status",status);
        return Result.success();
    }
    //查询店铺营业状态
    @GetMapping("/status")
    public Result getShopStatus(){

        //从redis中获取店铺营业状态
        Integer status = (Integer) redisTemplate.opsForValue().get("shop:status");
        log.info("查询店铺营业状态为：{}",status == 1 ? "营业中" : "打烊了");
        return Result.success(status);
    }

}
