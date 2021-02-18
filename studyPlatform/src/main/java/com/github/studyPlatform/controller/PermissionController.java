package com.github.studyPlatform.controller;

import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.dto.*;
import com.github.studyPlatform.serviceImpl.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionServiceImpl permissionService;


    @PostMapping("/add")
    @ResponseBody
    public Response<Permission> add(@RequestBody Permission request) {
        Permission permission = permissionService.add(request);
        return Response.ok(permission);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<Permission> update(@RequestBody Permission old) {
        Permission permission = permissionService.update(old);
        return Response.ok(permission);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        permissionService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<Permission> list(@RequestBody ListPermissionRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return permissionService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "permission"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/permission/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/permission/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission", permission);
        return "page/permission/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission", permission);
        return "page/permission/detail";
    }
}
