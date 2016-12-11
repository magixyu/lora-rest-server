package lorapp.rest.controller;

import lorapp.db.entity.User;
import lorapp.db.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;

/**
 * @Author MoseC
 * @Desc
 * @Date 2016/12/11
 */

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public Iterable<User> getAllUser(){
        return userRepo.findAll();
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public User getUser(@PathVariable("userName") String userName){
        return userRepo.findByUserName(userName);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void addUser(@RequestBody User user){
        userRepo.save(user);
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable("userName") String userName){
        userRepo.delete(userRepo.findByUserName(userName));
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.POST)
    public void updateUser(@RequestBody User user){
        User targetUser = userRepo.findByUserName(user.getUserName());
        if(targetUser != null){
            user.setId(targetUser.getId());
            userRepo.save(user);
        }

    }

}
