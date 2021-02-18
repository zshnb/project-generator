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
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamServiceImpl examService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("/add")
    @ResponseBody
    public Response<Exam> add(@RequestBody Exam request) {
        Exam exam = examService.add(request);
        return Response.ok(exam);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<Exam> update(@RequestBody Exam old) {
        Exam exam = examService.update(old);
        return Response.ok(exam);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        examService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<Exam> list(@RequestBody ListExamRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return examService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "exam"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/exam/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/exam/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        Exam exam = examService.getById(id);
        model.addAttribute("exam", exam);
        return "page/exam/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        Exam exam = examService.getById(id);
        model.addAttribute("exam", exam);
        return "page/exam/detail";
    }
}
