package lorapp.rest.controller;

import lorapp.db.entity.User;
import lorapp.db.repo.UserRepo;
import lorapp.rest.util.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


/**
 * To make sure the response data in the same format,
 * make the input data validation logic embedded into controller method, but not use the hibernate validator
 */
@RestController
public class UserController {
    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepo userRepo;

    @RequestMapping(value = "/admin/user/all", method = RequestMethod.GET)
    public Iterable<User> getAllUser(){
        return userRepo.findAll();
    }

    @RequestMapping(value = "/admin/user/{userName}", method = RequestMethod.GET)
    public User getUser(@PathVariable("userName") String userName){
        return userRepo.findByUserName(userName);
    }

    @RequestMapping(value = "/admin/user", method = RequestMethod.PUT)
    public CommonResult addUser(@RequestBody User user){
        CommonResult commRes = new CommonResult();

        String userName = user.getUserName();
        String passwd = user.getPasswd();
        if(userName == null || userName.length()<1 || userName.length()>10){
            commRes.setErrorMsg("用户名长度必须在1~10之间。");
            return commRes;
        }
        if(passwd == null || passwd.length()<5 || passwd.length()>10){
            commRes.setErrorMsg("密码长度必须在5~10之间。");
            return commRes;
        }

        try {
            User existingUser = userRepo.findByUserName(user.getUserName());
            if(existingUser != null){
                commRes.setErrorMsg("用户名已存在。");
            }else{
                String passwdInMD5 = getMD5Code(user.getPasswd());
                user.setPasswd(passwdInMD5);
                userRepo.save(user);
                commRes.setSuccess(true);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            commRes.setErrorMsg(e.getMessage());
        }
        return commRes;
    }

    @RequestMapping(value = "/admin/user/{userName}", method = RequestMethod.DELETE)
    public CommonResult deleteUser(@PathVariable("userName") String userName){
        CommonResult commRes = new CommonResult();
        userRepo.delete(userRepo.findByUserName(userName));
        commRes.setSuccess(true);
        return commRes;
    }

    /**
     * userName should not be able to update
     * @param user
     * @return
     */
    @RequestMapping(value = "/admin/user/{userName}", method = RequestMethod.POST)
    public CommonResult updateUser(@RequestBody final User user){
        CommonResult commRes = new CommonResult();

        String passwd = user.getPasswd();
        if(passwd == null || passwd.length()<5 || passwd.length()>10){
            commRes.setErrorMsg("密码长度必须在5~10之间。");
            return commRes;
        }

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

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody User user, HttpServletRequest request){
        CommonResult commRes = new CommonResult();

        String userName = user.getUserName();
        String passwd = user.getPasswd();
        try {
            User targetUser = userRepo.findByUserName(userName);
            if (targetUser == null) {
                commRes.setErrorMsg("不存在此用户，请联系管理员添加。");
            } else {
                String passwdInMD5 = targetUser.getPasswd();
                if (passwdInMD5.equals(getMD5Code(passwd))) {
                    commRes.setSuccess(true);
                    request.getSession().setAttribute("user", targetUser);
                }else{
                    commRes.setErrorMsg("密码不正确。");
                }
            }
        } catch (Exception e){
            commRes.setErrorMsg(e.getMessage());
        }
        return commRes;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public CommonResult logout(HttpServletRequest request){
        CommonResult commRes = new CommonResult();
        request.getSession().removeAttribute("user");
        commRes.setSuccess(true);
        return commRes;
    }


    private static String getMD5Code(String passwd) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();

        String passwdInMD5 = base64en.encode(md.digest(passwd.getBytes()));
        return passwdInMD5;
    }

    public static void main(String[] args){
        try {
            System.out.println(UserController.getMD5Code("123456"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


}
