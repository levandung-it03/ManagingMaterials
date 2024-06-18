package com.CSDLPT.ManagingMaterials.EN_Account;

public enum RoleEnum {
    CHINHANH("branch"),
    CONGTY("company"),
    USER("user");

    private final String role;

    RoleEnum(String role) {
        this.role = role;
    }

    public String getJavaRole() {
        return role;
    }
}
