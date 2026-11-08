package com.example.filesystem;

public class StatisticsVisitor implements Visitor {
    private int fileCount = 0;
    private int directoryCount = 0;
    private int maxDepth = 0;
    private int currentDepth = -1;

    public int getFileCount() { return fileCount; }
    public int getDirectoryCount() { return directoryCount; }
    public int getMaxDepth() { return maxDepth; }

    public void reset() {
        fileCount = 0; directoryCount = 0; maxDepth = 0; currentDepth = -1;
    }

    @Override
    public void visit(FileNode file) {
        fileCount++;
    }

    @Override
    public void visit(DirectoryNode directory) {
        directoryCount++;
        currentDepth++;
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }

        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        currentDepth--;
    }
}