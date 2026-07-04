package dev.langchain4j.quarkus.workshop.entity;

import java.time.Instant;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class RagChunkLog extends PanacheEntity {

    @Column(columnDefinition = "TEXT")
    public String userQuestion;

    @Column(columnDefinition = "TEXT")
    public String chunkText;

    public Double similarityScore;

    public String sourceDocument;

    public Instant retrievalTimestamp;
}
