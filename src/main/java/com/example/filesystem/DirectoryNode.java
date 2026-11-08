package com.example.filesystem;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class DirectoryNode implements Element {
    private final String name;
    private DirectoryNode parent;
    private final Map<String, Element> children = new TreeMap<>();

    public DirectoryNode(String name) {
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this); // Double dispatch
    }

    @Override
    public String getName() { return name; }
    public DirectoryNode getParent() { return parent; }
    void setParent(DirectoryNode parent) { this.parent = parent; }

    public Collection<Element> getChildren() {
        return Collections.unmodifiableCollection(children.values());
    }

    public void addChild(Element child) {
        if (children.containsKey(child.getName())) {
            throw new IllegalArgumentException("Duplicate sibling name");
        }
        if (child instanceof FileNode f && f.getParent() != null ||
                child instanceof DirectoryNode d && d.getParent() != null) {
            throw new IllegalArgumentException("Element already has a parent");
        }
        if (isCycle(child)) {
            throw new IllegalArgumentException("Cycle detected in the tree");
        }

        children.put(child.getName(), child);
        if (child instanceof FileNode f) f.setParent(this);
        if (child instanceof DirectoryNode d) d.setParent(this);
    }

    private boolean isCycle(Element child) {
        if (this == child) return true;
        DirectoryNode current = this.parent;
        while (current != null) {
            if (current == child) return true;
            current = current.parent;
        }
        return false;
    }
}