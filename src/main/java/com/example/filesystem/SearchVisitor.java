package com.example.filesystem;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class SearchVisitor implements Visitor {
    private final String nameCriterion;
    private final long minThreshold;
    private final List<String> matchingPaths = new ArrayList<>();
    private final Deque<String> currentPath = new ArrayDeque<>();

    public SearchVisitor(String nameCriterion, long minThreshold) {
        this.nameCriterion = nameCriterion;
        this.minThreshold = minThreshold;
    }

    public List<String> getMatchingPaths() { return new ArrayList<>(matchingPaths); }
    public void reset() { matchingPaths.clear(); currentPath.clear(); }

    @Override
    public void visit(FileNode file) {
        if (file.getName().contains(nameCriterion) && file.getSize() >= minThreshold) {
            matchingPaths.add(buildPath() + "/" + file.getName());
        }
    }

    @Override
    public void visit(DirectoryNode directory) {
        currentPath.addLast(directory.getName());
        for (Element child : directory.getChildren()) {
            child.accept(this);
        }
        currentPath.removeLast();
    }

    private String buildPath() {
        return String.join("/", currentPath);
    }
}