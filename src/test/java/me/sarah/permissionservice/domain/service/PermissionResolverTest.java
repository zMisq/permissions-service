package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PermissionResolverTest {

    private static final UUID GROUP_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @InjectMocks
    private PermissionResolver sut;

    @Mock
    private GroupRepositoryPort mockedGroupRepositoryPort;

    @Mock
    private Group mockedGroup;

    @Mock
    private User mockedUser;

    @Test
    void Test_should_merge_direct_and_group_permissions() {
        when(mockedUser.getDirectPermissions())
                .thenReturn(Set.of("USER_READ", "USER_WRITE"));
        when(mockedUser.getGroupIds())
                .thenReturn(Set.of(GROUP_ID));
        when(mockedGroup.getPermissions())
                .thenReturn(Set.of("GROUP_READ", "GROUP_WRITE"));
        when(mockedGroupRepositoryPort.findById(GROUP_ID))
                .thenReturn(Optional.of(mockedGroup));

        Set<String> result = sut.resolvePermissions(mockedUser);

        assertThat(result).contains("USER_READ", "USER_WRITE", "GROUP_READ", "GROUP_WRITE");
    }

    @Test
    void Test_should_throw_when_group_is_missing() {
        when(mockedUser.getGroupIds())
                .thenReturn(Set.of(GROUP_ID));
        when(mockedGroupRepositoryPort.findById(GROUP_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> sut.resolvePermissions(mockedUser))
                .isInstanceOf(GroupNotFoundException.class);
    }
}
