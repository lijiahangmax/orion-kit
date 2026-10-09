/*
 * Copyright (c) 2019 - present Jiahang Li, All rights reserved.
 *
 *   https://kit.orionsec.cn
 *
 * Members:
 *   Jiahang Li - ljh1553488six@139.com - author
 *
 * The MIT License (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cn.orionsec.kit.net.telnet;

import cn.orionsec.kit.lang.constant.Const;
import cn.orionsec.kit.lang.constant.Letters;
import cn.orionsec.kit.lang.utils.Strings;
import cn.orionsec.kit.lang.utils.collect.Lists;

import java.util.*;

/**
 * Telnet 登录结果判定器
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/2
 */
public final class TelnetLoginJudge {

    /**
     * 提示符的最大长度
     */
    private static final int MAX_PROMPT_LENGTH = 256;

    /**
     * 常见提示符结束符
     */
    private static final String PROMPT_MARKERS = "$#>:：%)]}~";

    /**
     * 设备明确的认证失败关键词
     */
    private static final String[] AUTH_FAILURE_KEYWORDS = {
            "login incorrect",
            "incorrect password",
            "password incorrect",
            "password is invalid",
            "invalid password",
            "authentication fail",
            "login fail",
            "access denied"
    };

    private TelnetLoginJudge() {
    }

    /**
     * 最终判定登录结果
     *
     * @param page            登录后信息页 (已剔除 ANSI 序列)
     * @param loginPrompts    登录提示符候选
     * @param passwordPrompts 密码提示符候选
     * @param prompts         命令提示符候选
     * @param eof             读取时对端是否已关闭
     * @return 判定结果
     */
    public static TelnetLoginResult judge(String page,
                                          Collection<String> loginPrompts,
                                          Collection<String> passwordPrompts,
                                          Collection<String> prompts,
                                          boolean eof) {
        return judgeInternal(page, loginPrompts, passwordPrompts, prompts, eof, false, true, false);
    }

    /**
     * 读取静默后的结算判定
     *
     * @param page            登录后信息页 (已剔除 ANSI 序列)
     * @param loginPrompts    登录提示符候选
     * @param passwordPrompts 密码提示符候选
     * @param prompts         命令提示符候选
     * @return 判定结果 (未定时 {@link TelnetLoginResult#isUndecided()} 为 true)
     */
    public static TelnetLoginResult judgeSettled(String page,
                                                 Collection<String> loginPrompts,
                                                 Collection<String> passwordPrompts,
                                                 Collection<String> prompts) {
        return judgeInternal(page, loginPrompts, passwordPrompts, prompts, false, true, true, true);
    }

    /**
     * 数据到达时的结算判定
     *
     * @param page            登录后信息页 (已剔除 ANSI 序列)
     * @param loginPrompts    登录提示符候选
     * @param passwordPrompts 密码提示符候选
     * @param prompts         命令提示符候选
     * @return 判定结果 (未定时 {@link TelnetLoginResult#isUndecided()} 为 true)
     */
    public static TelnetLoginResult judgeReading(String page,
                                                 Collection<String> loginPrompts,
                                                 Collection<String> passwordPrompts,
                                                 Collection<String> prompts) {
        return judgeInternal(page, loginPrompts, passwordPrompts, prompts, false, true, false, true);
    }

    /**
     * 判定实现 (判定顺序: 失败证据优先)
     *
     * @param eof            对端是否已关闭
     * @param allowUndecided 无明确证据时是否返回未定
     * @param shapeEvidence  形态校验是否可作为成功证据
     * @param strictShape    成功证据是否要求光标停在行尾
     * @return 判定结果
     */
    private static TelnetLoginResult judgeInternal(String page,
                                                   Collection<String> loginPrompts,
                                                   Collection<String> passwordPrompts,
                                                   Collection<String> prompts,
                                                   boolean eof,
                                                   boolean allowUndecided,
                                                   boolean shapeEvidence,
                                                   boolean strictShape) {
        // 对端关闭
        if (eof) {
            return TelnetLoginResult.failure(TelnetLoginFailReason.EOF);
        }
        List<String> logins = normalize(loginPrompts);
        List<String> passwords = normalize(passwordPrompts);
        List<String> candidates = normalize(prompts);
        String lastLine = lastLine(page);
        // 登录/密码提示符重现
        if (endsWithAny(lastLine, logins)) {
            return TelnetLoginResult.failure(TelnetLoginFailReason.LOGIN_PROMPT_RETURNED);
        }
        if (endsWithAny(lastLine, passwords)) {
            return TelnetLoginResult.failure(TelnetLoginFailReason.PASSWORD_PROMPT_RETURNED);
        }
        // 认证失败关键词
        if (containsRejectKeyword(page)) {
            return TelnetLoginResult.failure(TelnetLoginFailReason.LOGIN_REJECTED);
        }
        // 末行命中候选
        if (endsWithAny(lastLine, candidates) && (!strictShape || isAtCursor(page))) {
            return TelnetLoginResult.success(isPromptLike(lastLine) ? lastLine : null);
        }
        // 提示符形态
        if (shapeEvidence) {
            String detected = extractPrompt(page, strictShape);
            if (detected != null) {
                return TelnetLoginResult.success(detected);
            }
        }
        // 返回未定
        if (allowUndecided) {
            return TelnetLoginResult.undecided();
        }
        // 候选为空放行, 否则失败
        if (candidates.isEmpty()) {
            return TelnetLoginResult.success(null);
        }
        return TelnetLoginResult.failure(TelnetLoginFailReason.PROMPT_UNCONFIRMED);
    }

