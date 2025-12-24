package com.brunocosta.taskflow.repository;

import static com.mongodb.client.model.Filters.eq;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.brunocosta.taskflow.domain.TaskComment;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.Sorts;

import jakarta.inject.Singleton;

@Singleton
public class TaskCommentRepository {
    private final MongoCollection<Document> collection;

    public TaskCommentRepository(MongoClient client) {
        this.collection = client.getDatabase("taskflow").getCollection("task_comments");
    }

    public TaskComment insert(TaskComment c) {
        Document d = toDocument(c);
        collection.insertOne(d);
        c.setId(d.getObjectId("_id"));
        return c;
    }

    public List<TaskComment> findByTaskId(ObjectId taskId) {
        return collection.find(eq("taskId", taskId))
                .sort(Sorts.ascending("createdAt"))
                .map(this::fromDocument)
                .into(new ArrayList<>());
    }

    public void ensureIndexes(){
        collection.createIndex(Indexes.ascending("taskId"));
        collection.createIndex(Indexes.ascending("createdAt"));
    }

    private TaskComment fromDocument(Document d) {
        TaskComment comment = new TaskComment();
        comment.setId(d.getObjectId("_id"));
        comment.setTaskId(d.getObjectId("taskId"));
        comment.setText(d.getString("text"));
        comment.setCreatedAt(d.get("createdAt", Date.class).toInstant());
        return comment;
    }

    private Document toDocument(TaskComment c) {
        Document d = new Document();
        if (Objects.nonNull(c.getId()))
            d.put("_id", c.getId());
        d.put("taskId", c.getTaskId());
        d.put("text", c.getText());
        d.put("createdAt", Instant.now());
        return d;
    }

}
