package me.sarah.permissionservice.domain.service;

import me.sarah.permissionservice.domain.exception.DomainException;
import me.sarah.permissionservice.domain.exception.GroupNotFoundException;
import me.sarah.permissionservice.domain.exception.UserAlreadyExistsException;
import me.sarah.permissionservice.domain.exception.UserNotFoundException;
import me.sarah.permissionservice.domain.model.Group;
import me.sarah.permissionservice.domain.model.User;
import me.sarah.permissionservice.port.out.GroupRepositoryPort;
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
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserServiceTest {

    private static final String USER_NAME = "someUserName";
    private static final String USER_EMAIL = "someEmail";
    private static final UUID USER_ID = UUID.fromString("e49e08b3-b970-48c4-9d2b-1b5b4881fd6d");
    private static final UUID GROUP_ID = UUID.fromString("e49e08b3-b970-48c4-aaaa-1b5b4881fd6d");
    private static final String GROUP_DESCRIPTION = "someGroupDescription";


    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepositoryPort mockedUserRepositoryPort;

    @Mock
    private GroupRepositoryPort mockedGroupRepositoryPort;

    @Mock
    private UserCreator mockedUserCreator;

    @Mock
    private User mockedUser;

    @Mock
    private Group mockedGroup;

    @Nested
    class CreateUser {

        @Test
        void Test_should_createUser() {
            //Given
            when(mockedUserRepositoryPort.findByUsername(USER_NAME))
                    .thenReturn(Optional.empty());
            when(mockedUserCreator.create(any(UUID.class), eq(USER_NAME), eq(USER_EMAIL)))
                    .thenReturn(mockedUser);
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenReturn(mockedUser);

            User result = sut.createUser(USER_NAME, USER_EMAIL);

            assertThat(result).isEqualTo(mockedUser);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findByUsername(USER_NAME))
                    .thenReturn(Optional.empty());
            when(mockedUserCreator.create(any(UUID.class), eq(USER_NAME), eq(USER_EMAIL)))
                    .thenReturn(mockedUser);
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.createUser(USER_NAME, USER_EMAIL));

        }

        @Test
        void Test_should_throw_when_username_already_exists() {
            when(mockedUserRepositoryPort.findByUsername(USER_NAME))
                    .thenReturn(Optional.of((mockedUser)));

            assertThrows(UserAlreadyExistsException.class,
                    () -> sut.createUser(USER_NAME, USER_EMAIL));
        }
    }

    @Nested
    class GetUserById {

        @Test
        void Test_should_getUserById() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));

            User result = sut.getUserById(USER_ID);

            assertThat(result).isEqualTo(mockedUser);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.getUserById(USER_ID));
        }

        @Test
        void Test_should_throw_when_user_does_not_exist() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> sut.getUserById(USER_ID));
        }
    }

    @Nested
    class AddUserToGroup {

        @Test
        void Test_should_add_group_and_save_user() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));

            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));

            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenReturn(mockedUser);

            User result = sut.addUserToGroup(USER_ID, GROUP_ID);

            assertThat(result).isEqualTo(mockedUser);
            verify(mockedUser).addGroup(GROUP_ID);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroup));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenThrow(DomainException.class);

            assertThrows(DomainException.class,
                    () -> sut.addUserToGroup(USER_ID, GROUP_ID));
        }

        @Test
        void Test_should_throw_when_group_does_not_exist() {

            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedGroupRepositoryPort.findById(GROUP_ID))
                    .thenReturn(Optional.empty());

            assertThrows(GroupNotFoundException.class,
                    () -> sut.addUserToGroup(USER_ID, GROUP_ID));
        }
    }

    @Nested
    class RemoveUserFromGroup {

        @Test
        void Test_should_removeUserFromGroup() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenReturn(mockedUser);

            sut.removeUserFromGroup(USER_ID, GROUP_ID);

            verify(mockedUser).removeGroup(GROUP_ID);
        }

        @Test
        void Test_should_handle_failed_repository() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUser));
            when(mockedUserRepositoryPort.save(mockedUser))
                    .thenThrow(DomainException.class);
            assertThrows(DomainException.class,
                    () -> sut.removeUserFromGroup(USER_ID, GROUP_ID));
        }

        @Test
        void Test_should_throw_UserNotFoundException_when_removeUserFromGroup_and_user_does_not_exist() {
            when(mockedUserRepositoryPort.findById(USER_ID))
                    .thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> sut.removeUserFromGroup(USER_ID, GROUP_ID));
        }
    }
}
