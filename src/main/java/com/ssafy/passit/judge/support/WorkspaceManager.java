package com.ssafy.passit.judge.support;

import com.ssafy.passit.common.type.LanguageType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceManager {

    private static final String ROOT_DIR = "passit-judge";
    private final Path rootDirectory;

    public WorkspaceManager(@Value("${judge.workspace-root:}") String configuredRoot) {
        if (configuredRoot != null && !configuredRoot.isBlank()) {
            this.rootDirectory = Path.of(configuredRoot);
            return;
        }
        this.rootDirectory = Path.of(System.getProperty("java.io.tmpdir"), ROOT_DIR);
    }

    public Path createWorkspace(String key, LanguageType language) throws IOException {
        Files.createDirectories(rootDirectory);
        Path workspace = rootDirectory.resolve(key + "-" + language.name().toLowerCase());
        deleteWorkspace(workspace);
        return Files.createDirectories(workspace);
    }

    public Path createWorkspace(Long submissionId, LanguageType language) throws IOException {
        return createWorkspace("sub-" + submissionId, language);
    }

    public Path writeSourceFile(Path workspace, String sourceCode, LanguageType language) throws IOException {
        Path sourceFile = workspace.resolve(resolveFileName(language));
        Files.writeString(sourceFile, sourceCode, StandardCharsets.UTF_8);
        return sourceFile;
    }

    public void deleteWorkspace(Path workspace) throws IOException {
        if (workspace == null || !Files.exists(workspace)) {
            return;
        }

        try (var stream = Files.walk(workspace)) {
            stream.sorted(Comparator.reverseOrder())
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException ignored) {
                    }
                });
        }
    }

    private String resolveFileName(LanguageType language) {
        return switch (language) {
            case PYTHON -> "main.py";
            case JAVA -> "Main.java";
            case CPP -> "main.cpp";
        };
    }
}
