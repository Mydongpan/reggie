package com.mayuan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mayuan.reggie.dto.DishDto;
import com.mayuan.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //添加菜品和口味
    void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品和口味，并回显到浏览器
    DishDto getByIdWithFlavor(Long id);
    //更新菜品
    void update(DishDto dishDto);

}
