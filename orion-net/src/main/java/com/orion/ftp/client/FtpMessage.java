package com.orion.ftp.client;

import java.util.HashMap;
import java.util.Map;

/**
 * FTP信息常量
 *
 * @author ljh15
 * @version 1.0.0
 * @since 2020/3/17 15:53
 */
public class FtpMessage {

    private FtpMessage() {
    }

    /**
     * FTP状态码及其描述
     */
    public static final Map<Integer, String> REPLY_CODE = new HashMap<>();

    static {
        REPLY_CODE.put(120, "服务已就绪, 在 n 分钟后开始");
        REPLY_CODE.put(125, "数据连接已打开, 正在开始传输");
        REPLY_CODE.put(150, "文件状态正常, 准备打开数据连接");
        REPLY_CODE.put(202, "未执行命令, 站点上的命令过多");
        REPLY_CODE.put(211, "系统状态, 或系统帮助答复");
        REPLY_CODE.put(212, "目录状态");
        REPLY_CODE.put(213, "文件状态");
        REPLY_CODE.put(214, "帮助消息");
        REPLY_CODE.put(215, "NAME 系统类型, 其中, NAME 是 Assigned Numbers 文档中所列的正式系统名称");
        REPLY_CODE.put(220, "服务就绪, 可以执行新用户的请求");
        REPLY_CODE.put(221, "服务关闭控制连接如果适当, 请注销");
        REPLY_CODE.put(225, "数据连接打开, 没有进行中的传输");
        REPLY_CODE.put(226, "关闭数据连接请求的文件操作已成功 (例如: 传输文件或放弃文件)");
        REPLY_CODE.put(227, "进入被动模式");
        REPLY_CODE.put(230, "用户已登录, 继续进行");
        REPLY_CODE.put(250, "请求的文件操作正确, 已完成");
        REPLY_CODE.put(257, "已创建 “PATHNAME”");
        REPLY_CODE.put(332, "需要登录帐户");
        REPLY_CODE.put(350, "请求的文件操作正在等待进一步的信息");
        REPLY_CODE.put(425, "无法打开数据连接");
        REPLY_CODE.put(426, "连接关闭");
        REPLY_CODE.put(450, "未执行请求的文件操作文件不可用");
        REPLY_CODE.put(451, "请求的操作异常终止: 正在处理本地错误");
        REPLY_CODE.put(452, "未执行请求的操作系统存储空间不够");
        REPLY_CODE.put(501, "在参数中有语法错误");
        REPLY_CODE.put(502, "未执行命令");
        REPLY_CODE.put(503, "错误的命令序列");
        REPLY_CODE.put(504, "未执行该参数的命令");
        REPLY_CODE.put(530, "未登录");
        REPLY_CODE.put(532, "存储文件需要帐户");
        REPLY_CODE.put(550, "未执行请求的操作文件不可用 (例如: 未找到文件, 没有访问权限)");
        REPLY_CODE.put(551, "请求的操作异常终止: 未知的页面类型");
        REPLY_CODE.put(552, "请求的文件操作异常终止: 超出存储分配 (对于当前目录或数据集)");
        REPLY_CODE.put(553, "未执行请求的操作不允许的文件名");
    }

}
