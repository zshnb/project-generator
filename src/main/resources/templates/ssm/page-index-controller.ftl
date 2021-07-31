package ${packageName};

<#list dependencies! as d>
    import ${d}.*;
</#list>
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
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
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            return new ModelAndView("index");
        }
        return new ModelAndView("login");
    }

    @GetMapping("/loginPage")
    public ModelAndView loginPage() {
        List<String> roles = roleService.list().stream()
            .map(Role::getName)
            .collect(Collectors.toList());
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("roles", roles);
        return modelAndView;
    }

    @GetMapping("/registerPage")
    public ModelAndView registerPage() {
        return new ModelAndView("register");
    }

    /**
        登录接口
    */
    @PostMapping("/login")
    @ResponseBody
    public Response<String> login(HttpSession session, @RequestBody LoginRequest request) {
        User user = userService.getOne(new QueryWrapper<User>()
            .eq("username", request.getUsername())
            .eq("password", request.getPassword())
            .eq("role", request.getRole()));
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        session.setAttribute("user", user);
        return Response.ok();
    }

    <#list unBindMenus as menu>
    @GetMapping("${menu.href}")
    public ModelAndView ${menu.href?substring(1)}() {
        return new ModelAndView("page/${menu.href?substring(1)}");
    }
    </#list>
}
