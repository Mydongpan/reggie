package com.mayuan.reggie.dto;

import com.mayuan.reggie.entity.Setmeal;
import com.mayuan.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
