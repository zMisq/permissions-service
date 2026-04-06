package me.sarah.permissionservice.adapter.out.mongo;

import me.sarah.permissionservice.adapter.out.mongo.entity.GroupEntity;
import me.sarah.permissionservice.adapter.out.mongo.mapper.GroupPersistenceMapper;
import me.sarah.permissionservice.adapter.out.mongo.repository.MongoGroupRepository;
import me.sarah.permissionservice.domain.model.Group;
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
class GroupRepositoryAdapterTest {

    @InjectMocks
    private GroupRepositoryAdapter sut;

    @Mock
    private MongoGroupRepository mockedMongoGroupRepository;

    @Mock
    private GroupPersistenceMapper mockedGroupPersistenceMapper;

    @Mock
    private GroupEntity mockedGroupEntity;

    @Mock
    private Group mockedGroup;

    @Nested
    class findById {

        private static final UUID GROUP_ID = UUID.fromString("aa44d814-e171-4e49-b9b6-bc26eed2153a");

        @Test
        void should_find_group_by_id() {
            // Arrange
            when(mockedMongoGroupRepository.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroupEntity));

            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenReturn(mockedGroup);

            // Act
            var result = sut.findById(GROUP_ID);

            // Assert
            assertThat(result.get()).isEqualTo(mockedGroup);
        }

        @Test
        void should_return_empty_optional_when_no_group_has_been_found() {
            // Arrange
            when(mockedMongoGroupRepository.findById(GROUP_ID))
                    .thenReturn(Optional.empty());

            // Act
            var result = sut.findById(GROUP_ID);

            // Assert
            assertThat(result).isEqualTo(Optional.empty());

            verifyNoInteractions(mockedGroupPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_group_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedMongoGroupRepository.findById(GROUP_ID))
                    .thenReturn(Optional.of(mockedGroupEntity));
            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findById(GROUP_ID));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }

        @Test
        void should_delegate_exception_to_caller_when_group_find_fails() {
            var exception = new RuntimeException();

            when(mockedMongoGroupRepository.findById(GROUP_ID))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findById(GROUP_ID));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }

    @Nested
    class findByName {

        private static final String GROUP_NAME = "someGroupName";

        @Test
        void should_find_group_by_group_name() {
            // Arrange
            when(mockedMongoGroupRepository.findByName(GROUP_NAME))
                    .thenReturn(Optional.of(mockedGroupEntity));

            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenReturn(mockedGroup);

            // Act
            var result = sut.findByName(GROUP_NAME);

            // Assert
            assertThat(result.get()).isEqualTo(mockedGroup);
        }

        @Test
        void should_return_empty_optional_when_no_group_has_been_found() {
            // Arrange
            when(mockedMongoGroupRepository.findByName(GROUP_NAME))
                    .thenReturn(Optional.empty());

            // Act
            var result = sut.findByName(GROUP_NAME);

            // Assert
            assertThat(result).isEqualTo(Optional.empty());

            verifyNoInteractions(mockedGroupPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_group_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedMongoGroupRepository.findByName(GROUP_NAME))
                    .thenReturn(Optional.of(mockedGroupEntity));
            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findByName(GROUP_NAME));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }

        @Test
        void should_delegate_exception_to_caller_when_group_find_fails() {
            var exception = new RuntimeException();

            when(mockedMongoGroupRepository.findByName(GROUP_NAME))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.findByName(GROUP_NAME));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }

    @Nested
    class save {

        @Mock
        private GroupEntity mockedGroupEntityUnsaved;

        @Mock
        private Group mockedGroupUnsaved;

        @Test
        void should_save_new_group() {
            // Arrange
            when(mockedGroupPersistenceMapper.toEntity(mockedGroupUnsaved))
                    .thenReturn(mockedGroupEntityUnsaved);
            when(mockedMongoGroupRepository.save(mockedGroupEntityUnsaved))
                    .thenReturn(mockedGroupEntity);
            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenReturn(mockedGroup);

            // Act
            var result = sut.save(mockedGroupUnsaved);

            // Assert
            assertThat(result).isEqualTo(mockedGroup);
        }

        @Test
        void should_delegate_exception_to_caller_when_entity_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedGroupPersistenceMapper.toEntity(mockedGroupUnsaved))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedGroupUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);

            verifyNoMoreInteractions(mockedGroupPersistenceMapper);
            verifyNoInteractions(mockedMongoGroupRepository);
        }

        @Test
        void should_delegate_exception_to_caller_when_entity_saving_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedGroupPersistenceMapper.toEntity(mockedGroupUnsaved))
                    .thenReturn(mockedGroupEntityUnsaved);
            when(mockedMongoGroupRepository.save(mockedGroupEntityUnsaved))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedGroupUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);

            verifyNoMoreInteractions(mockedGroupPersistenceMapper);
        }

        @Test
        void should_delegate_exception_to_caller_when_saved_entity_mapping_fails() {
            // Arrange
            var exception = new RuntimeException();

            when(mockedGroupPersistenceMapper.toEntity(mockedGroupUnsaved))
                    .thenReturn(mockedGroupEntityUnsaved);
            when(mockedMongoGroupRepository.save(mockedGroupEntityUnsaved))
                    .thenReturn(mockedGroupEntity);
            when(mockedGroupPersistenceMapper.toDomain(mockedGroupEntity))
                    .thenThrow(exception);

            // Act
            var thrown = catchThrowable(() -> sut.save(mockedGroupUnsaved));

            // Assert
            assertThat(thrown).isEqualTo(exception);
        }
    }

}