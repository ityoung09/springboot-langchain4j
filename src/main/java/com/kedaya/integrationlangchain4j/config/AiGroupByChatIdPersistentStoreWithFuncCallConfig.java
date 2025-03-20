package com.kedaya.integrationlangchain4j.config;

import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.integrationlangchain4j.config
 * @Project：integration-langchain4j
 * @name：AiGroupByChatIdPersistentStoreConfig
 * @Date：2025-03-20 16:38
 * @Filename：AiGroupByChatIdPersistentStoreConfig 会话分离，历史会话持久储配置
 */
@Configuration
public class AiGroupByChatIdPersistentStoreWithFuncCallConfig {

    public interface Assistant {
        String chat(@MemoryId String chatId, @UserMessage String message);

        @SystemMessage("""
                您是“Tuling”航空公司的客户聊天支持代理。请以友好、乐于助人且愉快的方式来回复。
                您正在通过在线聊天系统与客户互动。
                在提供有关预订或取消预订的信息之前，您必须始终从用户处获取以下信息：预订号、客户姓名。
                请讲中文。
                今天的日期是 ｛｛current_date｝｝。
                """)
        TokenStream chatStream(@MemoryId String chatId, @UserMessage String message, @V("current_date") String currentDate);
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
