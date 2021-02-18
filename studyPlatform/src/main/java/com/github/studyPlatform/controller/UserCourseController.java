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
@RequestMapping("/userCourse")
public class UserCourseController {
    @Autowired
    private UserCourseServiceImpl userCourseService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("/add")
    @ResponseBody
    public Response<UserCourse> add(@RequestBody UserCourse request) {
        UserCourse userCourse = userCourseService.add(request);
        return Response.ok(userCourse);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<UserCourse> update(@RequestBody UserCourse old) {
        UserCourse userCourse = userCourseService.update(old);
        return Response.ok(userCourse);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        userCourseService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<UserCourse> list(@RequestBody ListUserCourseRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return userCourseService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "userCourse"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/userCourse/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/userCourse/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        UserCourse userCourse = userCourseService.getById(id);
        model.addAttribute("userCourse", userCourse);
        return "page/userCourse/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        UserCourse userCourse = userCourseService.getById(id);
        model.addAttribute("userCourse", userCourse);
        return "page/userCourse/detail";
    }
}
