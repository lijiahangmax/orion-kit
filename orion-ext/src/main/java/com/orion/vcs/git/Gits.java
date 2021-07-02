package com.orion.vcs.git;

import com.orion.able.SafeCloseable;
import com.orion.utils.Arrays1;
import com.orion.utils.io.Streams;
import com.orion.vcs.git.info.BranchInfo;
import com.orion.vcs.git.info.LogInfo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * git本地仓库基本操作
 * <p>
 * checkout pull reset log branch
 * <p>
 * 其他功能请用命令行
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/2 10:15
 */
public abstract class Gits implements SafeCloseable {

    private Git git;

    protected Gits(Git git) {
        this.git = git;
    }

    public static Gits of(Git git) {
        return new Gits(git) {
        };
    }

    public static Gits of(File path) throws Exception {
        return new Gits(Git.open(path)) {
        };
    }

    public Gits checkout(String branchName) throws Exception {
        git.checkout().setName(branchName)
                .setCreateBranch(false)
                .call();
        return this;
    }

    public Gits pull() throws Exception {
        git.pull().call();
        return this;
    }

    public Gits reset(String version) throws Exception {
        git.reset()
                .setMode(ResetCommand.ResetType.HARD)
                .setRef(version)
                .call();
        return this;
    }

    public Gits reset(String version, ResetCommand.ResetType type) throws Exception {
        git.reset()
                .setRef(version)
                .setMode(type)
                .call();
        return this;
    }

    public List<BranchInfo> branchList() throws Exception {
        return this.branchList(null);
    }

    public List<BranchInfo> branchList(String name) throws Exception {
        List<Ref> refs = git.branchList().setContains(name)
                .setListMode(ListBranchCommand.ListMode.REMOTE)
                .call();
        return refs.stream()
                .filter(r -> !r.getName().endsWith("/HEAD"))
                .map(ref -> {
                    BranchInfo info = new BranchInfo();
                    info.setId(ref.getObjectId().name());
                    info.setName(Arrays1.last(ref.getName().split("/")));
                    return info;
                }).collect(Collectors.toList());
    }

    public List<LogInfo> logList() throws Exception {
        return this.logList(git.getRepository().getBranch(), 10);
    }

    public List<LogInfo> logList(int count) throws Exception {
        return this.logList(git.getRepository().getBranch(), count);
    }

    public List<LogInfo> logList(String branch) throws Exception {
        return this.logList(branch, 10);
    }

    public List<LogInfo> logList(String branch, int count) throws Exception {
        Repository repo = git.getRepository();
        Ref b = git.branchList()
                .setContains(branch)
                .setListMode(ListBranchCommand.ListMode.ALL)
                .call()
                .get(0);
        if (b == null) {
            return new ArrayList<>();
        }
        ObjectId bid = repo.resolve(b.getName());
        Iterable<RevCommit> commits = git.log().setMaxCount(count).add(bid).call();
        List<LogInfo> logs = new ArrayList<>();
        for (RevCommit commit : commits) {
            LogInfo log = new LogInfo();
            log.setId(commit.getId().name());
            log.setEmail(commit.getCommitterIdent().getName());
            log.setName(commit.getCommitterIdent().getName());
            log.setTime(commit.getCommitTime());
            log.setMessage(commit.getFullMessage());
            logs.add(log);
        }
        return logs;
    }

    public Git getGit() {
        return git;
    }

    @Override
    public void close() {
        Streams.close(git);
    }

}
