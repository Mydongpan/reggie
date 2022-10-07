package com.mayuan.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.mayuan.reggie.dto.DishDto;
import com.mayuan.reggie.entity.Dish;
import com.mayuan.reggie.entity.DishFlavor;
import com.mayuan.reggie.mapper.DishMapper;
import com.mayuan.reggie.service.DishFlavorService;
import com.mayuan.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServcieImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 添加基本信息到dish
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long disId = dishDto.getId(); //菜品id

        List<DishFlavor> flavors = dishDto.getFlavors(); //菜品口味
        flavors.stream().map((item) ->{
           item.setDishId(disId);
           return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);


    }

    /**
     * 根据id查询菜品和口味，并回显到浏览器
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
      //查询菜品基本信息，写入到对象中
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();

        BeanUtils.copyProperties(dish,dishDto);
        //查询到当前对应口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());

        List<DishFlavor> list = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(list);

        return dishDto;
    }

    /**
     * 更新菜品
     * @param dishDto
     */
    @Override
    @Transactional
    public void update(DishDto dishDto) {
        //更新基本信息
        this.updateById(dishDto);

        //清理当前菜品对应的口味数据
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getFlavors());
        dishFlavorService.remove(queryWrapper);

        //添加当前口味
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
