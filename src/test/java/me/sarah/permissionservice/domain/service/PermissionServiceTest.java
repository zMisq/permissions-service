package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.DomainException;
import me.sarah.permissionservice.domain.exception.UserNotFoundException;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.out.UserRepositoryPort;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PermissionServiceTest {

    private static final UUID USER_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");

    @InjectMocks
    private PermissionService sut;

    @Mock
    private UserRepositoryPort mockedUserRepositoryPort;

    @Mock
    private PermissionResolver mockedPermissionResolver;

    @Mock
    private User mockedUser;


    @Nested
    class assignDirectPermission {

        @Test
        void Test_should_add_direct_permission_and_save_user() {

            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenReturn(mockedUser);

            sut.assignDirectPermission(USER_ID, "USER_READ");

            verify(mockedUser).addPermission("USER_READ");
            verify(mockedUserRepositoryPort).save(mockedUser);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.assignDirectPermission(USER_ID, "USER_READ"));

        }

        @Test
        void Test_should_throw_UserNotFoundException_when_assignDirectPermission_and_user_does_not_exist() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> sut.assignDirectPermission(USER_ID, "USER_READ"));
        }

    }

    @Nested
    class removeDirectPermission {

        @Test
        void Test_should_removeDirectPermission() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenReturn(mockedUser);

            User result = sut.removeDirectPermission(USER_ID, "USER_READ");

            assertFalse(result.getDirectPermissions().contains("USER_READ"));
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.removeDirectPermission(USER_ID, "USER_READ"));

        }

        @Test
        void Test_should_throw_UserNotFoundException_when_removeDirectPermission_and_user_does_not_exist() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> sut.removeDirectPermission(USER_ID, "USER_READ"));
        }
    }

    @Nested
    class getEffectivePermissions {

        @Test
        void Test_should_delegate_to_permission_resolver() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedPermissionResolver.resolvePermissions(mockedUser))
                    .thenReturn(Set.of("USER_READ"));

            Set<String> result = sut.getEffectivePermissions(USER_ID);

            assertEquals(Set.of("USER_READ"), result);
        }

        @Test
        void Test_should_handle_failed_PermissionResolver() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedPermissionResolver.resolvePermissions(mockedUser))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.getEffectivePermissions(USER_ID));

        }

        @Test
        void Test_should_throw_when_user_is_missing() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class,
                    () -> sut.getEffectivePermissions(USER_ID));
        }
    }

    @Nested
    class hasPermission {

        @Test
        void Test_should_return_true_when_permission_is_present() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedPermissionResolver.resolvePermissions(mockedUser))
                    .thenReturn(Set.of("USER_READ"));

            assertTrue(sut.hasPermission(USER_ID, "USER_READ"));
            assertFalse(sut.hasPermission(USER_ID, "USER_WRITE"));
        }

        @Test
        void Test_should_throw_UserNotFoundException_when_hasPermission_and_user_does_not_exist() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> sut.hasPermission(USER_ID, "USER_READ"));
        }

    }
}
