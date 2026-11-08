package com.example.filesystem;

import com.example.filesystem.Visitor;

public interface Element {
    void accept(Visitor visitor);
    String getName();
}