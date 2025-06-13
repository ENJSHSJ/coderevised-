package com.xenkernar.pdlrms;

import com.xenkernar.pdlrms.entity.PublishedReport;
import com.xenkernar.pdlrms.entity.ReportTemplate;
import com.xenkernar.pdlrms.repository.PublishedReportRepository;
import com.xenkernar.pdlrms.service.ReportTemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.xenkernar.pdlrms.service.AdjudicatorService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

//@SpringBootTest
//class XenKerApplicationTests {
//    @Autowired
//    AdjudicatorService adjudicatorService;
//    @Autowired
//    ReportTemplateService reportTemplateService;
//    @Autowired
//    PublishedReportRepository publishedReportRepository;
//    @Test
//    void contextLoads() throws IOException, InterruptedException, ExecutionException, TimeoutException {
//        ExecutorService cacheExecutor = Executors.newVirtualThreadPerTaskExecutor();
//        File file = new File("D:/Users/Xenkernar/Desktop/reports/202125810201第四周.docx");
//        ReportTemplate template = reportTemplateService.get("默认");
//        long startTime = System.currentTimeMillis();
//        Future<String> future = cacheExecutor.submit(() -> {
//            AtomicInteger count = new AtomicInteger();
//            for (int i = 0; i < 50; i++) {
//                int finalI = i;
//                cacheExecutor.submit(() -> {
//                    try {
//                        PublishedReport lab = publishedReportRepository.findByFileName("程序设计实验-第1周周四.docx").getFirst();
//                        adjudicatorService.adjudicateReport(file, "C", template, lab.getTestCases());
//                        System.out.println("批改了第"+ (finalI + 1) +"份报告");
//                        count.getAndIncrement();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
//            while (count.get() < 50) {
//                Thread.sleep(1);
//            }
//            return "success";
//        });
//        future.get(100, TimeUnit.SECONDS);
//        long endTime = System.currentTimeMillis();
//        System.out.println("测试50份报告耗时: " + (endTime - startTime)/1000 + " 秒");
//    }
//
//}
@SpringBootTest
class XenKerApplicationTests {
    @Autowired
    AdjudicatorService adjudicatorService;
    @Autowired
    ReportTemplateService reportTemplateService;
    @Autowired
    PublishedReportRepository publishedReportRepository;

    @Test
    void contextLoads() throws IOException, InterruptedException, ExecutionException, TimeoutException {
        ExecutorService cacheExecutor = Executors.newVirtualThreadPerTaskExecutor();
        File file = new File("D:/Users/Xenkernar/Desktop/reports/202125810201第四周.docx");
        ReportTemplate template = reportTemplateService.get("默认");
        long startTime = System.currentTimeMillis();

        // 使用 CountDownLatch 监控子任务完成状态
        CountDownLatch latch = new CountDownLatch(50);

        Future<String> future = cacheExecutor.submit(() -> {
            for (int i = 0; i < 50; i++) {
                int finalI = i;
                cacheExecutor.submit(() -> {
                    try {
                        PublishedReport lab = publishedReportRepository.findByFileName("程序设计实验-第1周周四.docx").getFirst();
                        adjudicatorService.adjudicateReport(file, "C", template, lab.getTestCases());
                        System.out.println("批改了第" + (finalI + 1) + "份报告");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        // 每完成一个任务，计数减一
                        latch.countDown();
                    }
                });
            }
            // 等待所有子任务完成
            latch.await(); // 阻塞直到计数归零
            return "success";
        });

        // 主线程等待未来任务完成，设置合理超时
        try {
            String result = future.get(100, TimeUnit.SECONDS);
            System.out.println("所有任务完成，结果: " + result);
        } catch (TimeoutException e) {
            System.err.println("任务超时，部分任务可能未完成。");
            future.cancel(true); // 取消未完成的任务
        }

        long endTime = System.currentTimeMillis();
        System.out.println("测试50份报告耗时: " + (endTime - startTime) / 1000 + " 秒");
    }
}

