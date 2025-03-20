package com.kedaya.integrationlangchain4j.config;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.integrationlangchain4j.config
 * @Project：integration-langchain4j
 * @name：AiGroupByChatIdPersistentStoreConfig
 * @Date：2025-03-20 16:38
 * @Filename：AiGroupByChatIdPersistentStoreConfig
 * 会话分离，历史会话持久储配置
 */
@Configuration
public class AiGroupByChatIdPersistentStoreWithFuncCallConfig {

    public interface Assistant {
        String chat(@MemoryId String chatId, @UserMessage String message);

        TokenStream chatStream(@MemoryId String chatId, @UserMessage String message);
    }

    @Bean
    public AiGroupByChatIdPersistentStoreWithFuncCallConfig.Assistant groupByChatIdPersistentWithFuncCallAssistant(@Qualifier("qwenChatModel") ChatLanguageModel qwenChatModel,
                                                                                 QwenStreamingChatModel qwenStreamingChatModel,
                                                                                 AiFunctionCallConfig aiFunctionCallConfig) {

        ChatMemoryProvider chatMemoryProvider = memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .chatMemoryStore(new MySqlChatMemoryStoreConfig())
                .build();
        return AiServices.builder(AiGroupByChatIdPersistentStoreWithFuncCallConfig.Assistant.class).chatLanguageModel(qwenChatModel)
                .streamingChatLanguageModel(qwenStreamingChatModel)
                .chatMemoryProvider(chatMemoryProvider)
                .tools(aiFunctionCallConfig)
                .build();
    }
}
