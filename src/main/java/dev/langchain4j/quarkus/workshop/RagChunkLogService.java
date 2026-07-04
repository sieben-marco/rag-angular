package dev.langchain4j.quarkus.workshop;

import java.time.Instant;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import dev.langchain4j.quarkus.workshop.entity.RagChunkLog;

@ApplicationScoped
public class RagChunkLogService {

    @Transactional
    public void log(String userQuestion, String chunkText,
                    Double similarityScore, String sourceDocument) {
        RagChunkLog log = new RagChunkLog();
        log.userQuestion = userQuestion;
        log.chunkText = chunkText;
        log.similarityScore = similarityScore;
        log.sourceDocument = sourceDocument;
        log.retrievalTimestamp = Instant.now();
        log.persist();
    }
}
