package me.sarah.permissionservice.adapter.out.mongo.repository;

import com.mongodb.BasicDBObjectBuilder;
import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
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
public class UserRepositoryIntegrationtest {

    private static final UUID USER_ID = UUID.fromString("bebd9c13-d38a-43de-a5a6-c4837c184532");
    private static final UUID GROUP_ID = UUID.fromString("bb454056-0e50-4021-a216-884785ac9a7b");
    private static final UUID ANOTHER_GROUP_ID = UUID.fromString("2fd3bde6-4afb-4dd8-8f33-ca064fabae01");

    private static final String COLLECTION_NAME = "users";

    @Container
    private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:8.2.6").withReplicaSet();

    @Autowired
    private MongoUserRepository userRepositoryPort;

    @Autowired
    private MongoTemplate mongoTemplate;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
    }

    @AfterEach
    void cleanUp() {
        mongoTemplate.remove(new BasicQuery("{}"), COLLECTION_NAME);
    }

    @Test
    void should_find_inserted_user() {
        // Arrange
        mongoTemplate.save(
                BasicDBObjectBuilder.start()
                        .add("_id", USER_ID)
                        .add("username", "someUsername")
                        .add("email", "someEmail")
                        .add("directPermissions", Set.of("SOME_DIRECT_PERMISSION"))
                        .add("groupIds", Set.of(GROUP_ID.toString(),
                                ANOTHER_GROUP_ID.toString()))
                        .get(),
                COLLECTION_NAME
        );

        // Act
        var loadedUser = userRepositoryPort.findById(USER_ID);

        // Assert
        assertThat(loadedUser).isPresent();
        assertThat(loadedUser.get().getId()).isEqualTo(USER_ID);
        assertThat(loadedUser.get().getUsername()).isEqualTo("someUsername");
        assertThat(loadedUser.get().getEmail()).isEqualTo("someEmail");
        assertThat(loadedUser.get().getDirectPermissions()).containsExactly("SOME_DIRECT_PERMISSION");
        assertThat(loadedUser.get().getGroupIds()).containsExactlyInAnyOrder(
                GROUP_ID.toString(),
                ANOTHER_GROUP_ID.toString()
        );
    }

    @Test
    void should_persist_user_with_expected_raw_document_state() {
        // Arrange
        UserEntity userEntity = new UserEntity(
                USER_ID,
                "someUsername",
                "someEmail",
                Set.of("SOME_DIRECT_PERMISSION"),
                Set.of(GROUP_ID.toString(), ANOTHER_GROUP_ID.toString())
        );

        // Act
        userRepositoryPort.save(userEntity);

        // Assert
        var rawDocument = mongoTemplate.getCollection(COLLECTION_NAME)
                .find(new Document("_id", USER_ID))
                .first();

        var directPermissions = rawDocument.get("directPermissions", List.class);
        var groupIds = rawDocument.get("groupIds", List.class);

        assertThat(rawDocument).isNotNull();
        assertThat(rawDocument.get("_id")).isEqualTo(USER_ID);
        assertThat(rawDocument.get("username")).isEqualTo("someUsername");
        assertThat(rawDocument.get("email")).isEqualTo("someEmail");
        assertThat(directPermissions).containsExactlyInAnyOrder("SOME_DIRECT_PERMISSION");
        assertThat(groupIds).containsExactlyInAnyOrder(
                GROUP_ID.toString(),
                ANOTHER_GROUP_ID.toString()
        );
    }
}
