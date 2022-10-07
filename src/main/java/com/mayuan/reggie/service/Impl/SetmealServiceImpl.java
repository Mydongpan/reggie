package com.mayuan.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayuan.reggie.common.CustomException;
import com.mayuan.reggie.dto.SetmealDto;

import com.mayuan.reggie.entity.Setmeal;
import com.mayuan.reggie.entity.SetmealDish;
import com.mayuan.reggie.mapper.SetmealMapper;

import com.mayuan.reggie.service.SetmealDishService;
import com.mayuan.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

//    @Autowired
//    private CategoryService categoryService;

    /**
     * 新增套餐
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存基本信息到对象中
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item) ->{
            item.setSetmealId(setmealDto.getId());

            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品关系
        setmealDishService.saveBatch(setmealDishes);

    }
    /**
     * 根据ids删除套餐
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐是否为可用删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if (count > 0){
            //如果不能删除，抛出异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //删除套餐
        this.removeByIds(ids);
        //删除套餐关联菜品信息 select * from setmeal_dish where setmeal_id in (ids.)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getDishId,ids);

        setmealDishService.remove(lambdaQueryWrapper);
    }

    /**
     * 修改时回显套餐和菜品
     * @param id
     */
    @Override

    public SetmealDto getByIdWithDish(Long id) {//Select * from setmeal

        //将基本信息复制到SermealDto中
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();

        BeanUtils.copyProperties(setmeal,setmealDto);
//        //获取菜品分类
//        Category category = categoryService.getById(setmeal.getCategoryId());
//        String categoryName = category.getName();
//        setmealDto.setCategoryName(categoryName);
//
//        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(SetmealDish::getSetmealId,id);
//        setmealDishService.removeById(queryWrapper);
        //根据套餐id，查询菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,setmeal.getId());

        List<SetmealDish> list = setmealDishService.list(queryWrapper);
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 更新菜品
     * @param setmealDto
     */
    @Override
    public void update(SetmealDto setmealDto) {
        //更新基本数据
        this.updateById(setmealDto);

        //删除原表中的套餐菜品数据
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getSetmealDishes());
        setmealDishService.remove(setmealDishLambdaQueryWrapper);

        //重新套餐菜品数据
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        dishList.stream().map((item) ->{
           item.setSetmealId(setmealDto.getId());
           return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(dishList);
    }
}
