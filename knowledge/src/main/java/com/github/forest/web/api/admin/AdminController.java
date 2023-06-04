package com.github.forest.web.api.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.forest.core.exception.ServiceException;
import com.github.forest.core.result.GlobalResult;
import com.github.forest.core.result.GlobalResultGenerator;
import com.github.forest.dto.UserInfoDTO;
import com.github.forest.dto.UserSearchDTO;
import com.github.forest.dto.admin.UserRoleDTO;
import com.github.forest.entity.Role;
import com.github.forest.entity.Tag;
import com.github.forest.entity.Topic;
import com.github.forest.entity.User;
import com.github.forest.service.AdminService;
import com.github.forest.service.RoleService;
import com.github.forest.service.TagService;
import com.github.forest.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author sunzy
 * @date 2023/5/31 21:50
 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private TagService tagService;

    @GetMapping("/topics")
    public GlobalResult<PageInfo<Topic>> topics(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        return adminService.getTopics(page, rows);
    }

    @GetMapping("/users")
    public GlobalResult<PageInfo<UserInfoDTO>> users(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, UserSearchDTO searchDTO) {
        return adminService.getUsers(page, rows, searchDTO);
    }

    @GetMapping("/users/{idUser}/role")
    public GlobalResult<List<Role>> userRole(@PathVariable Long idUser) {
        List<Role> roles = roleService.findByIdUser(idUser);
        return GlobalResultGenerator.genSuccessResult(roles);
    }

    @GetMapping("/roles")
    public GlobalResult<PageInfo<Role>> roles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Role> roles = roleService.list();
        PageInfo<Role> pageInfo = new PageInfo<>(roles);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/user/update-role")
    public GlobalResult<Boolean> updateUserRole(@RequestBody UserRoleDTO userRole) throws ServiceException {
        boolean flag = userService.updateUserRole(userRole.getIdUser(), userRole.getIdRole());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/user/update-status")
    public GlobalResult<Boolean> updateUserStatus(@RequestBody User user) throws ServiceException {
        boolean flag = userService.updateStatus(user.getId(), user.getStatus());
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PostMapping("/role/update-status")
    public GlobalResult<Boolean> updateRoleStatus(@RequestBody Role role) throws ServiceException{
        boolean flag = roleService.updateStatus(role.getIdRole(), role.getStatus());
        return GlobalResultGenerator.genSuccessResult(flag);
    }


    @PostMapping("/role/post")
    public GlobalResult<Boolean> addRole(@RequestBody Role role) throws ServiceException {
        boolean flag = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PutMapping("/role/post")
    public GlobalResult<Boolean> updateRole(@RequestBody Role role) throws ServiceException {
        boolean flag = roleService.saveRole(role);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @GetMapping("/tag/detail/{idTag}")
    public GlobalResult<Tag> tagDetail(@PathVariable Integer idTag){
        Tag tag = tagService.getById(idTag);
        return GlobalResultGenerator.genSuccessResult(tag);
    }


}
