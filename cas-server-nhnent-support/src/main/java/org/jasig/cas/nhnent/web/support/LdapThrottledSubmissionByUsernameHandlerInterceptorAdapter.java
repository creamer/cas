package org.jasig.cas.nhnent.web.support;

import net.spy.memcached.MemcachedClientIF;
import org.jasig.cas.authentication.PreventedException;
import org.jasig.cas.nhnent.util.LdapAttributeIO;
import org.jasig.cas.nhnent.util.MemcachedIO;
import org.jasig.inspektr.common.web.ClientInfoHolder;
import org.ldaptive.LdapAttribute;
import org.ldaptive.LdapEntry;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruaa on 2016. 4. 20..
 */
public class LdapThrottledSubmissionByUsernameHandlerInterceptorAdapter
            extends AbstractThrottledSubmissionHandlerInterceptorAdapterFonNhn {

    private static final int FLAG_ACCOUNT_DISABLED = 16777216;
    private LdapAttributeIO ldapAttributeIO;
    private MemcachedIO memcachedIO;

    public LdapThrottledSubmissionByUsernameHandlerInterceptorAdapter(LdapAttributeIO ldapAttributeIO,
                                                                      MemcachedIO memcachedIO) {
        this.ldapAttributeIO = ldapAttributeIO;
        this.memcachedIO = memcachedIO;
    }

    @Override
    protected boolean exceedsThreshold(HttpServletRequest request) {
        String userName = request.getParameter(getUsernameParameter());
        try {
            LdapEntry ldapEntry = ldapAttributeIO.retrieve(userName, "cn", "userdisable");
            if (ldapEntry != null) {
                LdapAttribute disableAttr = ldapEntry.getAttribute("userdisable");
                if(disableAttr != null) {
                    String temp = disableAttr.getStringValue();
                    logger.debug("::: user name: " + userName + ", userdisable: " + temp);
                    if (Integer.parseInt(temp) == FLAG_ACCOUNT_DISABLED) {
                        return true;
                    }
                }
            }
        } catch (PreventedException e) {
            logger.error("ldap error", e);
        }

        return false;
    }

    @Override
    protected void recordThrottle(HttpServletRequest request) {
        super.recordThrottle(request);
    }

    @Override
    protected void recordSubmissionFailure(HttpServletRequest request) {
        // memcache 에 로그인 실패 카운트 증가
        String key = constructKey(request);
        Integer failureCount = (Integer) memcachedIO.get(key);

        if (failureCount == null) {
            memcachedIO.set(key, 1);
            failureCount = 1;
        } else {
            failureCount++;
            memcachedIO.set(key, failureCount);
        }

        // 로그인 실패 횟수가 5 이상인 경우 LDAP 의 userdisable 속성의 값을 4로 세팅한다.
        if (failureCount >= 5) {
            // memcache 에 저장된 실패 횟수는 초기화하여 차후 로그인 시도 시 실패 횟수를 0부터 시작할 수 있도록 한다.
            removeFailureCount(request);
            lockAccount(request.getParameter(getUsernameParameter()));
        }

        logger.debug("::: failure count: " + failureCount);
    }

    @Override
    protected void recordSubmissionSuccess(HttpServletRequest request) {
        // 정상적으로 인증 성공 시 memcache 에 저장된 실패 횟수를 초기화 한다.
        removeFailureCount(request);
    }

    private String constructKey(final HttpServletRequest request) {
        final String username = request.getParameter(getUsernameParameter());

        if (username == null) {
            return request.getRemoteAddr();
        }

        return ClientInfoHolder.getClientInfo().getClientIpAddress() + ';' + username.toLowerCase();
    }

    private void lockAccount(String userName) {
        Map<String, String> attrs = new HashMap<>();
        attrs.put("userdisable", String.valueOf(FLAG_ACCOUNT_DISABLED));

        try {
            ldapAttributeIO.inject(userName, attrs);
        } catch (PreventedException e) {
            logger.error(e.getMessage());
        }
    }

    private void removeFailureCount(HttpServletRequest request) {
        String key = constructKey(request);
        memcachedIO.delete(key);
    }
}
