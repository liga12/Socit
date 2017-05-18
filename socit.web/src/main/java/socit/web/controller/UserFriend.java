package socit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import socit.domain.entity.Friend;
import socit.domain.entity.User;
import socit.domain.enums.FRIENDSTATUS;
import socit.domain.repository.FriendRepository;
import socit.service.FriendService;
import socit.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class UserFriend {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Transactional
    @RequestMapping(value = "/user/friends")
    public ModelAndView toFriends() {
        ModelAndView modelAndView = new ModelAndView("friends");
        modelAndView.addObject("user", userService.getUserByPrincpals());
        List<Friend> friends = friendService.getFriendsByUser(userService.getUserByPrincpals());
        modelAndView.addObject("friends", friends);
        List<Friend> applicationFriends = friendService.getFriendsByFriendAndFriendstatus(userService.getUserByPrincpals(),
                FRIENDSTATUS.WAIT);
        if (applicationFriends.size() > 0) {
            modelAndView.addObject("applicationsFriends", applicationFriends);
            modelAndView.addObject("menuFriends", applicationFriends.size());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/searchFriends")
    public ModelAndView searchFriends(@RequestParam(value = "search") String search) {
        ModelAndView modelAndView = new ModelAndView("friends");
        User userByPrincipals = userService.getUserByPrincpals();
        List<Friend> friends = friendService.getFriendsByUser(userByPrincipals);
        List<User> users = userService.findFriends(search);
        if (search != null && !search.equals("")) {
            List<User> userSort = new ArrayList<>();
            List<Friend> friendSort = new ArrayList<>();
            int count = 0;
            for (User user : users) {
                count++;
                if (!user.getId().equals(userByPrincipals.getId())) {
                    count--;
                    for (Friend friend : friends) {
                        if (friend.getFriend().getId().equals(user.getId())) {
                            friendSort.add(friend);
                            count++;
                            break;
                        }
                    }
                }
                if (count == 0) {
                    userSort.add(user);
                }
                count = 0;
            }
            modelAndView.addObject("users", userSort);
            modelAndView.addObject("friends", friendSort);
        }
        modelAndView.addObject("user", userByPrincipals);
        return modelAndView;
    }

    @RequestMapping(value = "/user/addFriends")
    public String addFriends(@RequestParam(value = "id") String id,
                             @RequestParam(value = "idFriend", required = false) String idFriend) {
        if (idFriend != null) {
            Friend friend = friendService.getById(Integer.valueOf(idFriend));
            if (friend != null) {
                friend.setFriendstatus(FRIENDSTATUS.WAIT);
                friendService.update(friend);
            }
        } else {
            friendService.save(new Friend(userService.getUserByPrincpals(),
                    userService.getById(Integer.valueOf(id)), FRIENDSTATUS.WAIT));
        }
        return "redirect:/user/friends";
    }

    @RequestMapping(value = "/user/deleteFriends")
    public String deleteFriends(@RequestParam(value = "id") String id,
                                @RequestParam(value = "idFriend", required = false) String idFriend,
                                @RequestParam(value = "idUser", required = false) String idUser) {
        Friend friend = friendService.getById(Integer.valueOf(id));
        friendService.remove(friend);
        if (idFriend != null && idUser != null) {
            Friend friend1 = friendService.getFriendByUserAndFriend
                    (userService.getById(Integer.valueOf(idFriend)),userService.getById(Integer.valueOf(idUser)));
            friend1.setFriendstatus(FRIENDSTATUS.REJECTED);
            friendService.update(friend1);
        }
        return "redirect:/user/friends";
    }

    @RequestMapping(value = "/user/confirmFriends")
    public String confirmFriend(@RequestParam(value = "id") String id) {
        Friend friend = friendService.getById(Integer.valueOf(id));
        if (friend != null) {
            friend.setFriendstatus(FRIENDSTATUS.CONFIRM);
            friendService.update(friend);
            Friend friend1 = new Friend(userService.getUserByPrincpals(), friend.getUser(), FRIENDSTATUS.CONFIRM);
            friendService.save(friend1);
        }

        return "redirect:/user/friends";
    }

    @RequestMapping(value = "/user/rejectFriends")
    public String rejectFriend(@RequestParam(value = "id") String id) {
        Friend friend = friendService.getById(Integer.valueOf(id));
        if (friend != null) {
            friend.setFriendstatus(FRIENDSTATUS.REJECTED);
            friendService.update(friend);
        }
        return "redirect:/user/friends";
    }
}
