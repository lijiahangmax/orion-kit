package com.orion.test;

import com.orion.constant.Const;
import com.orion.utils.Valid;
import com.orion.utils.identity.CreditCodes;
import com.orion.utils.regexp.Matches;
import org.junit.Test;

import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author ljh15
 * @version 1.0.0
 * @since 2020/10/31 23:19
 */
public class MatchesTests {

    @Test
    public void test() {
        Valid.isTrue(Matches.isHex("123345ABCDEF"));
        System.out.println("Matches.isHex(\"123345ABCDEFG\") = " + Matches.isHex("123345ABCDEFG"));
        System.out.println("Matches.isIpv4(\"192.168.1.1\") = " + Matches.isIpv4("192.168.1.1"));
        System.out.println("Matches.isIpv4(\"255.168.255.0\") = " + Matches.isIpv4("255.168.255.0"));
        System.out.println("Matches.isIpv4(\"256.168.1.1\") = " + Matches.isIpv4("256.168.1.1"));
        System.out.println("Matches.isIpv6(\"2409:8a00:8435:a880:14a7:26e7:cffa:810e\") = " + Matches.isIpv6("2409:8a00:8435:a880:14a7:26e7:cffa:810e"));
        System.out.println("Matches.isIpv6(\"2409:8a00:8435:a880:54ce:c3a9:62e8:e68d\") = " + Matches.isIpv6("2409:8a00:8435:a880:54ce:c3a9:62e8:e68d"));
        System.out.println("Matches.isIpv6(\"2409:8a00:8435:a880:c8ae:3ab4:845f:b9b8\") = " + Matches.isIpv6("2409:8a00:8435:a880:c8ae:3ab4:845f:b9b8"));
        System.out.println("Matches.isUuid(UUID.randomUUID().toString()) = " + Matches.isUuid(UUID.randomUUID().toString()));
        System.out.println("Matches.isUuid(UUID.randomUUID().toString()) = " + Matches.isUuid(UUID.randomUUID().toString()));
        System.out.println("Matches.isDouble(\"232\") = " + Matches.isDouble("232"));
        System.out.println("Matches.isDouble(\"232.2323\") = " + Matches.isDouble("232.2323"));
        System.out.println("Matches.isInteger(\"2323\") = " + Matches.isInteger("2323"));
        System.out.println("Matches.isInteger(\"232.2323\") = " + Matches.isInteger("232.2323"));
        System.out.println("Matches.isZipCode(\"100038\") = " + Matches.isZipCode("100038"));
        System.out.println("Matches.isZipCode(\"100010\") = " + Matches.isZipCode("100010"));
        System.out.println("Matches.isHttp(\"http://23.com\") = " + Matches.isHttp("http://23.com"));
        System.out.println("Matches.isHttp(\"http://www.23.com\") = " + Matches.isHttp("http://www.23.com"));
        System.out.println("Matches.isHttp(\"http://23.com?xsdsd=23&ewew=23qwe%2\") = " + Matches.isHttp("http://23.com?xsdsd=23&ewew=23qwe%2"));
        System.out.println("Matches.isHttp(\"http:///23.com\") = " + Matches.isHttp("http:///23.com"));
        System.out.println("Matches.isUtf(\"asd\") = " + Matches.isUtf("asd"));
        System.out.println("Matches.isUtf(\"asd00\") = " + Matches.isUtf("asd00"));
        System.out.println("Matches.isUtf(\"asd00_\") = " + Matches.isUtf("asd00_"));
        System.out.println("Matches.isUtf(\"asd00_&&\") = " + Matches.isUtf("asd00_&&"));
        System.out.println("Matches.isUri(\"http://23.com\") = " + Matches.isUri("http://23.com"));
        System.out.println("Matches.isUri(\"tpc://23\") = " + Matches.isUri("tpc://23"));
        System.out.println("Matches.isUri(\"tpc:///23\") = " + Matches.isUri("tpc:///23"));
        System.out.println("Matches.isLinuxPath(\"/etc/a\") = " + Matches.isLinuxPath("/etc/a"));
        System.out.println("Matches.isLinuxPath(\"D:/etc/a\") = " + Matches.isLinuxPath("D:/etc/a"));
        System.out.println("Matches.isLinuxPath(\"/etc/a\\\\1\") = " + Matches.isLinuxPath("/etc/a\\1"));
        System.out.println("Matches.isWindowsPath(\"/etc/a\") = " + Matches.isWindowsPath("/etc/a"));
        System.out.println("Matches.isWindowsPath(\"D:/etc/a\") = " + Matches.isWindowsPath("D:/etc/a"));
        System.out.println("Matches.isWindowsPath(\"/etc/a\\\\1\") = " + Matches.isWindowsPath("/etc/a\\1"));
        System.out.println("Matches.isPhone(\"18888888888\") = " + Matches.isPhone("18888888888"));
        System.out.println("Matches.isPhone(\"15566772323\") = " + Matches.isPhone("15566772323"));
        System.out.println("Matches.isPhone(\"18790873456\") = " + Matches.isPhone("18790873456"));
        System.out.println("Matches.isEmail(\"lasd2@qwe.c\") = " + Matches.isEmail("lasd2@qwe.c"));
        System.out.println("Matches.isEmail(\"lasd2@_qwe.com.com\") = " + Matches.isEmail("lasd2@_qwe.com.com"));
        System.out.println("Matches.isEmail(\"lasd2_@1sd.com\") = " + Matches.isEmail("lasd2_@1sd.com"));
        System.out.println("Matches.isEmail(\"lasd2@_&*qwe22.@sd.com\") = " + Matches.isEmail("lasd2@_&*qwe22.@sd.com"));
        IntStream.range(1, 20).forEach((__) -> Valid.isTrue(Matches.isCreditCode(CreditCodes.random())));

    }

    public static void main(String[] args) {
        new MatchesTests().ext();
    }

    public void ext() {
        String s = "2409:8a00:8435:a880:14a7:26e7:cffa:810e\n" +
                "2409:8a00:8435:a880:54ce:c3a9:62e8:e68d\n" +
                "2409:8a00:8435:a880:c8ae:3ab4:845f:b9b8\n" +
                "asd:7777";
        System.out.println(Matches.extIpv6(s));
        System.out.println(Matches.extIpv6List(s));
        s = IntStream.rangeClosed(1, 5).mapToObj((__) -> CreditCodes.random() + Const.LF).collect(Collectors.joining());
        System.out.println(Matches.extCreditCode(s));
        System.out.println(Matches.extCreditCodeList(s));
        s = "15865238745,16859635685,17759685658,888555,18457965832,13111258895,2";
        System.out.println(Matches.extPhone(s));
        System.out.println(Matches.extPhoneList(s));
        s = "522568@qq.com,775asd_@qs.c,77ser@jd.com,77@_@";
        System.out.println(Matches.extEmail(s));
        System.out.println(Matches.extEmailList(s));
    }

}
