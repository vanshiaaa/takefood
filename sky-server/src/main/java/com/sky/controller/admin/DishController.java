package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    //新增菜品
    @PostMapping()
    public Result add(@RequestBody  DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO);

        dishService.saveWithFlavor(dishDTO);
        String key = "dish_" + dishDTO.getCategoryId();
        cleanCache(key);

        return Result.success();
    }
    //菜品分页查询
    @GetMapping("/page")
    public Result<PageResult> pageQuary(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询：{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);

    }


    //删除菜品
    @DeleteMapping()
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除菜品：{}",ids);
        dishService.delete(ids);
        cleanCache("dish_*");
        return Result.success();
    }
    //查询回显
    @GetMapping("/{id}")
    public Result<DishVO> getByid(@PathVariable Long id){
        log.info("查询回显：{}",id);
        DishVO dishVO =dishService.getByid(id);
        return Result.success(dishVO);

    }
    //修改菜品
    @PutMapping()
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }
    //根据分类id查询菜品
    @GetMapping("/list")
    public Result<List<Dish>> select(Long categoryId){
        log.info("根据分类id查询菜品：{}",categoryId);
        List<Dish> dish = dishService.selectByCategoryId(categoryId);
        return  Result.success(dish);

    }
    //修改菜品状态
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id){
        log.info("修改菜品状态：{}，id：{}",status,id);
        dishService.updateStatus(status,id);
        cleanCache("dish_*");
        return Result.success();
    }
    //清理缓存
    private  void cleanCache(String pattern){
        Set Keys = redisTemplate.keys(pattern);
        redisTemplate.delete(Keys);
    }

}
