package com.example.filesystem;

public interface Visitor {
    void visit(FileNode file);
    void visit(DirectoryNode directory);
}