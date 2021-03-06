package com.changgou.user.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.changgou.entity.PageResult;
import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.user.config.TokenDecode;
import com.changgou.user.service.UserService;
import com.changgou.user.pojo.User;
import com.changgou.user.util.FastDFSClient;
import com.changgou.user.util.FastDFSFile;
import com.changgou.user.util.ImageUpload;
import com.github.pagehelper.Page;
import com.rabbitmq.tools.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private TokenDecode tokenDecode;

    @Autowired
    private FastDFSClient fastDFSClient;

    @Autowired
    private FastDFSFile fastDFSFile;

    @RequestMapping("/updatePassword")
    public Result updatePassword(@RequestParam("password") String password, @RequestParam("hisPassword") String hisPassword,@RequestParam("name") String name)
    {
        return userService.updatePassword(password, hisPassword, name);
    }

    /**
     * 验证验证码
     *
     * @param code
     * @return
     */
    @RequestMapping("/a/updatePhone")
        public Result updatePhone(@RequestParam("code") String code) {
        return userService.updatePhone(code);
    }

    /**
     * 验证用户密码
     *
     * @param password
     * @return
     */
    @RequestMapping("/validatePassword")
    public Result validatePassword(@RequestParam("password") String password) {

        return userService.validatePassword(password);
    }


    @RequestMapping("/updatePhoneTrue")
    public Result updatePhoneTrue(@RequestParam("phone") String phone) {
        return userService.updatePhoneTrue(phone);
    }

    @PostMapping("/imageUpload")
    public Result imageUpload(MultipartFile file) {
        try {
            //判断文件是否存在
            if (file == null) {
                throw new RuntimeException("文件不存在");
            }
            //获取文件的完整名称
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                throw new RuntimeException("文件不存在");
            }

            //获取文件的扩展名称  abc.jpg   jpg
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            //获取文件内容
            byte[] content = file.getBytes();

            //创建文件上传的封装实体类
            FastDFSFile fastDFSFile = new FastDFSFile(originalFilename, content, extName);

            //基于工具类进行文件上传,并接受返回参数  String[]
            String[] uploadResult = FastDFSClient.upload(fastDFSFile);

            //封装返回结果
            String url = FastDFSClient.getTrackerUrl() + uploadResult[0] + "/" + uploadResult[1];
            if (url != null) {
                User user = new User();
                String username = tokenDecode.getUserInfo().get("username");
                user.setUsername(username);
                user.setImageUrl(url);
                userService.update(user);
            }
            return new Result(true, StatusCode.OK, "文件上传成功", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, StatusCode.ERROR, "文件上传失败");

    }

    @PostMapping("/imageUploadOss")
    public Result imageUploadOss(MultipartFile file) {
        if (file != null) {
            //更新头像
            userService.updateHead(file);
            return new Result(true, StatusCode.OK, "文件上传成功");
        }
        return new Result(false, StatusCode.ERROR, "文件上传失败");

    }


    /**
     * 更新用户数据
     * @param userInfoMap
     * @param userInfo
     * @return
     */
    @PutMapping()
    public Result updateInfo(@RequestBody Map userInfoMap, @RequestParam("userInfo") String userInfo) {


        User user = JSON.parseObject(userInfo, User.class);


        userService.updateInfo(userInfoMap, user);
        return new Result(true, StatusCode.OK, "用户更新数据成功");
    }


    /**
     * 查询全部数据
     *
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('accountant')")
    public Result findAll() {
        List<User> userList = userService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", userList);
    }

    /***
     * 根据ID查询数据
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result findById(@PathVariable String username) {
        User user = userService.findById(username);
        return new Result(true, StatusCode.OK, "查询成功", user);
    }

    @GetMapping("/load/{username}")
    public User findUserInfo(@PathVariable("username") String username) {
        User user = userService.findById(username);
        return user;
    }

    @GetMapping("/load/noname")
    public User findUse() {
        User user = userService.findByNull();
        return user;
    }

    @GetMapping("/load")
    public User findUserInfo() {

        User user = userService.findById(tokenDecode.getUserInfo().get("username"));

        return user;
    }

    @RequestMapping("/areaMap")
    public Map findAreaMap() {
        Map map = userService.findMapByAreaId();
        return map;
    }

    /***
     * 新增数据
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user){
        userService.add(user);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value = "/{username}")
    public Result update(@RequestBody User user, @PathVariable String username) {
        user.setUsername(username);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}")
    public Result delete(@PathVariable String username) {
        userService.delete(username);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search")
    public Result findList(@RequestParam Map searchMap) {
        List<User> list = userService.findList(searchMap);
        return new Result(true, StatusCode.OK, "查询成功", list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}")
    public Result findPage(@RequestParam Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult = new PageResult(pageList.getTotal(), pageList.getResult());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    /**
     * 根据用户名获取用户电话
     *
     * @return
     */
    @GetMapping("/findPhoneByUsername")
    public String findPhoneByUsername() {
        String username = tokenDecode.getUserInfo().get("username");
        String phone = userService.findPhoneByUsername(username);
        return phone;
    }
}
