package com.tjr.springaifunctions.services;

import com.tjr.springaifunctions.model.Answer;
import com.tjr.springaifunctions.model.Question;

public interface OpenAIService {
    Answer getAnswer(Question question);
}
