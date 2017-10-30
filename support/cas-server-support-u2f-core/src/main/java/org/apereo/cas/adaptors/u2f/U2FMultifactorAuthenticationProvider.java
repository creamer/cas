package org.apereo.cas.adaptors.u2f;

import org.apereo.cas.authentication.AbstractMultifactorAuthenticationProvider;

/**
 * This is {@link U2FMultifactorAuthenticationProvider}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
public class U2FMultifactorAuthenticationProvider extends AbstractMultifactorAuthenticationProvider {
    private static final long serialVersionUID = 157455070794156717L;

    /**
     * Required for serialization and reflection.
     */
    public U2FMultifactorAuthenticationProvider() {
    }

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    public String getFriendlyName() {
        return "FIDO U2F";
    }
}