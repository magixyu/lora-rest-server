package lorapp.rest.controller;

import lorapp.db.entity.User;
import lorapp.db.repo.UserRepo;
import lorapp.rest.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

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
    public CommonResult addUser(@RequestBody User user){
        CommonResult commRes = new CommonResult();

        try {
            User existingUser = userRepo.findByUserName(user.getUserName());
            if(existingUser != null){
                commRes.setErrorMsg("用户名已存在");
            }else{
                String passwd = getMD5Code(user.getPasswd());
                user.setPasswd(passwd);
                userRepo.save(user);
                commRes.setSuccess(true);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            commRes.setErrorMsg(e.getMessage());
        }
        return commRes;
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.DELETE)
    public CommonResult deleteUser(@PathVariable("userName") String userName){
        CommonResult commRes = new CommonResult();
        userRepo.delete(userRepo.findByUserName(userName));
        commRes.setSuccess(true);
        return commRes;
    }

    @RequestMapping(value = "/{userName}", method = RequestMethod.POST)
    public CommonResult updateUser(@RequestBody final User user){
        CommonResult commRes = new CommonResult();

        try {
            User targetUser = userRepo.findByUserName(user.getUserName());
            if (targetUser != null) {
                user.setPasswd(getMD5Code(user.getPasswd()));
                user.setId(targetUser.getId());
                userRepo.save(user);

                commRes.setSuccess(true);
                commRes.setResponseData(new HashMap<String, Object>() {{put("updatedUser", user);}});
            }else{
                commRes.setErrorMsg("不存在此用户");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            commRes.setErrorMsg(e.getMessage());
        }
        return commRes;
    }


    private String getMD5Code(String passwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] passwdInMD5 = md.digest(passwd.getBytes());
        return String.valueOf(passwdInMD5);
    }

}
