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
@RequestMapping("/courseAssistant")
public class CourseAssistantController {
    @Autowired
    private CourseAssistantServiceImpl courseAssistantService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("/add")
    @ResponseBody
    public Response<CourseAssistant> add(@RequestBody CourseAssistant request) {
        CourseAssistant courseAssistant = courseAssistantService.add(request);
        return Response.ok(courseAssistant);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<CourseAssistant> update(@RequestBody CourseAssistant old) {
        CourseAssistant courseAssistant = courseAssistantService.update(old);
        return Response.ok(courseAssistant);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        courseAssistantService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<CourseAssistant> list(@RequestBody ListCourseAssistantRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return courseAssistantService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "courseAssistant"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/courseAssistant/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/courseAssistant/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        CourseAssistant courseAssistant = courseAssistantService.getById(id);
        model.addAttribute("courseAssistant", courseAssistant);
        return "page/courseAssistant/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        CourseAssistant courseAssistant = courseAssistantService.getById(id);
        model.addAttribute("courseAssistant", courseAssistant);
        return "page/courseAssistant/detail";
    }
}
