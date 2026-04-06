package me.sarah.permissionservice.adapter.out.mongo;

import me.sarah.permissionservice.adapter.out.mongo.entity.UserEntity;
import me.sarah.permissionservice.adapter.out.mongo.mapper.UserPersistenceMapper;
import me.sarah.permissionservice.adapter.out.mongo.repository.MongoUserRepository;
import me.sarah.permissionservice.domain.model.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @InjectMocks
    private UserRepositoryAdapter sut;

    @Mock
    private MongoUserRepository mockedMongoUserRepository;

    @Mock
    private UserPersistenceMapper mockedUserPersistenceMapper;

    @Mock
    private UserEntity mockedUserEntity;

    @Mock
    private User mockedUser;

    @Nested
    class findById {

        private static final UUID USER_ID = UUID.fromString("aa44d816-e171-4e49-b9b6-bc26eed2153a");

        @Test
        void should_find_user_by_id() {
            // Arrange
            when(mockedMongoUserRepository.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUserEntity));

            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenReturn(mockedUser);

            // Act
            var result = sut.findById(USER_ID);

            // Assert
            assertThat(result.get()).isEqualTo(mockedUser);
        }

        @Test
        void should_return_empty_optional_when_no_user_has_been_found() {
            // Arrange
            when(mockedMongoUserRepository.findById(USER_ID))
                    .thenReturn(Optional.empty());

            // Act
            var result = sut.findById(USER_ID);

            // Assert
            assertThat(result).isEqualTo(Optional.empty());

            verifyNoInteractions(mockedUserPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_user_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedMongoUserRepository.findById(USER_ID))
                    .thenReturn(Optional.of(mockedUserEntity));
            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findById(USER_ID));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }

        @Test
        void should_delegate_exception_to_caller_when_user_find_fails() {
            var exception = new RuntimeException();

            when(mockedMongoUserRepository.findById(USER_ID))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findById(USER_ID));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }

    @Nested
    class findByUsername {

        private static final String USERNAME = "someUsername";

        @Test
        void should_find_user_by_username() {
            // Arrange
            when(mockedMongoUserRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(mockedUserEntity));

            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenReturn(mockedUser);

            // Act
            var result = sut.findByUsername(USERNAME);

            // Assert
            assertThat(result.get()).isEqualTo(mockedUser);
        }

        @Test
        void should_return_empty_optional_when_no_user_has_been_found() {
            // Arrange
            when(mockedMongoUserRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.empty());

            // Act
            var result = sut.findByUsername(USERNAME);

            // Assert
            assertThat(result).isEqualTo(Optional.empty());

            verifyNoInteractions(mockedUserPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_user_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedMongoUserRepository.findByUsername(USERNAME))
                    .thenReturn(Optional.of(mockedUserEntity));
            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findByUsername(USERNAME));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }

        @Test
        void should_delegate_exception_to_caller_when_user_find_fails() {
            var exception = new RuntimeException();

            when(mockedMongoUserRepository.findByUsername(USERNAME))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findByUsername(USERNAME));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }

    @Nested
    class save {

        private static final String USERNAME = "someUsername";

        @Mock
        private UserEntity mockedUserEntityUnsaved;
        @Mock
        private User mockedUserUnsaved;

        @Test
        void should_save_new_user() {
            // Arrange
            when(mockedUserPersistenceMapper.toEntity(mockedUserUnsaved))
                    .thenReturn(mockedUserEntityUnsaved);
            when(mockedMongoUserRepository.save(mockedUserEntityUnsaved))
                    .thenReturn(mockedUserEntity);
            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenReturn(mockedUser);

            // Act
            var result = sut.save(mockedUserUnsaved);

            // Assert
            assertThat(result).isEqualTo(mockedUser);
        }

        @Test
        void should_delegate_exception_to_caller_when_entity_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedUserPersistenceMapper.toEntity(mockedUserUnsaved))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedUserUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);

            verifyNoMoreInteractions(mockedUserPersistenceMapper);
            verifyNoInteractions(mockedMongoUserRepository);
        }

        @Test
        void should_delegate_exception_to_caller_when_entity_saving_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedUserPersistenceMapper.toEntity(mockedUserUnsaved))
                    .thenReturn(mockedUserEntityUnsaved);
            when(mockedMongoUserRepository.save(mockedUserEntityUnsaved))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedUserUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);

            verifyNoMoreInteractions(mockedUserPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_saved_entity_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedUserPersistenceMapper.toEntity(mockedUserUnsaved))
                    .thenReturn(mockedUserEntityUnsaved);
            when(mockedMongoUserRepository.save(mockedUserEntityUnsaved))
                    .thenReturn(mockedUserEntity);
            when(mockedUserPersistenceMapper.toDomain(mockedUserEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedUserUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }
}