package com.tjr.springaifunctions.services;

import com.tjr.springaifunctions.functions.WeatherServiceFunction;
import com.tjr.springaifunctions.model.Answer;
import com.tjr.springaifunctions.model.Question;
import com.tjr.springaifunctions.model.WeatherRequest;
import com.tjr.springaifunctions.model.WeatherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OpenAIServiceImpl implements OpenAIService {

    private final OpenAiChatModel openAiChatModel;

    @Value("${API_NINJAS_KEY}")
    private String apiNinjasKey;

    @Override
    public Answer getAnswer(Question question) {
        var promptOptions = OpenAiChatOptions.builder()
                .functionCallbacks(List.of(FunctionCallback.builder()
//                        .function("CurrentWeather", new WeatherServiceFunction(apiNinjasKey))
//                        .description("Get the current weather for a location")
//                                .inputType(WeatherRequest.class)
//                        .build()))
                        .function("CurrentWeather", new WeatherServiceFunction(apiNinjasKey))
                        .description("Get the current weather for a location")
                        .responseConverter(response -> {
                            String schema = ModelOptionsUtils.getJsonSchema(WeatherResponse.class, false);
                            String json = ModelOptionsUtils.toJsonString(response);
                            return schema + "\n" + json;
                        })
                                .inputType(WeatherRequest.class)
                        .build()))
                .build();

        Message userMessage = new PromptTemplate(question.question()).createMessage();
        Message systemMessage = new SystemPromptTemplate("You are a weather service. You receive weather information from a service which gives you the information based on the metrics system." +
                " When answering the weather in an imperial system country, you should convert the temperature to Fahrenheit and the wind speed to miles per hour. ").createMessage();

        var response = openAiChatModel.call(new Prompt(List.of(userMessage, systemMessage), promptOptions));
        return new Answer(response.getResult().getOutput().getContent());
    }
}