    /**
     * 从信息页末尾提取命令提示符
     *
     * @param page   信息页内容
     * @param strict 是否要求光标停在提示符行上
     * @return 提示符, 未识别返回 null
     */
    private static String extractPrompt(String page, boolean strict) {
        if (Strings.isBlank(page)) {
            return null;
        }
        if (strict && !isAtCursor(page)) {
            return null;
        }
        String line = lastLine(page);
        return isPromptLike(line) ? line : null;
    }

    /**
     * 从信息页末尾提取命令提示符
     *
     * @param page 信息页内容
     * @return 提示符, 未识别返回 null
     */
    public static String extractPrompt(String page) {
        return extractPrompt(page, false);
    }

    /**
     * 提示符形态校验
     *
     * @param candidate 提示符候选
     * @return true 形似提示符
     */
    private static boolean isPromptLike(String candidate) {
        if (Strings.isBlank(candidate) || candidate.length() > MAX_PROMPT_LENGTH) {
            return false;
        }
        String value = candidate.trim();
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) < 0x20) {
                return false;
            }
        }
        return PROMPT_MARKERS.indexOf(value.charAt(value.length() - 1)) >= 0;
    }

    /**
     * 页面末尾是否停在行尾
     *
     * @param page 信息页内容 (调用前需保证非空)
     * @return true 光标停在行尾
     */
    private static boolean isAtCursor(String page) {
        char last = page.charAt(page.length() - 1);
        return last != Letters.CR && last != Letters.LF;
    }

    /**
     * 获取文本最后一个非空行
     *
     * @param page 文本
     * @return lastLine
     */
    private static String lastLine(String page) {
        if (Strings.isBlank(page)) {
            return Const.EMPTY;
        }
        String[] lines = page.split(Const.LF, -1);
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].replace(Const.CR, Const.EMPTY).trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return Const.EMPTY;
    }

    /**
     * 末行是否以任一候选结尾
     *
     * @param line       末行
     * @param candidates 候选列表
     * @return true 命中
     */
    private static boolean endsWithAny(String line, List<String> candidates) {
        if (Strings.isBlank(line)) {
            return false;
        }
        for (String candidate : candidates) {
            if (line.endsWith(candidate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查信息页末尾是否存在认证失败关键词
     *
     * @param page 信息页内容
     * @return contains
     */
    private static boolean containsRejectKeyword(String page) {
        if (Strings.isBlank(page)) {
            return false;
        }
        String[] lines = page.split(Const.LF, -1);
        StringBuilder tail = new StringBuilder();
        int count = 0;
        for (int i = lines.length - 1; i >= 0; i--) {
            String line = lines[i].replace(Const.CR, Const.EMPTY).trim();
            if (line.isEmpty()) {
                continue;
            }
            tail.insert(0, line + Const.LF);
            if (++count >= 2) {
                break;
            }
        }
        if (tail.isEmpty()) {
            return false;
        }
        String lower = tail.toString().toLowerCase(Locale.ROOT);
        for (String keyword : AUTH_FAILURE_KEYWORDS) {
            if (lower.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 规范化候选列表
     *
     * @param candidates 候选列表
     * @return 规范化后的不可变列表
     */
    public static List<String> normalize(Collection<String> candidates) {
        if (candidates == null || candidates.isEmpty()) {
            return Lists.empty();
        }
        Set<String> normalized = new LinkedHashSet<>(candidates.size());
        for (String candidate : candidates) {
            if (Strings.isNotBlank(candidate)) {
                normalized.add(candidate.trim());
            }
        }
        return List.copyOf(normalized);
    }

}
