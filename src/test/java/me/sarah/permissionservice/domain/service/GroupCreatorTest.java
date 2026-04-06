package me.sarah.permissionservice.domain.service;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class GroupCreatorTest {

    private final GroupCreator sut = new GroupCreator();

    @Test
    void should_create_group() {
        // Act
        var result = sut.create("someName", "someDescription");

        // Assert
        assertThat(result.getId().toString()).matches("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");
        assertThat(result.getName()).isEqualTo("someName");
        assertThat(result.getDescription()).isEqualTo("someDescription");
        assertThat(result.getPermissions()).isEqualTo(Collections.emptySet());
    }
}