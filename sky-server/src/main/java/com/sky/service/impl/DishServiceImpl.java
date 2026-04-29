package com.sky.service.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.aspectj.apache.bcel.classfile.ConstantNameAndType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Transactional
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入一条数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        //向口味表插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertBatch(flavors);

        }


    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void delete(List<Long> ids) {
        //1.查看菜品是否起售中
        for (Long id : ids) {
            if (dishMapper.selectById(id).getStatus()== StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }

        }

        //2.查看菜品是否处于套餐中
        for (Long id : ids) {
            if (setmealDishMapper.selectByDishId(id).size() > 0 && setmealDishMapper.selectByDishId(id)!=null ){
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
        }

        //3.删除菜品表信息
        //4，删除菜品口味表信息
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }


    }

    @Override
    public DishVO getByid(Long id) {
        //查询菜品基本数据
        Dish dish = dishMapper.selectById(id);
        //查询菜品口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.selectById(id);
        //封装
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavorMapper.selectById(id));
        return dishVO;
    }

    @Override
    public void update(DishDTO dishDTO) {
        //更新菜品表基本数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.update(dish);
        //删除原有口味数据
        dishFlavorMapper.deleteByDishId(dish.getId());
        //插入新口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);

        }
    }

    //根据分类id查询菜品
    @Override
    public List<Dish> selectByCategoryId(Long categoryId) {
        return dishMapper.selectByCategoryId(categoryId);

    }

}
