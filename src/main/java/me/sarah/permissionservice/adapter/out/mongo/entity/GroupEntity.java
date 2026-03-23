package me.sarah.permissionservice.adapter.out.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.util.UUID;


@Document("groups")
    public class GroupEntity {

        @Id
        private UUID id;
        private String name;
        private String description;
        private Set<String> permissions;

        public UUID getId() {
            return id;
        }
        public void setId(UUID id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public Set<String> getPermissions() {
            return permissions;
        }
        public void setPermissions(Set<String>permissions) {
            this.permissions = permissions;
        }
        }

