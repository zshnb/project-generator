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
@RequestMapping("/chapterAttachment")
public class ChapterAttachmentController {
    @Autowired
    private ChapterAttachmentServiceImpl chapterAttachmentService;

    @Autowired
    private PermissionServiceImpl permissionService;

    @PostMapping("/add")
    @ResponseBody
    public Response<ChapterAttachment> add(@RequestBody ChapterAttachment request) {
        ChapterAttachment chapterAttachment = chapterAttachmentService.add(request);
        return Response.ok(chapterAttachment);
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<ChapterAttachment> update(@RequestBody ChapterAttachment old) {
        ChapterAttachment chapterAttachment = chapterAttachmentService.update(old);
        return Response.ok(chapterAttachment);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        chapterAttachmentService.delete(id);
        return Response.ok();
    }

    @PostMapping("/list")
    @ResponseBody
    public ListResponse<ChapterAttachment> list(@RequestBody ListChapterAttachmentRequest request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return chapterAttachmentService.list(request);
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "chapterAttachment"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/chapterAttachment/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/chapterAttachment/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        ChapterAttachment chapterAttachment = chapterAttachmentService.getById(id);
        model.addAttribute("chapterAttachment", chapterAttachment);
        return "page/chapterAttachment/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        ChapterAttachment chapterAttachment = chapterAttachmentService.getById(id);
        model.addAttribute("chapterAttachment", chapterAttachment);
        return "page/chapterAttachment/detail";
    }
}
