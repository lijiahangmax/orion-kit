package com.orion.vcs.git;

import com.orion.able.SafeCloseable;
import com.orion.utils.Arrays1;
import com.orion.utils.Exceptions;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * git本地仓库基本操作
 * <p>
 * checkout pull reset log branch clean
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

    public static Gits of(File path) {
        try {
            return new Gits(Git.open(path)) {
            };
        } catch (IOException e) {
            throw Exceptions.vcs(e);
        }
    }

    public static Gits of(Repository repo) {
        return new Gits(Git.wrap(repo)) {
        };
    }

    /**
     * 检出分支代码
     *
     * @param branchName 分支名称
     * @return this
     */
    public Gits checkout(String branchName) {
        try {
            git.checkout().setName(branchName)
                    .setCreateBranch(false)
                    .call();
            return this;
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * pull代码
     *
     * @return this
     */
    public Gits pull() {
        try {
            git.pull().call();
            return this;
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 还原版本 hard
     *
     * @param version commitId
     * @return this
     */
    public Gits reset(String version) {
        return this.reset(version, ResetCommand.ResetType.HARD);
    }

    /**
     * 还原版本
     *
     * @param version commitId
     * @param type    类型
     * @return this
     */
    public Gits reset(String version, ResetCommand.ResetType type) {
        try {
            git.reset()
                    .setRef(version)
                    .setMode(type)
                    .call();
            return this;
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 分支列表
     *
     * @return list
     */
    public List<BranchInfo> branchList() {
        return this.branchList(null);
    }

    /**
     * 分支列表
     *
     * @param name 分支名称
     * @return list
     */
    public List<BranchInfo> branchList(String name) {
        try {
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
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 日志列表
     *
     * @return 当前分支日志列表
     */
    public List<LogInfo> logList() {
        try {
            return this.logList(git.getRepository().getBranch(), 10);
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 日志列表
     *
     * @param count 日志数量
     * @return 当前分支日志列表
     */
    public List<LogInfo> logList(int count) {
        try {
            return this.logList(git.getRepository().getBranch(), count);
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 日志列表
     *
     * @param branch 分支名称
     * @return 分支日志列表
     */
    public List<LogInfo> logList(String branch) {
        return this.logList(branch, 10);
    }

    /**
     * 日志列表
     *
     * @param branch 分支名称
     * @param count  日志数量
     * @return 分支日志列表
     */
    public List<LogInfo> logList(String branch, int count) {
        try {
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
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 清空工作目录其他文件
     *
     * @return return
     */
    public Gits clean() {
        try {
            git.clean().setForce(true)
                    .setCleanDirectories(true)
                    .call();
            return this;
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    /**
     * 获取目录
     *
     * @return 目录
     */
    public String getDirectory() {
        return git.getRepository().getDirectory().getParent();
    }

    /**
     * 获取远程url
     *
     * @return url
     */
    public String getRemoteUrl() {
        return git.getRepository().getConfig().getString("remote", "origin", "url");
    }

    /**
     * 获取当前分支
     *
     * @return branch
     */
    public String getBranch() {
        try {
            return git.getRepository().getBranch();
        } catch (Exception e) {
            throw Exceptions.vcs(e);
        }
    }

    public Git getGit() {
        return git;
    }

    public Repository getRepository() {
        return git.getRepository();
    }

    @Override
    public void close() {
        Streams.close(git);
    }

}
