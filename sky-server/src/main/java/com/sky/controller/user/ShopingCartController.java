package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
public class ShopingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    //添加购物车
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车商品为：{}",shoppingCartDTO);
        shoppingCartService.add(shoppingCartDTO);
        return Result.success();
    }
}
