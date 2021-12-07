package com.orion.vcs.git.info;

import java.io.Serializable;

/**
 * 分支信息
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2021/7/2 11:10
 */
public class BranchInfo implements Serializable {

    private static final long serialVersionUID = 765488714L;

    /**
     * id
     */
    private String id;

    /**
     * remote
     */
    private String remote;

    /**
     * 名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return remote + "/" + name;
    }

}
