package com.brunocosta.taskflow.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.brunocosta.taskflow.domain.Task;
import com.brunocosta.taskflow.domain.TaskStatus;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Sorts;

import jakarta.inject.Singleton;
import static com.mongodb.client.model.Filters.*;

@Singleton
public class TaskRepository {

    private final MongoCollection<Document> collection;

    public TaskRepository(MongoClient mongoClient) {
        this.collection = mongoClient.getDatabase("taskflow").getCollection("tasks");
    }

    public Task insert(Task task) {
        Document doc = toDocument(task);
        collection.insertOne(doc);
        task.setId(doc.getObjectId("_id"));
        return task;
    }

    public Optional<Task> findById(ObjectId id) {
        Document doc = collection.find(eq("_id", id)).first();
        return Optional.ofNullable(doc).map(this::fromDocument);
    }

    public boolean update(ObjectId id, Task updated) {
        Document set = new Document()
                .append("title", updated.getTitle())
                .append("description", updated.getDescription())
                .append("status", updated.getStatus().name())
                .append("updatedAt", updated.getUpdatedAt());

        return collection.updateOne(eq("_id", id), new Document("$set", set))
                .getModifiedCount() > 0;
    }

    public boolean delete(ObjectId id) {
        return collection.deleteOne(eq("_id", id)).getDeletedCount() > 0;
    }

    public List<Task> findAll(int page, int size, TaskStatus status, String q) {
        List<Bson> filters = new ArrayList<>();
        if (Objects.nonNull(status)) {
            filters.add(eq("status", status.name()));
        }
        if (Objects.nonNull(q) && !q.isBlank()) {
            String pattern = ".*" + java.util.regex.Pattern.quote(q.trim()) + ".*";
            filters.add(or(
                    regex("title", pattern, "i"),
                    regex("description", pattern, "i")));
        }
        Bson finalFilter = filters.isEmpty() ? new Document() : and(filters);
        return collection.find(finalFilter)
                .sort(Sorts.descending("createdAt"))
                .skip(page * size)
                .limit(size)
                .map(this::fromDocument)
                .into(new ArrayList<>());
    }

    private Document toDocument(Task t) {
        Document d = new Document();
        if (t.getId() != null)
            d.put("_id", t.getId());
        d.put("title", t.getTitle());
        d.put("description", t.getDescription());
        d.put("status", t.getStatus().name());
        d.put("createdAt", t.getCreatedAt());
        d.put("updatedAt", t.getUpdatedAt());
        return d;
    }

    private Task fromDocument(Document d) {
        Task t = new Task();
        t.setId(d.getObjectId("_id"));
        t.setTitle(d.getString("title"));
        t.setDescription(d.getString("description"));
        t.setStatus(TaskStatus.valueOf(d.getString("status")));
        t.setCreatedAt(d.get("createdAt", Date.class).toInstant());
        t.setUpdatedAt(d.get("updatedAt", Date.class).toInstant());
        return t;
    }

}
