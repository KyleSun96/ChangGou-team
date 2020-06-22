package com.changgou.user.service;

import com.changgou.order.pojo.Task;
import com.changgou.user.pojo.User;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface UserService {


    /**
     * 修改数据
     * @param userInfoMap
     * @param userInfo
     */
    void updateInfo(Map userInfoMap, User userInfo);

    /**
     * 根据地区
     *
     * @return
     */
    String findAreaId(Map userInfoMap);

    /***
     * 查询所有
     * @return
     */
    List<User> findAll();

    /**
     * 根据ID查询
     *
     * @param username
     * @return
     */
    User findById(String username);

    /***
     * 新增
     * @param user
     */
    void add(User user);

    /***
     * 修改
     * @param user
     */
    void update(User user);

    /***
     * 删除
     * @param username
     */
    void delete(String username);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<User> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<User> findPage(Map<String, Object> searchMap, int page, int size);

    int updateUserPoint(Task task);
}
