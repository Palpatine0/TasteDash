package com.wbq.controller.admin;

import com.wbq.entity.Unit;
import com.wbq.entity.R;
import com.wbq.service.IUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin/unit")
public class AdminUnitController {

    @Autowired
    private IUnitService unitService;

    @RequestMapping("/listAll")
    public R listAll() {
        List<Unit> list = unitService.list();
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("unitList", list);
        return R.ok(resultMap);
    }

    @PostMapping("/add")
    public R add(@RequestBody Unit unit) {
        unit.setValue(unit.getLabel());
        unitService.save(unit);
        return R.ok();
    }


}
