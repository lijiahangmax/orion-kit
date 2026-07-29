package cn.orionsec.kit.ext.vcs.git;

import cn.orionsec.kit.ext.vcs.git.info.BranchInfo;
import cn.orionsec.kit.ext.vcs.git.info.LogInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.*;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Gits 单元测试
 */
public class GitsTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private Git jgit;
    private Gits gits;
    private File repoDir;

    @Before
    public void setUp() throws Exception {
        repoDir = tempFolder.newFolder("test-repo");
        jgit = Git.init().setDirectory(repoDir).call();
        // 配置用户信息
        jgit.getRepository().getConfig().setString("user", null, "name", "Test User");
        jgit.getRepository().getConfig().setString("user", null, "email", "test@example.com");
        jgit.getRepository().getConfig().save();
        gits = Gits.of(jgit);
    }

    @After
    public void tearDown() {
        if (gits != null) {
            gits.close();
        }
    }

    @Test
    public void testOfWithGit() {
        Gits g = Gits.of(jgit);
        assertNotNull(g);
        assertNotNull(g.getGit());
        assertEquals(jgit, g.getGit());
        // 不关闭 gits 已经管理了底层 git
    }

    @Test
    public void testOfWithFile() {
        Gits g = Gits.of(repoDir);
        assertNotNull(g);
        assertNotNull(g.getRepository());
        g.close();
    }

    @Test
    public void testOfWithRepository() {
        Gits g = Gits.of(jgit.getRepository());
        assertNotNull(g);
        assertNotNull(g.getGit());
    }

    @Test
    public void testGetDirectory() {
        String dir = gits.getDirectory();
        assertNotNull(dir);
        assertEquals(repoDir.getAbsolutePath(), dir);
    }

    @Test
    public void testGetBranch() {
        String branch = gits.getBranch();
        assertNotNull(branch);
        // 新初始化的仓库默认分支可能是 master 或 main
        assertTrue(branch.equals("master") || branch.equals("main"));
    }

    @Test
    public void testGetRemoteUrl() {
        // 本地初始化仓库没有 remote url
        String remoteUrl = gits.getRemoteUrl();
        assertNull(remoteUrl);
    }

    @Test
    public void testHasRef() throws Exception {
        // 初始化后没有 HEAD 指向的 commit，所以 HEAD 存在但分支不一定存在
        // 先创建一个提交
        createAndCommitFile("test.txt", "hello", "initial commit");
        assertTrue(gits.hasRef("HEAD"));
        assertTrue(gits.hasRef(gits.getBranch()));
        assertFalse(gits.hasRef("nonexistent-branch-xyz"));
    }

    @Test
    public void testGetRef() throws Exception {
        createAndCommitFile("test.txt", "content", "first commit");
        Ref ref = gits.getRef("HEAD");
        assertNotNull(ref);
        assertNotNull(ref.getObjectId());
    }

    @Test
    public void testCheckout() throws Exception {
        // 创建初始提交
        createAndCommitFile("file1.txt", "content1", "first commit");
        // 创建新分支
        jgit.branchCreate().setName("dev").call();
        // checkout 到新分支
        gits.checkout("dev");
        assertEquals("dev", gits.getBranch());
        // checkout 回来
        gits.checkout(getDefaultBranch());
        assertEquals(getDefaultBranch(), gits.getBranch());
    }

    @Test
    public void testResetHard() throws Exception {
        // 第一次提交
        createAndCommitFile("file1.txt", "v1", "commit 1");
        RevCommit first = jgit.log().call().iterator().next();
        // 第二次提交
        createAndCommitFile("file2.txt", "v2", "commit 2");
        // reset 到第一次
        gits.reset(first.getId().getName(), ResetCommand.ResetType.HARD);
        // 验证已回退
        RevCommit current = jgit.log().call().iterator().next();
        assertEquals(first.getId().getName(), current.getId().getName());
    }

    @Test
    public void testResetSoft() throws Exception {
        createAndCommitFile("file1.txt", "v1", "commit 1");
        RevCommit first = jgit.log().call().iterator().next();
        createAndCommitFile("file2.txt", "v2", "commit 2");
        gits.reset(first.getId().getName(), ResetCommand.ResetType.SOFT);
        RevCommit current = jgit.log().call().iterator().next();
        assertEquals(first.getId().getName(), current.getId().getName());
    }

    @Test
    public void testClean() throws Exception {
        // 创建初始提交
        createAndCommitFile("tracked.txt", "tracked", "initial");
        // 创建未跟踪文件
        File untracked = new File(repoDir, "untracked.txt");
        try (FileWriter fw = new FileWriter(untracked)) {
            fw.write("untracked content");
        }
        assertTrue(untracked.exists());
        // clean
        gits.clean();
        assertFalse(untracked.exists());
    }

    @Test
    public void testLogList() throws Exception {
        createAndCommitFile("f1.txt", "c1", "first commit");
        createAndCommitFile("f2.txt", "c2", "second commit");
        createAndCommitFile("f3.txt", "c3", "third commit");

        String branch = gits.getBranch();
        List<LogInfo> logs = gits.logList(branch, 10);
        assertNotNull(logs);
        assertEquals(3, logs.size());
        // 最新提交在前
        assertEquals("third commit", logs.get(0).getMessage());
        assertEquals("second commit", logs.get(1).getMessage());
        assertEquals("first commit", logs.get(2).getMessage());
        // 验证 LogInfo 字段不为空
        for (LogInfo log : logs) {
            assertNotNull(log.getId());
            assertNotNull(log.getName());
            assertNotNull(log.getMessage());
            assertNotNull(log.getTime());
        }
    }

    @Test
    public void testLogListWithCount() throws Exception {
        createAndCommitFile("f1.txt", "c1", "commit 1");
        createAndCommitFile("f2.txt", "c2", "commit 2");
        createAndCommitFile("f3.txt", "c3", "commit 3");

        List<LogInfo> logs = gits.logList(gits.getBranch(), 2);
        assertEquals(2, logs.size());
    }

    @Test
    public void testLogListDefault() throws Exception {
        createAndCommitFile("f1.txt", "c1", "commit 1");
        List<LogInfo> logs = gits.logList();
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
    }

    @Test
    public void testLogListWithCountOverload() throws Exception {
        createAndCommitFile("f1.txt", "c1", "commit 1");
        createAndCommitFile("f2.txt", "c2", "commit 2");
        List<LogInfo> logs = gits.logList(1);
        assertEquals(1, logs.size());
    }

    @Test
    public void testAuth() {
        Gits authed = gits.auth("user", "pass");
        assertNotNull(authed);
        assertSame(gits, authed);
    }

    @Test
    public void testAuthWithCharArray() {
        Gits authed = gits.auth("user", new char[]{'p', 'a', 's', 's'});
        assertNotNull(authed);
        assertSame(gits, authed);
    }

    @Test
    public void testGetRepository() {
        assertNotNull(gits.getRepository());
        assertEquals(jgit.getRepository(), gits.getRepository());
    }

    @Test
    @Ignore("需要远程仓库")
    public void testClone() {
        // 需要网络操作，忽略
    }

    @Test
    @Ignore("需要远程仓库")
    public void testPull() {
        // 需要网络操作，忽略
    }

    @Test
    @Ignore("需要远程仓库")
    public void testPush() {
        // 需要网络操作，忽略
    }

    @Test
    @Ignore("需要远程仓库")
    public void testFetch() {
        // 需要网络操作，忽略
    }

    @Test
    @Ignore("需要远程仓库")
    public void testBranchList() {
        // 需要远程仓库分支，忽略
    }

    // ============= BranchInfo 测试 =============

    @Test
    public void testBranchInfo() {
        BranchInfo info = new BranchInfo();
        info.setId("abc123");
        info.setRemote("origin");
        info.setName("master");

        assertEquals("abc123", info.getId());
        assertEquals("origin", info.getRemote());
        assertEquals("master", info.getName());
        assertEquals("origin/master", info.toString());
    }

    // ============= LogInfo 测试 =============

    @Test
    public void testLogInfo() {
        LogInfo log = new LogInfo();
        log.setId("commit-id-123");
        log.setEmail("test@example.com");
        log.setName("Test User");
        log.setMessage("test message");
        log.setTime(1625200000);

        assertEquals("commit-id-123", log.getId());
        assertEquals("test@example.com", log.getEmail());
        assertEquals("Test User", log.getName());
        assertEquals("test message", log.getMessage());
        assertNotNull(log.getTime());
        assertNotNull(log.toString());
        assertTrue(log.toString().contains("commit-id-123"));
    }

    // ============= 辅助方法 =============

    private void createAndCommitFile(String name, String content, String message) throws Exception {
        File file = new File(repoDir, name);
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(content);
        }
        jgit.add().addFilepattern(name).call();
        jgit.commit().setMessage(message)
                .setAuthor("Test User", "test@example.com")
                .setCommitter("Test User", "test@example.com")
                .call();
    }

    private String getDefaultBranch() throws Exception {
        return jgit.getRepository().getBranch();
    }

}
