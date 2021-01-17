package ${packageName};

<#list dependencies! as d>
    import ${d}.*;
</#list>
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Controller
public class IndexController {
    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/")
    public String index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return "index";
        }
        return "redirect:loginPage";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model) {
        List<String> roles = roleService.list().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
        model.addAttribute("roles", roles);
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Response<String> login(HttpSession session, @RequestBody LoginRequest request) {
        User user = userService.getOne(new QueryWrapper<User>()
            .eq("username", request.getUsername())
            .eq("password", request.getPassword())
            .eq("role", request.getRole()));
        session.setAttribute("user", user);
        return Response.ok();
    }
}
