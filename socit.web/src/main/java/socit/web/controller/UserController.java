package socit.web.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import socit.domain.entity.Friend;
import socit.domain.entity.Post;
import socit.domain.entity.User;
import socit.domain.repository.PostRepository;
import socit.service.FriendService;
import socit.service.PhotoPostService;
import socit.service.PostService;
import socit.service.UserService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

@Controller
@Log4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private PhotoPostService photoPostService;

    @Autowired
    private PostService postService;

    @Autowired
    private FriendService friendService;

    @Transactional
    @RequestMapping(value = "/user/home")
    public ModelAndView toUserWall() {
        log.debug("request: /user/home");
        log.debug("Get user by authentication user");
        User userByAuthentication = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("Get user by id = " + userByAuthentication.getId());
        User user = userService.getById(userByAuthentication.getId());
        ModelAndView modelAndView = new ModelAndView("user_wall");
        if (user.getStatus()) {
            modelAndView.addObject("user", user);
            Calendar date = user.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            if (date!=null) {
                modelAndView.addObject("date", formatter.format(date.getTime()));
            }
            List<Post> listPost = new ArrayList<>();
            List<Post> lists = user.getPosts();
            if (lists != null) {
                for (Post list : lists) {
                    if (list.getStatus()) {
                        listPost.add(list);
                    }
                }
            }
            Collections.reverse(listPost);
            modelAndView.addObject("list", listPost);
        } else {
            modelAndView.setViewName("/login");
            modelAndView.addObject("error", "You have not confirmed registration via email");
        }

        return modelAndView;
    }

    @RequestMapping(value = "/uploadFile")
    public String uploadImage(@RequestParam("description") String description,
                              @RequestParam(value = "all", required = false) String[] allUser,
                              @RequestParam("file") MultipartFile[] myFile) {
        if (description!=null&&myFile[0].getSize()>0){
            try {
                photoPostService.savePhotoPost(description, allUser, myFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "redirect:/user/home";
    }

    @RequestMapping(value = "/post/delete")
    public String postDelete(@RequestParam(value = "id") Integer id) {
        Post post = postService.getById(id);
        post.setStatus(false);
        postService.update(post);
        return "redirect:/user/home";
    }

    @RequestMapping(value = "/post/edit")
    public String postEdit(@RequestParam(value = "id") Integer id,
                           @RequestParam(value = "comment") String comment) {
       Post post =  postService.getById(id);
       post.setComment(comment);
       postService.update(post);
        return "redirect:/user/home";
    }
}
