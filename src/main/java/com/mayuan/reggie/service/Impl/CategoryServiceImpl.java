package com.mayuan.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mayuan.reggie.common.CustomException;
import com.mayuan.reggie.entity.Category;
import com.mayuan.reggie.entity.Dish;
import com.mayuan.reggie.entity.Setmeal;
import com.mayuan.reggie.mapper.CategoryMapper;
import com.mayuan.reggie.service.CategoryService;
import com.mayuan.reggie.service.DishService;
import com.mayuan.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;





    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id){
        LambdaQueryWrapper<Dish> dislambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        dislambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dislambdaQueryWrapper);

        //查询时关联菜
        if (count1 > 0){
            throw new CustomException("当前分类下关联了菜，不能删除");
        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();

        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            throw new CustomException("已经关联了套餐，不能删除");
        }

        super.removeById(id);
    }
}
