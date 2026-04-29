package com.sky.mapper;

import com.sky.anno.Autofill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<Long> selectByDishId(Long id);


    void insertSetmealDishBatch(List<SetmealDish> setmealDishes);
}
