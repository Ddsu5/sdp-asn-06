package com.example.filesystem;

import com.example.filesystem.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {
    private DirectoryNode root;
    private DirectoryNode src;
    private DirectoryNode test;
    private FileNode mainJava;
    private FileNode helperJava;
    private FileNode testJava;

    @BeforeEach
    void setUp() {
        root = new DirectoryNode("root");
        src = new DirectoryNode("src");
        test = new DirectoryNode("test");
        mainJava = new FileNode("Main.java", 150);
        helperJava = new FileNode("Helper.java", 50);
        testJava = new FileNode("MainTest.java", 300);

        src.addChild(mainJava);
        src.addChild(helperJava);
        test.addChild(testJava);
        root.addChild(src);
        root.addChild(test);
    }

    @Test
    void testT1_DoubleDispatchAndContracts() {
        TotalSizeVisitor visitor = new TotalSizeVisitor();
        mainJava.accept(visitor);
        assertEquals(150, visitor.getTotalSize());

        visitor.reset();
        root.accept(visitor);
        assertTrue(visitor.getTotalSize() > 150);
    }

    @Test
    void testT2_SizeAccumulation() {
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        assertEquals(500, sizeVisitor.getTotalSize());
    }

    @Test
    void testT3_SearchCombinesFilters() {
        SearchVisitor searchVisitor = new SearchVisitor("Main", 100);
        root.accept(searchVisitor);
        List<String> paths = searchVisitor.getMatchingPaths();

        assertEquals(2, paths.size());
        assertFalse(paths.contains("root/src/Helper.java"));
    }

    @Test
    void testT4_SearchDeterministicPaths() {
        SearchVisitor searchVisitor = new SearchVisitor(".java", 0);
        root.accept(searchVisitor);
        List<String> paths = searchVisitor.getMatchingPaths();

        assertEquals("root/src/Helper.java", paths.get(0));
        assertEquals("root/src/Main.java", paths.get(1));
        assertEquals("root/test/MainTest.java", paths.get(2));
    }

    @Test
    void testT5_StatisticsCorrectness() {
        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        root.accept(statsVisitor);

        assertEquals(3, statsVisitor.getFileCount());
        assertEquals(3, statsVisitor.getDirectoryCount());
        assertEquals(1, statsVisitor.getMaxDepth());
    }

    @Test
    void testT6_EmptyStructureAndNoMatch() {
        DirectoryNode emptyRoot = new DirectoryNode("empty");

        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        emptyRoot.accept(sizeVisitor);
        assertEquals(0, sizeVisitor.getTotalSize());

        StatisticsVisitor statsVisitor = new StatisticsVisitor();
        emptyRoot.accept(statsVisitor);
        assertEquals(0, statsVisitor.getFileCount());
        assertEquals(1, statsVisitor.getDirectoryCount());
        assertEquals(0, statsVisitor.getMaxDepth());

        SearchVisitor searchVisitor = new SearchVisitor("NonExistent", 0);
        root.accept(searchVisitor);
        assertTrue(searchVisitor.getMatchingPaths().isEmpty());
    }

    @Test
    void testT7_RepeatExecutionsDoNotMultiplyState() {
        TotalSizeVisitor sizeVisitor = new TotalSizeVisitor();
        root.accept(sizeVisitor);
        assertEquals(500, sizeVisitor.getTotalSize());

        sizeVisitor.reset();
        root.accept(sizeVisitor);
        assertEquals(500, sizeVisitor.getTotalSize());
    }

    @Test
    void testT8_InvalidTreeMutationsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new FileNode("bad.txt", -10));

        assertThrows(IllegalArgumentException.class, () -> src.addChild(new FileNode("Main.java", 10)));

        assertThrows(IllegalArgumentException.class, () -> test.addChild(mainJava));

        assertThrows(IllegalArgumentException.class, () -> src.addChild(root));
        assertThrows(IllegalArgumentException.class, () -> root.addChild(root));
    }
}