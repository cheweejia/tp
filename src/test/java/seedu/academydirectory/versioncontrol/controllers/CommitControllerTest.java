package seedu.academydirectory.versioncontrol.controllers;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.academydirectory.testutil.TypicalCommits.COMMIT1;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import seedu.academydirectory.versioncontrol.objects.Commit;
import seedu.academydirectory.versioncontrol.objects.Tree;
import seedu.academydirectory.versioncontrol.utils.HashGenerator;
import seedu.academydirectory.versioncontrol.utils.HashMethod;

public class CommitControllerTest {
    private static final Path DATA_DIR = Paths.get("src", "test",
            "data", "VersionControlTest", "CommitControllerTest");
    private static final Path TESTING_DIR = Paths.get("src", "test", "temp");
    private static final HashGenerator hashGenerator = new HashGenerator(HashMethod.SHA1);

    @Test
    public void createNewCommit() {
        if (TESTING_DIR.toFile().exists()) {
            assertTrue(TESTING_DIR.toFile().delete());
        }
        assertTrue(TESTING_DIR.toFile().mkdir());

        CommitController commitController = new CommitController(hashGenerator, TESTING_DIR);

        // Null Tree and Null Commit
        String message = "Initial Commit";
        Supplier<Tree> nullTreeSupplier = () -> Tree.NULL;
        Supplier<Commit> nullCommitSupplier = () -> Commit.NULL;
        Commit currentCommit = assertDoesNotThrow(() -> commitController.createNewCommit(message,
                nullTreeSupplier,
                nullCommitSupplier));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(nullCommitSupplier, currentCommit.getParentSupplier());
        assertEquals(nullTreeSupplier, currentCommit.getTreeSupplier());

        // Non-Null Tree and Non-Null Commit
        Supplier<Tree> treeSupplier = () -> new Tree("Test", "TEst", "TEST");
        Supplier<Commit> commitSupplier = () -> COMMIT1;
        currentCommit = assertDoesNotThrow(() -> commitController.createNewCommit(message,
                treeSupplier,
                commitSupplier));

        assertEquals(System.getProperty("user.name"), currentCommit.getAuthor());
        assertEquals(currentCommit.getDate(), currentCommit.getDate());
        assertEquals(message, currentCommit.getMessage());
        assertEquals(commitSupplier, currentCommit.getParentSupplier());
        assertEquals(treeSupplier, currentCommit.getTreeSupplier());

        assertTrue(TESTING_DIR.toFile().delete());
    }

    @Test
    public void fetchCommitByHash() throws IOException {
        if (TESTING_DIR.toFile().exists()) {
            assertTrue(TESTING_DIR.toFile().delete());
        }
        assertTrue(TESTING_DIR.toFile().mkdir());

        CommitController commitController = new CommitController(hashGenerator, DATA_DIR);

        // Exact Hash used -> Commit fetched successfully
        String commitHash = "1d83638a25901e76c8e3882afca2347f8352cd06";
        Path filepath = DATA_DIR.resolve(Paths.get(commitHash));
        assertTrue(filepath.toFile().exists()); // Check if file exists first

        Commit actualCommit = commitController.fetchCommitByHash(commitHash);
        assertEquals(COMMIT1, actualCommit);

        // fiveCharHash used -> Commit fetched successfully
        commitHash = commitHash.substring(0, 5);
        actualCommit = commitController.fetchCommitByHash(commitHash);
        assertEquals(COMMIT1, actualCommit);

        // given hash not present in disk -> Commit.NULL returned
        commitHash = "Testing123";
        actualCommit = commitController.fetchCommitByHash(commitHash);
        assertEquals(Commit.NULL, actualCommit);

        // given corrupt commit file -> Commit.NULL returned
        commitHash = "corrupted";
        actualCommit = commitController.fetchCommitByHash(commitHash);
        assertEquals(Commit.NULL, actualCommit);

        assertTrue(TESTING_DIR.toFile().delete());
    }
}
