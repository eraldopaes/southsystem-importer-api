package br.com.southsystem.importer.domain.enums;

public enum BucketTypeEnum {

    FILE_INPUT("file-input"),
    FILE_OUTPUT("file-output");

    private String name;

    BucketTypeEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
