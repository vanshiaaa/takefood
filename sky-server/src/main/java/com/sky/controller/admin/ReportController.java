package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 数据统计接口
 */
@RestController
@RequestMapping("/admin/report")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate end
    ) {
        TurnoverReportVO turnoverReportVO = reportService.turnoverStatistics(begin, end);

        return Result.success(turnoverReportVO);
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */

    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate end
    ) {
        UserReportVO userReportVO = reportService.userStatistics(begin, end);

        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> ordersreport(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate end
    ) {
        OrderReportVO orderReportVO = reportService.orderreport(begin, end);

        return Result.success(orderReportVO);
    }

    /**
     * 查询销售额前10的菜品
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate end
    ) {
        SalesTop10ReportVO salestop10 = reportService.salestop10(begin, end);

        return Result.success(salestop10);
    }
}
