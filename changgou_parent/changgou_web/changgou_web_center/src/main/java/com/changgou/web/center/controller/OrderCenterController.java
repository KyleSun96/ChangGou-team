package com.changgou.web.center.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.feign.OrderItemFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wcenter")
public class OrderCenterController {

    @Autowired
    private OrderFeign orderFeign;

    @Autowired
    private OrderItemFeign orderItemFeign;



    /**
     * 跳转到未发货订单页面
     * @param model
     * @return
     */
    @RequestMapping("/order/noConsignOrder")
    public String toOrderSend(Model model){
        List<Order> orderList = orderFeign.findNoConsignByUsername().getData();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = (List<OrderItem>) orderItemFeign.findByOrderId(order.getId()).getData();
            order.setOrderItemList(orderItemList);
        }
        model.addAttribute("orderList", orderList);
        return "center-order-send";
    }
    /**
     * 跳转到未支付订单页面
     * @param model
     * @return
     */
    @RequestMapping("/order/noPayOrder")
    public String toOrderPay(Model model){
        List<Order> orderList = orderFeign.findNoPayByUsername().getData();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = (List<OrderItem>) orderItemFeign.findByOrderId(order.getId()).getData();
            order.setOrderItemList(orderItemList);
        }
        model.addAttribute("orderList", orderList);
        return "center-order-pay";
    }
    //手动确定收货
    @GetMapping("/task")
    @ResponseBody
    public Result task(@RequestParam("id") String id){
        Result result = orderFeign.confirmTask(id);
        return result;
    }

    //查询所有待收货订单
    @GetMapping ("/findOrder")
    @ResponseBody
    public Result findOrder(){
        Result<List<Order>> result = orderFeign.findAllOrder();
        return result;
    }

    /**
     * 跳转到待收货页面
     *
     * @param model
     * @return
     */
    @GetMapping("/order/findPayOrder")
    public String toOrderReceive(Model model) {
        List<Order> orderList = orderFeign.findPayOrder().getData();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = (List<OrderItem>) orderItemFeign.findByOrderId(order.getId()).getData();
            order.setOrderItemList(orderItemList);
        }
        model.addAttribute("orderList", orderList);

        return "center-order-receive";

    }



    /**
     * 跳转到待评价页面
     *
     * @return
     */
    @RequestMapping("/order/findBuyerRate")
    public String toOrderEvaluate(Model model) {
        List<Order> orderList = orderFeign.findBuyerRateByOrder().getData();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = (List<OrderItem>) orderItemFeign.findByOrderId(order.getId()).getData();
            order.setOrderItemList(orderItemList);
        }
        model.addAttribute("orderList", orderList);

        return "center-order-evaluate";
    }

    //查询所有订单
    @RequestMapping("/order/allOrder")
    public String toIndex(Model model){
        List<Order> orderList = orderFeign.findOrderByUsername().getData();
        for (Order order : orderList) {
            List<OrderItem> orderItemList = (List<OrderItem>) orderItemFeign.findByOrderId(order.getId()).getData();
            order.setOrderItemList(orderItemList);
        }
        model.addAttribute(orderList);
        return "center-index";
    }

    /**
     * 发送催发货短信
     */
    @RequestMapping("/toCall")
    @ResponseBody
    public Result toCall(@RequestParam("id") String id){
        String subId = id.substring(id.length() - 6);
        Result result = orderFeign.sendMessage(subId);
        return result;
    }
}
