package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /***
     * 查询所有
     * @return
     */
    List<Order> findAll();

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    Order findById(String id);

    //查询待收货的订单
    List<Order> findPayOrder(String username);

    //查询带评价订单
    List<Order> findBuyerRateByOrder(String username);

    /***
     * 新增
     * @param order
     */
    String add(Order order);

    /***
     * 修改
     * @param order
     */
    void update(Order order);

    /***
     * 删除
     * @param id
     */
    void delete(String id);

    /***
     * 多条件搜索
     * @param searchMap
     * @return
     */
    List<Order> findList(Map<String, Object> searchMap);

    /***
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(int page, int size);

    /***
     * 多条件分页查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    Page<Order> findPage(Map<String, Object> searchMap, int page, int size);

    //修改订单的支付状态,并记录日志
    void updatePayStatus(String orderId, String transactionId);

    void closeOrder(String message);

    void batchSend(List<Order> orders);

    //手动确认收货
    void confirmTask(String orderId,String operator);

    //确认收货
    public void define(String username);

    void autoTack();
}
