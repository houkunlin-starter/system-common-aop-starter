package com.houkunlin.system.common.aop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllowIP(key = AllowIP.INNER_IP)
@Getter
@RestController
@RequestMapping("/AllowIP/AtType/")
@RequiredArgsConstructor
public class AllowIPControllerAtType {

    @AllowIP(key = AllowIP.INNER_IP)
    @GetMapping("/m11")
    public boolean m11() {
        return true;
    }

    @AllowIP(key = AllowIP.INNER_IP_A)
    @GetMapping("/m12")
    public boolean m12() {
        return true;
    }

    @AllowIP(key = AllowIP.INNER_IP_B)
    @GetMapping("/m13")
    public boolean m13() {
        return true;
    }

    @AllowIP(key = AllowIP.INNER_IP_C)
    @GetMapping("/m14")
    public boolean m14() {
        return true;
    }

    @AllowIP(key = AllowIP.INNER_IP_LOCAL)
    @GetMapping("/m15")
    public boolean m15() {
        return true;
    }

    @AllowIP(key = AllowIP.INNER_IP, ipList = {"8.8.8.8", "6.6.6.6"})
    @GetMapping("/m16")
    public boolean m16() {
        return true;
    }

    @AllowIP(ipList = {"8.8.8.8", "6.6.6.6"})
    @GetMapping("/m17")
    public boolean m17() {
        return true;
    }

    @AllowIP(ipList = {"8.8.8.8/24", "6.6.6.6/24"})
    @GetMapping("/m18")
    public boolean m18() {
        return true;
    }

    @AllowIP
    @GetMapping("/m19")
    public boolean m19() {
        return true;
    }

    @GetMapping("/m20")
    public boolean m20() {
        return true;
    }

    @AllowIP(key = "office-net")
    @GetMapping("/m21")
    public boolean m21() {
        return true;
    }

    @AllowIP(key = "office-net-2")
    @GetMapping("/m22")
    public boolean m22() {
        return true;
    }
}
