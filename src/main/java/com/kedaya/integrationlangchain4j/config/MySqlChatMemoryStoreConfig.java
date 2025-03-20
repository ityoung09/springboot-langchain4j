package com.kedaya.integrationlangchain4j.config;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author：CHENWEI
 * @Package：com.kedaya.integrationlangchain4j.config
 * @Project：integration-langchain4j
 * @name：MySqlChatMemoryStoreConfig
 * @Date：2025-03-20 16:39
 * @Filename：MySqlChatMemoryStoreConfig
 */
public class MySqlChatMemoryStoreConfig implements ChatMemoryStore {

    private Map<String, List<ChatMessage>> storeMap = new ConcurrentHashMap<>();

    /**
     * Retrieves messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     * @return List of messages for the specified chat memory. Must not be null. Can be deserialized from JSON using {@link ChatMessageDeserializer}.
     */
    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        if (storeMap.containsKey(memoryId.toString())) {
            return storeMap.get(memoryId);
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * Updates messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     * @param messages List of messages for the specified chat memory, that represent the current state of the {@link ChatMemory}.
     *                 Can be serialized to JSON using {@link ChatMessageSerializer}.
     */
    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
//        messages.forEach(System.out::println);
//        if (storeMap.containsKey(memoryId.toString())) {
//            List<ChatMessage> chatMessages = storeMap.get(memoryId);
////            chatMessages.addAll(messages);
//            storeMap.put(memoryId.toString(), chatMessages);
//        }else {
            storeMap.put(memoryId.toString(), messages);
//        }
    }

    /**
     * Deletes all messages for a specified chat memory.
     *
     * @param memoryId The ID of the chat memory.
     */
    @Override
    public void deleteMessages(Object memoryId) {
        storeMap.remove(memoryId);
    }
}
