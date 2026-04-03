package me.sarah.permissionservice.adapter.out.mongo.mapper;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import me.sarah.permissionservice.domain.model.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserPersistenceMapperTest {

    private final UserPersistenceMapper sut = new UserPersistenceMapper();

    @Test
    void Test_should_map_entity_to_domain_including_permissions_and_groupIds() {
        UUID userId = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");
        UUID groupId = UUID.fromString("e49e08b3-b970-48c4-aaaa-1b5b4881fd6d");

        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setUsername("SomeUsername");
        entity.setEmail("someUser@example.com");
        entity.setDirectPermissions(Set.of("USER_READ"));
        entity.setGroupIds(Set.of(groupId.toString()));

        User result = sut.toDomain(entity);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo("SomeUsername");
        assertThat(result.getEmail()).isEqualTo("someUser@example.com");
        assertThat(result.getDirectPermissions()).containsExactly("USER_READ");
        assertThat(result.getGroupIds()).containsExactly(groupId);
    }

    @Test
    void Test_should_map_domain_to_entity_including_permissions_and_groupIds() {
        UUID userId = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");
        UUID groupId = UUID.fromString("e49e08b3-b970-48c4-aaaa-1b5b4881fd6d");

        User user = new User(
                userId,
                "SomeUser",
                "someUser@example.com",
                Set.of("USER_READ"),
                Set.of(groupId));

        UserEntity result = sut.toEntity(user);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getUsername()).isEqualTo("SomeUser");
        assertThat(result.getEmail()).isEqualTo("someUser@example.com");
        assertThat(result.getDirectPermissions()).containsExactly("USER_READ");
        assertThat(result.getGroupIds()).containsExactly(groupId.toString());
    }
}