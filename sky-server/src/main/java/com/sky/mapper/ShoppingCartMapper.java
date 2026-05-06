package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    //查询购物车
    List<ShoppingCart> list(ShoppingCart shoppingCart);
    //插入购物车
    @Update("insert into shopping_cart(user_id, dish_id, setmeal_id, name, image, amount, number,dish_flavor ,create_time) " +
            "values(#{userId}, #{dishId}, #{setmealId}, #{name}, #{image}, #{amount}, #{number}, #{dishFlavor},#{createTime})")
    void add(ShoppingCart shoppingCart);


    //更新购物车数量
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumber(ShoppingCart cart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void deleteByUserId(Long userId);
}
