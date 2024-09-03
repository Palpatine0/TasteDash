package com.wbq.controller.admin;

import com.wbq.entity.Dish;
import com.wbq.entity.PageBean;
import com.wbq.entity.R;
import com.wbq.service.IDishService;
import com.wbq.util.DateUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/dish")
public class AdminDishController {

    @Autowired
    private IDishService dishService;

    @Value("${dishImgsFilePath}")
    private String dishImgsFilePath;

    @RequestMapping("/list")
    public R list(@RequestBody PageBean pageBean) {
        System.out.println(pageBean);
        Map<String, Object> map = new HashMap<>();
        map.put("start", pageBean.getStart());
        map.put("pageSize", pageBean.getPageSize());
        List<Dish> list = dishService.list(map);
        Long total = dishService.getTotal(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("dishList", list);
        resultMap.put("total", total);
        return R.ok(resultMap);
    }

    @PostMapping("/save")
    public R save(@RequestBody Dish dish) {
        dish.setTime(new Date());
        if (dish.getId() != null) {
            dishService.updateById(dish);
        } else {
            dish.setOnsale(true);
            dishService.save(dish);
        }
        return R.ok();
    }

    @GetMapping("/delete")
    public R delete(Integer id) {
        dishService.removeById(id);
        return R.ok();
    }

    @PostMapping("/updateOnSale")
    public R updateOnSale(@RequestBody Dish dish) {
        Dish d = dishService.getById(dish.getId());
        d.setOnsale(dish.isOnsale());
        dishService.updateById(d);
        return R.ok();
    }

    @RequestMapping("/uploadImage")
    public Map<String, Object> uploadImage(MultipartFile file) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            String newFileName = DateUtil.getCurrentDateStr() + suffixName;

            FileUtils.copyInputStreamToFile(file.getInputStream(), new File(dishImgsFilePath + newFileName));
            map.put("code", 0);
            map.put("msg", "上传成功");
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("imageName", newFileName);
            map2.put("src", "/image/dish/" + newFileName);
            map.put("data", map2);
        }
        return map;
    }

}
