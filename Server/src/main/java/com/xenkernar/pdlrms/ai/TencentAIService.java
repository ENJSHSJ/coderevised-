package com.xenkernar.pdlrms.ai;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.nlp.v20190408.NlpClient;
import com.tencentcloudapi.nlp.v20190408.models.*;
import com.xenkernar.pdlrms.config.TencentCloudConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TencentAIService {
    private final NlpClient client;

    //    @Autowired
//    public TencentAIService(TencentCloudConfig config) {
//        Credential cred = new Credential(
//                config.getSecretId(),
//                config.getSecretKey()
//        );
//        this.client = new NlpClient(cred, config.getRegion());
//    }
//     改为接受客户端注入
    @Autowired
    public TencentAIService(TencentCloudConfig config, NlpClient client) {
        this.client = client;
    }
//    @Autowired
//    public TencentAIService(TencentCloudConfig config, NlpClient client) {
//        Credential cred = new Credential(
//                config.getSecretId(),
//                config.getSecretKey()
//        );
//        this.client = client == null ? new NlpClient(cred, config.getRegion()) : client;
//    }
    /**
     * 生成代码练习题
     * @param code 用户上传的正确代码
     * @return 生成的练习题（Markdown格式）
     */
    public String generateExercise(String code) {
        try {
            // 初始化请求对象
            TextWritingRequest req = new TextWritingRequest();
            req.setText(code);
            req.setStyle("technical");

            // 调用腾讯云API
            TextWritingResponse resp = client.TextWriting(req);

            // 处理响应
            if (resp.getWritingList() == null || resp.getWritingList().length == 0) {
                throw new RuntimeException("未返回有效内容");
            }
            return resp.getWritingList()[0].getTargetText();

        } catch (TencentCloudSDKException e) {
            throw new RuntimeException("调用腾讯云API时出错: " + e.getMessage(), e);
        }
    }
}