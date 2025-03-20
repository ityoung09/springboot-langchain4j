/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kedaya.integrationlangchain4j.demos.web;

import com.kedaya.integrationlangchain4j.config.AiChatStoreConfig;
import com.kedaya.integrationlangchain4j.config.AiGroupByChatIdPersistentStoreConfig;
import com.kedaya.integrationlangchain4j.config.AiGroupByChatIdPersistentStoreWithFuncCallConfig;
import com.kedaya.integrationlangchain4j.config.AiGroupByChatIdStoreConfig;
import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.TokenStream;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
@RequestMapping("/ai")
public class BasicController {

    @Resource
    private QwenChatModel qwenChatModel;

    @Resource
    private OpenAiChatModel openAiChatModel;

    @Resource
    private QwenStreamingChatModel qwenStreamingChatModel;

    @Resource
    @Qualifier("normalAssistant")
    private AiChatStoreConfig.Assistant assistant;

    @Resource
    @Qualifier("groupByChatAssistant")
    private AiGroupByChatIdStoreConfig.Assistant assistant2;

    @Resource
    @Qualifier("groupByChatIdPersistentAssistant")
    private AiGroupByChatIdPersistentStoreConfig.Assistant assistant3;

    @Resource
    @Qualifier("groupByChatIdPersistentWithFuncCallAssistant")
    private AiGroupByChatIdPersistentStoreWithFuncCallConfig.Assistant assistant4;


    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "你好", required = false) String message) {
        return openAiChatModel.chat(message);
    }

    @GetMapping("/qwMaxChat")
    public String qwMax(@RequestParam(value = "message", defaultValue = "你好", required = false) String message) {
        return qwenChatModel.chat(message);
    }

    @GetMapping(value = "/qwq-32b", produces = "text/stream;charset=utf-8")
    public Flux<String> qwq32b(@RequestParam(value = "message", defaultValue = "你好", required = false) String message) {
        return Flux.create(fluxSink -> {
            qwenStreamingChatModel.chat(message, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    fluxSink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    fluxSink.complete();
                }

                @Override
                public void onError(Throwable error) {
                    fluxSink.error(error);
                }
            });
        });
    }

    @GetMapping("/qwMaxHistory")
    public String qwMaxHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message) {
        return assistant.chat(message);
    }

    @GetMapping(value = "/qwq32bHistory", produces = "text/stream;charset=utf-8")
    public Flux<String> qwq32bHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message) {
        TokenStream tokenStream = assistant.chatStream(message);
        return Flux.create(fluxSink -> {
            tokenStream.onPartialResponse(fluxSink::next)
                    .onCompleteResponse(chatResponse -> fluxSink.complete())
                    .onError(fluxSink::error)
                    .start();
        });
    }

    @GetMapping("/qwMaxUniqHistory")
    public String qwMaxUniqHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        return assistant2.chat(chatId, message);
    }

    @GetMapping(value = "/qwq32bUniqHistory", produces = "text/stream;charset=utf-8")
    public Flux<String> qwq32bUniqHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        TokenStream tokenStream = assistant2.chatStream(chatId, message);
        return Flux.create(fluxSink -> {
            tokenStream.onPartialResponse(fluxSink::next)
                    .onCompleteResponse(chatResponse -> fluxSink.complete())
                    .onError(fluxSink::error)
                    .start();
        });
    }
    @GetMapping("/qwMaxUniqPreHistory")
    public String qwMaxUniqPreHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        return assistant3.chat(chatId, message);
    }

    @GetMapping(value = "/qwq32bUniqPreHistory", produces = "text/stream;charset=utf-8")
    public Flux<String> qwq32bUniqPreHistory(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        TokenStream tokenStream = assistant3.chatStream(chatId, message);
        return Flux.create(fluxSink -> {
            tokenStream.onPartialResponse(fluxSink::next)
                    .onCompleteResponse(chatResponse -> fluxSink.complete())
                    .onError(fluxSink::error)
                    .start();
        });
    }
    @GetMapping("/qwMaxUniqPreHistoryWithFuncCall")
    public String qwMaxUniqPreHistoryWithFuncCall(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        return assistant4.chat(chatId, message);
    }

    @GetMapping(value = "/qwq32bUniqPreHistoryyWithFuncCall", produces = "text/stream;charset=utf-8")
    public Flux<String> qwq32bUniqPreHistoryyWithFuncCall(@RequestParam(value = "message", defaultValue = "你好", required = false) String message, @RequestParam("chatId") String chatId) {
        TokenStream tokenStream = assistant4.chatStream(chatId, message, LocalDate.now().toString());
        return Flux.create(fluxSink -> {
            tokenStream.onPartialResponse(fluxSink::next)
                    .onCompleteResponse(chatResponse -> fluxSink.complete())
                    .onError(fluxSink::error)
                    .start();
        });
    }


}
