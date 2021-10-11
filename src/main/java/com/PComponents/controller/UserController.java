package com.PComponents.controller;

import com.PComponents.model.User;
import com.PComponents.repository.RoleRepository;
import com.PComponents.service.ProductService;
import com.PComponents.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RoleRepository roleRepository;


    @GetMapping(value = {"/", "**/index"})
    public String index(Model model) {
        model.addAttribute("index");
        return "index";
    }

    @GetMapping(value = {"/login"})
    public String login(Model model) {
        model.addAttribute("login");
        return "login";
    }

    @GetMapping(value = "/register")
    public String registration(Model model, User user) {
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping(value = "/register")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "Există deja un cont care are asociat asteastă adresă de e-mail!");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("register");
        } else {
            userService.saveUser(user);
            model.addAttribute("successMessage", "Contul a fost creat cu succes!");
            model.addAttribute("user", new User());
        }
        return "register";
    }

    @GetMapping(value = "/admin/UserListAdminHome")
    public String adminHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("userName", "Bine ai venit, " + user.getUserName() + "!");
        model.addAttribute("adminMessage", "Aceasta pagină este accesibilă numai user-ilor cu rol de Admin.");
        List<User> list = userService.findAllUsers();
        model.addAttribute("listOfUsers", list);
        return "admin/UserListAdminHome";
    }

    @GetMapping(value = "/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") int id) throws Exception {
        userService.deleteUserById(id);
        return "redirect:/admin/UserListAdminHome";
    }

    @GetMapping(value = "/user/ProductsUserHome")
    public String userHome(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        model.addAttribute("userName", "Bine ai venit, " + user.getUserName() + "!");
        model.addAttribute("userMessage", "Aceasta pagină este accesibilă numai user-ilor cu rol de User.");
        model.addAttribute("productsList", productService.findAllProducts());
        return "user/ProductsUserHome";
    }

    @GetMapping("/profil")
    public String showUpdateForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + user1));
        model.addAttribute("user", user);
        return "profil";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") int id, @Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            user.setId(id);
            return "profil";
        }
        userService.saveUser(user);
        return "index";
    }

    @GetMapping("/makeAdmin/{id}")
    public String makeAdmin(@PathVariable("id") int id, @Valid User user, BindingResult result) {
        userService.makeAdmin(user);
        return "redirect:/admin/UserListAdminHome";
    }

    @GetMapping("/makeUser/{id}")
    public String makeUser(@PathVariable("id") int id, @Valid User user, BindingResult result) {
        userService.makeUser(user);
        return "redirect:/admin/UserListAdminHome";
    }
}