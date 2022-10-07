package com.mayuan.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mayuan.reggie.dto.SetmealDto;
import com.mayuan.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 根据ids删除套餐
     * @param ids
     */
    void removeWithDish(List<Long> ids);

    /**
     * 修改时回显套餐和菜品
     * @param id
     */
    SetmealDto getByIdWithDish(Long id);

    /**
     * 更新菜品
     */
    void update(SetmealDto setmealDto);
}
