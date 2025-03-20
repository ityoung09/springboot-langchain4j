package com.kedaya.integrationlangchain4j;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.response.ChatResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IntegrationLangchain4jApplicationTests {

    @Resource
    private QwenChatModel qwenChatModel;

    /**
     * 测试记忆对话
     */
    @Test
    void test_01() {
        UserMessage userMessage = UserMessage.userMessage("你好, 我叫陈伟");
        ChatResponse chat = qwenChatModel.chat(userMessage);
        AiMessage aiMessage = chat.aiMessage();
        System.out.println(aiMessage.text());
        ChatResponse secondChat = qwenChatModel.chat(userMessage, aiMessage, UserMessage.userMessage("你好，请我我叫什么名字"));
        System.out.println(secondChat.aiMessage().text());
    }

}
