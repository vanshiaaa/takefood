package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的定时任务
     */
    @Scheduled(cron = "0 0/1 * * * ?")//每分钟执行一次
    public void processTimeoutOrder() {
        log.info("执行处理超时订单的定时任务");

        LocalDateTime time = LocalDateTime.now().minusMinutes(30);

        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);


        if (list != null && !list.isEmpty()) {
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时未支付，系统自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
                log.info("订单 {} 已取消", orders.getNumber());
            }
        } else {
            log.info("没有需要处理的超时订单");
        }


    }

    /**
     * 处理配送订单的定时任务
     */
    @Scheduled(cron = "0 0 1 * * ?")//每天凌晨1点执行一次
    public  void processDeliverOrder(){
        log.info("执行处理配送订单的定时任务");
        LocalDateTime time = LocalDateTime.now().minusHours(1);
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (list != null && !list.isEmpty()) {
            for (Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
                orderMapper.update(orders);
                log.info("订单 {} 已完成", orders.getNumber());
            }
        } else {
            log.info("没有需要处理的配送订单");
        }

    }
}
