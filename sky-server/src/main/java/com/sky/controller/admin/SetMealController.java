package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("添加套餐：{}",setmealDTO);
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    //套餐分页查询
    @GetMapping("/page")
    public Result<PageResult> pageQuary(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("套餐分页查询：{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    //删除套餐
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("删除套餐：{}",ids);
        setmealService.delete(ids);
        return Result.success();
    }
    //查询回显
    @GetMapping("/{id}")
    public Result<SetmealVO> getByid(@PathVariable Long id){
        log.info("查询回显：{}",id);
        SetmealVO setmealVO =setmealService.getByid(id);
        return Result.success(setmealVO);

    }
    //修改菜品
    @PutMapping()
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改菜品：{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }

    //修改起售状态
        @PostMapping("/status/{status}")
        public Result updateStatus(@PathVariable Integer status,Long id){
            log.info("修改起售状态：{}，{}",status,id);
            setmealService.updateStatus(status,id);
            return Result.success();
        }
}