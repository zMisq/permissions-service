package me.sarah.permissionservice.adapter.out.mongo.repository;

import com.mongodb.BasicDBObjectBuilder;
import me.sarah.permissionservice.adapter.out.mongo.entity.GroupEntity;
import org.bson.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mongodb.MongoDBContainer;


import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@Testcontainers
public class GroupRepositoryIntegrationtest {

    private static final UUID GROUP_ID = UUID.fromString("bb454056-0e50-4021-a216-884785ac9a7b");
    private static final String COLLECTION_NAME = "groups";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:8.2.6").withReplicaSet();

    @Autowired
    private MongoGroupRepository groupRepositoryPort;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @AfterEach
    void cleanUp() { mongoTemplate.remove(new BasicQuery("{}"), COLLECTION_NAME); }

    @Test
    void should_find_inserted_group() {
        mongoTemplate.save(
                BasicDBObjectBuilder.start()
                        .add("_id", GROUP_ID)
                        .add("name", "admin")
                        .add("description", "Admin group")
                        .add("permissions", Set.of("USER_READ",
                                "USER_WRITE"))
                        .get(),
                COLLECTION_NAME
        );

        var loadedGroup = groupRepositoryPort.findById(GROUP_ID);

        assertThat(loadedGroup).isPresent();
        assertThat(loadedGroup.get().getId()).isEqualTo(GROUP_ID);
        assertThat(loadedGroup.get().getName()).isEqualTo("admins");

        assertThat(loadedGroup.get().getDescription()).isEqualTo("Admin group");
                assertThat(loadedGroup.get().getPermissions())
                        .containsExactlyInAnyOrder("USER_READ",
                                "USER_WRITE");
    }

    @Test
    void should_persist_group_with_expected_raw_document_state() {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(GROUP_ID);
        groupEntity.setName("admins");
        groupEntity.setDescription("Admin group");
        groupEntity.setPermissions(Set.of("USER_READ",
                "USER_WRITE"));

        groupRepositoryPort.save(groupEntity);

        var rawDocument = mongoTemplate.getCollection(COLLECTION_NAME)
                        .find(new Document("_id", GROUP_ID))
                        .first();

        var permissions = (List<String>)
                rawDocument.get("permissions");

        assertThat(rawDocument).isNotNull();
        assertThat(rawDocument.get("_id")).isEqualTo(GROUP_ID);
        assertThat(rawDocument.get("name")).isEqualTo("admins");
        assertThat(rawDocument.get("description")).isEqualTo("Admin group");

                assertThat(permissions).containsExactlyInAnyOrder("USER_READ",
                        "USER_WRITE");
    }
}

