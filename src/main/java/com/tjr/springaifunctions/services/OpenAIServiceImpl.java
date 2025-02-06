package com.tjr.springaifunctions.services;

import com.tjr.springaifunctions.model.Answer;
import com.tjr.springaifunctions.model.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    final ChatModel chatModel;

    @Override
    public Answer getAnswer(Question question) {
        return new Answer("to-do implement me!");
    }
}