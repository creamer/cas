package org.jasig.cas.nhnent.util;

import org.jasig.cas.authentication.PreventedException;
import org.ldaptive.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ruaa on 2016. 4. 20..
 */
public class LdapAttributeIO {

    @Autowired
    private ConnectionFactory connectionFactory;
    @NotNull
    private String baseDn;
    @NotNull
    private String userFilter;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setUserFileter(String userFileter) {
        this.userFilter = userFileter;
    }

    public void setBaseDn(String baseDn) {
        this.baseDn = baseDn;
    }

    public LdapEntry retrieve(String user, String... attrs) throws PreventedException{
        LdapEntry ldapEntry = null;
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            connection.open();

            String.format("%s,%s", attrs);

            SearchOperation search = new SearchOperation(connection);
            SearchRequest searchRequest = new SearchRequest(baseDn, createSearchFilter(user));
            searchRequest.setReturnAttributes(attrs);

            SearchResult result = search.execute(searchRequest).getResult();

            ldapEntry = result.getEntry();
        } catch (LdapException e) {
            throw new PreventedException("Unexpected LDAP error", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return ldapEntry;
    }

    public void inject(String user, Map<String, String> attrs) throws PreventedException {
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            connection.open();

            LdapEntry ldapEntry = retrieve(user, "cn", "email");

            ModifyOperation modify = new ModifyOperation(connection);
            AttributeModification[] attributeModifications = new AttributeModification[attrs.size()];

            int attrIndex = 0;
            Iterator iterator = attrs.keySet().iterator();
            while (iterator.hasNext()) {
                String attributeName = (String)iterator.next();
                attributeModifications[attrIndex] = new AttributeModification(AttributeModificationType.REPLACE,
                        new LdapAttribute(attributeName, attrs.get(attributeName)));

                attrIndex++;
            }

            ModifyRequest modifyRequest = new ModifyRequest(ldapEntry.getDn(), attributeModifications);
            modify.execute(modifyRequest);
        } catch (LdapException e) {
            throw new PreventedException("Unexpected LDAP error", e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected SearchFilter createSearchFilter(String user) {
        SearchFilter filter = new SearchFilter();
        if(this.userFilter != null) {
            this.logger.debug("searching for DN using userFilter");
            filter.setFilter(this.userFilter);
            filter.setParameter("user", user);
        } else {
            this.logger.error("Invalid userFilter, cannot be null or empty.");
        }

        return filter;
    }
}
