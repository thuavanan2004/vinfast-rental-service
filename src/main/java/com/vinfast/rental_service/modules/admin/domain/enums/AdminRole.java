package com.vinfast.rental_service.modules.admin.domain.enums;

public enum AdminRole {
    SUPER_ADMIN("super_admin"),
    ADMIN("admin"),
    SUPPORT("support"),
    CONTENT_MANAGER("content_manager");

    private final String value;

    AdminRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
