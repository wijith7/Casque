package org.wso2.carbon.identity.casque.authenticator.authenticator;

import org.mockito.Mock;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.casque.authenticator.constants.CasqueAuthenticatorConstants;
import org.wso2.carbon.user.core.UserStoreManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

//import org.mockito.Mock;

//@PrepareForTest({CasqueAuthenticator.class })
public class CasqueAuthenticatorTest {

    private CasqueAuthenticator casqueAuthenticator;

    @Mock
    CasqueAuthenticator mockedCasqueAuthenticator;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private UserStoreManager userStoreManager;

    @BeforeMethod
    public void setUp() {

        casqueAuthenticator = new CasqueAuthenticator();
        initMocks(this);
    }

    @Test(description = "Test case for GetName() method.")
    public void testGetName() {

        Assert.assertEquals(casqueAuthenticator.getName(),
                CasqueAuthenticatorConstants.AUTHENTICATOR_NAME);
    }

    @Test(description = "Test case for RetryAuthenticationEnabled() method.")
    protected void testRetryAuthenticationEnabled() {

        Assert.assertEquals(casqueAuthenticator.retryAuthenticationEnabled(),
                false);
    }

    @Test(description = "Test case for GetFriendlyName() method.")
    public void testGetFriendlyName() {

        Assert.assertEquals(casqueAuthenticator.getFriendlyName(),
                CasqueAuthenticatorConstants.AUTHENTICATOR_FRIENDLY_NAME);
    }

    @Test(description = "Test case for getContextIdentifier() method.")
    public void testGetContextIdentifier() {

        when(httpServletRequest.getParameter(anyString())).thenReturn("wso2");
        Assert.assertEquals(casqueAuthenticator.getContextIdentifier(httpServletRequest), "wso2");

    }

    @Test(description = "Test case for canHandle() method.")
    public void testCanHandle() {

        Assert.assertEquals(casqueAuthenticator.canHandle(httpServletRequest), true);

    }

}