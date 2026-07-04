package dev.langchain4j.quarkus.workshop;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.ContentMetadata;
import dev.langchain4j.rag.content.injector.ContentInjector;

@ApplicationScoped
public class RagLoggingContentInjector implements ContentInjector {

    @Inject
    RagChunkLogService ragChunkLogService;

    @Override
    public UserMessage inject(List<Content> contents, ChatMessage chatMessage) {
        String userQuestion = ((UserMessage) chatMessage).singleText();

        StringBuilder context = new StringBuilder();

        for (Content content : contents) {
            Double score = 0.0;
            if (content.metadata() != null && content.metadata().containsKey(ContentMetadata.SCORE)) {
                Object scoreObj = content.metadata().get(ContentMetadata.SCORE);
                if (scoreObj instanceof Number number) {
                    score = number.doubleValue();
                }
            }

            String sourceDoc = "desconhecido";
            if (content.textSegment().metadata() != null) {
                String fileName = content.textSegment().metadata().getString("file_name");
                if (fileName != null) {
                    sourceDoc = fileName;
                }
            }

            ragChunkLogService.log(userQuestion, content.textSegment().text(), score, sourceDoc);

            context.append("- ").append(content.textSegment().text()).append("\n");
        }

        return new UserMessage(userQuestion
                + "\nUse as seguintes informacoes para responder:\n"
                + context);
    }
}
