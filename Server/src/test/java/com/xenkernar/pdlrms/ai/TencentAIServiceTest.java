package com.xenkernar.pdlrms.ai;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;
import com.xenkernar.pdlrms.config.TencentCloudConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TencentAIServiceTest {

    @Mock
    private NlpClient mockNlpClient;

    @Mock
    private TencentCloudConfig mockConfig;

    @InjectMocks
    private TencentAIService tencentAIService;

    @Test
    public void testGenerateExercise_Success() throws TencentCloudSDKException {
        // 其他测试逻辑

        // 准备模拟响应
        TextWritingResponse mockResponse = new TextWritingResponse();
        Writing[] writings = new Writing[1];
        writings[0] = new Writing();
        writings[0].setTargetText("生成的练习题内容");
        mockResponse.setWritingList(writings);

//         配置 Mock 行为
        lenient().when(mockConfig.getSecretId()).thenReturn("mock-secret-id");
        lenient().when(mockConfig.getSecretKey()).thenReturn("mock-secret-key");
        lenient().when(mockConfig.getRegion()).thenReturn("ap-guangzhou");
        when(mockNlpClient.TextWriting(any(TextWritingRequest.class))).thenReturn(mockResponse);

        // 调用并验证
        String result = null;
        try {
            result = tencentAIService.generateExercise("public class Test {}");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals("生成的练习题内容", result);
    }

    @Test
    public void testGenerateExercise_EmptyResponse() throws TencentCloudSDKException {
        // 模拟空响应
        when(mockNlpClient.TextWriting(any(TextWritingRequest.class))).thenReturn(new TextWritingResponse());

        // 验证自定义异常
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tencentAIService.generateExercise("public class Test {}");
        });
        assertTrue(exception.getMessage().contains("未返回有效内容"));
    }

    @Test
    public void testGenerateExercise_SDKException() throws TencentCloudSDKException {
        // 模拟 SDK 异常
        when(mockNlpClient.TextWriting(any(TextWritingRequest.class)))
                .thenThrow(new TencentCloudSDKException("模拟腾讯云SDK异常"));

        // 验证异常处理
        Exception exception = assertThrows(RuntimeException.class, () -> {
            tencentAIService.generateExercise("public class Test {}");
        });
        assertTrue(exception.getMessage().contains("调用腾讯云API时出错"));
    }
}