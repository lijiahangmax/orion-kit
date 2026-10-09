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

import cn.orionsec.kit.lang.utils.collect.Lists;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TelnetLoginJudge test
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2026/10/2
 */
public class TelnetLoginJudgeTests {

    private static final List<String> LOGIN = Lists.singleton("login:");

    private static final List<String> PASSWORD = Lists.singleton("Password:");

    private static final List<String> PROMPTS = Arrays.asList("$", "#", ">", "%");

    @Test
    public void testCandidateMatched() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "Last login: Mon Sep 28 10:00:00 2026\r\ntest@telnet-1:~$ ", LOGIN, PASSWORD, PROMPTS, false);
        assertFalse(result.isFailure());
        assertEquals("test@telnet-1:~$", result.getDetectedPrompt());
    }

    @Test
    public void testDetectByShapeWhenPromptsEmpty() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nroot@host:/#", LOGIN, PASSWORD, Lists.empty(), false);
        assertFalse(result.isFailure());
        assertEquals("root@host:/#", result.getDetectedPrompt());
    }

    @Test
    public void testDetectRouterPrompt() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "User Access Verification\r\nRouter>", LOGIN, PASSWORD, Lists.empty(), false);
        assertFalse(result.isFailure());
        assertEquals("Router>", result.getDetectedPrompt());
    }

    @Test
    public void testLoginPromptReturned() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "Last login: Mon Sep 28 10:00:00 2026\r\nLogin incorrect\r\nlogin: ", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_PROMPT_RETURNED, result.getReason());
        assertNull(result.getDetectedPrompt());
    }

    @Test
    public void testPasswordPromptReturned() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "User Access Verification\r\nUsername: root\r\nPassword: ", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.PASSWORD_PROMPT_RETURNED, result.getReason());
    }

    @Test
    public void testEofFirst() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "root@host:~#", LOGIN, PASSWORD, PROMPTS, true);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.EOF, result.getReason());
    }

    @Test
    public void testLoginRejectedByKeyword() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nLogin incorrect\r\n", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_REJECTED, result.getReason());
    }

    @Test
    public void testLoginRejectedCaseInsensitive() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nAuthentication Failure\r\n", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_REJECTED, result.getReason());
    }

    @Test
    public void testLoginRejectedWithEmptyPrompts() {
        // 失败关键词优先于留空放行
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nLogin incorrect\r\n", LOGIN, PASSWORD, Lists.empty(), false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_REJECTED, result.getReason());
    }

    @Test
    public void testUndecidedWhileReading() {
        // 无明确证据 → 未定
        TelnetLoginResult result = TelnetLoginJudge.judgeReading(
                "banner\r\n\r\n", LOGIN, PASSWORD, PROMPTS);
        assertFalse(result.isFailure());
        assertTrue(result.isUndecided());
        // 最终判定: 候选非空 → 失败
        TelnetLoginResult settled = TelnetLoginJudge.judge(
                "banner\r\n\r\n", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(settled.isFailure());
        assertEquals(TelnetLoginFailReason.PROMPT_UNCONFIRMED, settled.getReason());
    }

    @Test
    public void testUndecidedSettledWithoutPrompts() {
        // 最终判定: 无明确证据且未配置候选 → 放行
        TelnetLoginResult settled = TelnetLoginJudge.judge(
                "banner\r\n\r\n", LOGIN, PASSWORD, Lists.empty(), false);
        assertFalse(settled.isFailure());
        assertFalse(settled.isUndecided());
        assertNull(settled.getDetectedPrompt());
    }

    @Test
    public void testReadingNotSettledByBannerLikeLine() {
        // 形似提示符的 banner 行不结算
        TelnetLoginResult reading = TelnetLoginJudge.judgeReading(
                "Security Notice:", LOGIN, PASSWORD, PROMPTS);
        assertTrue(reading.isUndecided());
        // 静默结算: 光标停在行尾 → 形态证据
        TelnetLoginResult settled = TelnetLoginJudge.judgeSettled(
                "Security Notice:", LOGIN, PASSWORD, PROMPTS);
        assertFalse(settled.isUndecided());
        assertFalse(settled.isFailure());
    }

    @Test
    public void testSettledRequiresPromptAtCursor() {
        // 以换行结尾: 形态证据不成立
        TelnetLoginResult unfinished = TelnetLoginJudge.judgeSettled(
                "banner\r\nroot@host:~#\r\n", LOGIN, PASSWORD, Lists.empty());
        assertTrue(unfinished.isUndecided());
        // 光标停在提示符上 → 结算
        TelnetLoginResult settled = TelnetLoginJudge.judgeSettled(
                "banner\r\nroot@host:~# ", LOGIN, PASSWORD, Lists.empty());
        assertFalse(settled.isUndecided());
        assertFalse(settled.isFailure());
        assertEquals("root@host:~#", settled.getDetectedPrompt());
    }

    @Test
    public void testCandidateNotSettledByTrailingLineBanner() {
        // 候选命中但以换行结尾: 不结算
        TelnetLoginResult reading = TelnetLoginJudge.judgeReading(
                "Contact: <root@localhost>\r\n", LOGIN, PASSWORD, PROMPTS);
        assertTrue(reading.isUndecided());
        TelnetLoginResult settled = TelnetLoginJudge.judgeSettled(
                "Contact: <root@localhost>\r\n", LOGIN, PASSWORD, PROMPTS);
        assertTrue(settled.isUndecided());
        // 光标停在候选行上 → 结算
        TelnetLoginResult ok = TelnetLoginJudge.judgeReading(
                "root@host:~# ", LOGIN, PASSWORD, PROMPTS);
        assertFalse(ok.isUndecided());
        assertFalse(ok.isFailure());
        assertEquals("root@host:~#", ok.getDetectedPrompt());
    }

    @Test
    public void testRejectKeywordOnlyMatchesTail() {
        // 页面中部关键词不误伤
        TelnetLoginResult ok = TelnetLoginJudge.judge(
                "Access denied to unauthorized users\r\n\r\nbanner\r\nroot@host:~#", LOGIN, PASSWORD, PROMPTS, false);
        assertFalse(ok.isFailure());
        assertEquals("root@host:~#", ok.getDetectedPrompt());
        // 末尾失败信息: 判失败
        TelnetLoginResult rejected = TelnetLoginJudge.judge(
                "banner\r\nAccess denied\r\n", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(rejected.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_REJECTED, rejected.getReason());
    }

    @Test
    public void testUnconfirmedWithPrompts() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nsome noise", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.PROMPT_UNCONFIRMED, result.getReason());
    }

    @Test
    public void testEmptyPageWithPrompts() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "", LOGIN, PASSWORD, PROMPTS, false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.PROMPT_UNCONFIRMED, result.getReason());
    }

    @Test
    public void testEmptyPageWithoutPrompts() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "", LOGIN, PASSWORD, Lists.empty(), false);
        assertFalse(result.isFailure());
        assertNull(result.getDetectedPrompt());
    }

    @Test
    public void testCrLfAndSpaces() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\n  test@telnet-1:~$ \r\n", LOGIN, PASSWORD, Lists.singleton("$"), false);
        assertFalse(result.isFailure());
        assertEquals("test@telnet-1:~$", result.getDetectedPrompt());
    }

    @Test
    public void testTrailingBlankLines() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\nroot@host:~#\r\n\r\n", LOGIN, PASSWORD, Lists.empty(), false);
        assertFalse(result.isFailure());
        assertEquals("root@host:~#", result.getDetectedPrompt());
    }

    @Test
    public void testFailurePriorToSuccess() {
        TelnetLoginResult result = TelnetLoginJudge.judge(
                "banner\r\ntelnet-1 login:", LOGIN, PASSWORD, Lists.singleton("login:"), false);
        assertTrue(result.isFailure());
        assertEquals(TelnetLoginFailReason.LOGIN_PROMPT_RETURNED, result.getReason());
    }

    @Test
    public void testNormalizeCandidates() {
        // 去空 trim 去重 (null/空白元素剔除)
        assertEquals(Arrays.asList("login:", "root@host:~#"),
                TelnetLoginJudge.normalize(Arrays.asList(" login: ", "", null, "login:", " root@host:~# ")));
        assertTrue(TelnetLoginJudge.normalize(null).isEmpty());
        assertTrue(TelnetLoginJudge.normalize(Lists.empty()).isEmpty());
    }

    @Test
    public void testExtractPromptRejectControlChar() {
        assertNull(TelnetLoginJudge.extractPrompt("banner\r\nro\u0007ot@host:~#"));
    }

    @Test
    public void testExtractPromptRejectTooLong() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            builder.append('a');
        }
        builder.append('#');
        assertNull(TelnetLoginJudge.extractPrompt(builder.toString()));
    }

    @Test
    public void testExtractPrompt() {
        assertEquals("root@host:~#", TelnetLoginJudge.extractPrompt("banner\r\nlast line\r\nroot@host:~#"));
        assertNull(TelnetLoginJudge.extractPrompt("banner\r\nno prompt"));
        assertNull(TelnetLoginJudge.extractPrompt(null));
    }

}
