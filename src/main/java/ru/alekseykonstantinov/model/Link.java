package ru.alekseykonstantinov.model;

public record Link(
        String link
) {

    @Override
    public String toString() {
        return link + '\'';
    }
}
