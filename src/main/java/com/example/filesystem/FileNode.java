package com.example.filesystem;

public class FileNode implements Element {
    private final String name;
    private final long size;
    private DirectoryNode parent;

    public FileNode(String name, long size) {
        if (size < 0) {
            throw new IllegalArgumentException("File size cannot be negative");
        }
        this.name = name;
        this.size = size;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this); // Double dispatch
    }

    @Override
    public String getName() { return name; }
    public long getSize() { return size; }
    public DirectoryNode getParent() { return parent; }
    void setParent(DirectoryNode parent) { this.parent = parent; }
}