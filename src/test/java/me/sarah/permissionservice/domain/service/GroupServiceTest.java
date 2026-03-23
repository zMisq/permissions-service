package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.DomainException;
import me.sarah.permissionservice.domain.exception.GroupAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class GroupServiceTest {

    private static final String GROUP_NAME = "someGroupName";
    private static final String GROUP_DESCRIPTION = "someGroupDescription";
    private static final UUID GROUP_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @InjectMocks
    private GroupService sut;

    @Mock
    private GroupRepositoryPort mockedGroupRepositoryPort;

    @Mock
    private GroupCreator mockedGroupCreator;

    @Mock
    private Group mockedGroup;

    @Nested
    class CreateGroup {

        @Test
        void Test_should_createGroup() {
            // Given
            when(mockedGroupRepositoryPort.findByName(GROUP_NAME))
                    .thenReturn(Optional.empty());
            when(mockedGroupCreator.create(GROUP_NAME, GROUP_DESCRIPTION))
                    .thenReturn(mockedGroup);
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenReturn(mockedGroup);

            // When
            Group result = sut.createGroup(GROUP_NAME, GROUP_DESCRIPTION);

            // Then
            assertThat(result).isEqualTo(mockedGroup);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedGroupRepositoryPort.findByName(GROUP_NAME))
                    .thenReturn(Optional.empty());
            when(mockedGroupCreator.create(GROUP_NAME, GROUP_DESCRIPTION))
                    .thenReturn(mockedGroup);
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.createGroup(GROUP_NAME, GROUP_DESCRIPTION));

        }

        @Test
        void Test_should_throw_exception_when_name_already_exists() {
            // Given
            when(mockedGroupRepositoryPort.findByName(GROUP_NAME))
                    .thenReturn(Optional.of(mockedGroup));

            // When / Then
            assertThrows(GroupAlreadyExistsException.class,
                    () -> sut.createGroup(GROUP_NAME, GROUP_DESCRIPTION));
        }
    }

    @Nested
    class GetGroupById {

        @Test
        void Test_should_getGroupById() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));

            // When
            Group result = sut.getGroupById(GROUP_ID);

            // Then
            assertThat(result).isEqualTo(mockedGroup);
        }

        @Test
        void Test_should_throw_when_group_does_not_exist() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.empty());

            // When / Then
            assertThrows(GroupNotFoundException.class, () -> sut.getGroupById(GROUP_ID));
        }
    }

    @Nested
    class AssignPermission {

        @Test
        void Test_should_add_permission_and_save_group() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenReturn(mockedGroup);

            // When
            sut.assignPermission(GROUP_ID, "USER_READ");

            // Then
            verify(mockedGroup).addPermission("USER_READ");
            verify(mockedGroupRepositoryPort).save(mockedGroup);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenThrow(DomainException.class);
            assertThrows(DomainException.class,
                    () -> sut.assignPermission(GROUP_ID, "USER_READ"));

        }

        @Test
        void Test_should_throw_GroupNotFoundException_when_assignPermission_and_group_does_not_exist() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.empty());

            // When / Then
            assertThrows(GroupNotFoundException.class, () -> sut.assignPermission(GROUP_ID,
                    "USER_READ"));
        }
    }

    @Nested
    class RemovePermission {

        @Test
        void Test_should_removePermission() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenReturn(mockedGroup);

            // When
            sut.removePermission(GROUP_ID, "USER_READ");

            // Then
            verify(mockedGroup).removePermission("USER_READ");
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));
            when(mockedGroupRepositoryPort.save(mockedGroup))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.removePermission(GROUP_ID, "USER_READ"));
        }

        @Test
        void Test_should_throw_GroupNotFoundException_when_removePermission_and_group_does_not_exist() {
            // Given
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.empty());

            // When / Then
            assertThrows(GroupNotFoundException.class,
                    () -> sut.removePermission(GROUP_ID, "USER_READ"));
        }
    }
}
