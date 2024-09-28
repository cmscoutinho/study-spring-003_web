package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ChatGPTQuery {
    public static String getTranslation(String texto) {
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_APIKEY"));


        CompletionRequest query = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();


        var reply = service.createCompletion(query);
        return reply.getChoices().get(0).getText();
    }
}
