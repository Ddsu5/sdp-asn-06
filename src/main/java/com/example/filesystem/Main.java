package com.example.filesystem;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== In-Memory File System Demo ===");

        DirectoryNode root = new DirectoryNode("root");
        DirectoryNode src = new DirectoryNode("src");
        DirectoryNode docs = new DirectoryNode("docs");
        DirectoryNode test = new DirectoryNode("test");

        FileNode mainJava = new FileNode("Main.java", 1500);
        FileNode utilsJava = new FileNode("Utils.java", 800);
        FileNode readme = new FileNode("README.md", 450);
        FileNode specPdf = new FileNode("Spec.pdf", 3200);
        FileNode mainTest = new FileNode("MainTest.java", 1100);

        src.addChild(mainJava);
        src.addChild(utilsJava);
        docs.addChild(readme);
        docs.addChild(specPdf);
        test.addChild(mainTest);

        root.addChild(src);
        root.addChild(docs);
        root.addChild(test);

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        System.out.println("\n[1] Total Size Operation:");
        System.out.println("Total Size of 'root': " + sizeVisitor.getTotalSize() + " bytes");

        SearchVisitor searchVisitor = new SearchVisitor(".java", 1000);
        root.accept(searchVisitor);
        System.out.println("\n[2] Search Operation (Criteria: contains '.java', size >= 1000):");
        searchVisitor.getMatchingPaths().forEach(path -> System.out.println(" - " + path));

        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        root.accept(statsVisitor);
        System.out.println("\n[3] Statistics Operation:");
        System.out.println("File Count: " + statsVisitor.getFileCount());
        System.out.println("Directory Count: " + statsVisitor.getDirectoryCount());
        System.out.println("Max Depth: " + statsVisitor.getMaxDepth());
    }
}