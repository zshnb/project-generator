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
@RequestMapping("/attachment")
public class AttachmentController {
    @Autowired
    private AttachmentServiceImpl attachmentService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("/add")
    @ResponseBody
    public Response<Attachment> add(@RequestBody Attachment request) {
        Attachment attachment = attachmentService.add(request);
        return Response.ok(attachment);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<Attachment> update(@RequestBody Attachment old) {
        Attachment attachment = attachmentService.update(old);
        return Response.ok(attachment);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        attachmentService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<Attachment> list(@RequestBody ListAttachmentRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return attachmentService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "attachment"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/attachment/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/attachment/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        Attachment attachment = attachmentService.getById(id);
        model.addAttribute("attachment", attachment);
        return "page/attachment/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        Attachment attachment = attachmentService.getById(id);
        model.addAttribute("attachment", attachment);
        return "page/attachment/detail";
    }
}
