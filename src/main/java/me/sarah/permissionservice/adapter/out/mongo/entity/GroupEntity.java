package me.sarah.permissionservice.adapter.out.mongo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;


@Document("groups")
@TypeAlias("group")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupEntity {

    @Id
    private UUID id;
    private String name;
    private String description;
    private Set<String> permissions;
}

