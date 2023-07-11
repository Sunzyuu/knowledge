package com.github.forest.web.api.bank;

import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.BankDTO;
import com.github.forest.service.BankService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/7/10 18:26
 */
@RestController
@RequestMapping("/api/v1/admin/bank")
public class BankController {

    @Resource
    private BankService bankService;

    @GetMapping("/list")
    public GlobalResult<PageInfo<BankDTO>> banks(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<BankDTO> banks = bankService.findBanks();
        PageInfo<BankDTO> pageInfo = new PageInfo<>(banks);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }
}
