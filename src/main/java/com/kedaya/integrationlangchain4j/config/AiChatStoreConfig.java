package com.kedaya.integrationlangchain4j.config;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.integrationlangchain4j.config
 * @Project：integration-langchain4j
 * @name：AiChatStoreConfig
 * @Date：2025-03-20 15:44
 * @Filename：AiChatStoreConfig
 * 这种方式是存在了内存中一但重启就失效了
 */
@Configuration
public class AiChatStoreConfig {

    public interface Assistant {
        String chat(String message);

        TokenStream chatStream(String message);
    }

    @Bean
    public Assistant normalAssistant(@Qualifier("qwenChatModel") ChatLanguageModel qwenChatModel, QwenStreamingChatModel qwenStreamingChatModel) {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant assistant = AiServices.builder(Assistant.class)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .chatLanguageModel(qwenChatModel)
                .chatMemory(chatMemory)
                .build();
        return assistant;
    }
}
