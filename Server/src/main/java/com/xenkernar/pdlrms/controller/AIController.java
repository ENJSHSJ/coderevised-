package com.xenkernar.pdlrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xenkernar.pdlrms.ai.TencentAIService;
// src/main/java/com/yourproject/controller/AIController.java
@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private TencentAIService aiService;

    @PostMapping("/generate-exercise")
    public ResponseEntity<String> generateExercise(@RequestBody String code) {
        try {
            String exercises = aiService.generateExercise(code);
            return ResponseEntity.ok(exercises);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("AI服务调用失败: " + e.getMessage());
        }
    }
}