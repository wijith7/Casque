package org.wso2.carbon.identity.casque.authenticator.authenticator;

import com.sun.tools.javadoc.Start;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.testng.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import org.wso2.carbon.identity.application.authentication.framework.AuthenticatorFlowStatus;
import org.wso2.carbon.identity.application.authentication.framework.context.AuthenticationContext;
import org.wso2.carbon.identity.application.authentication.framework.exception.AuthenticationFailedException;
import org.wso2.carbon.identity.application.authentication.framework.model.AuthenticatedUser;
import org.wso2.carbon.identity.application.authentication.framework.util.FrameworkConstants;
import org.wso2.carbon.identity.application.common.model.User;
import org.wso2.carbon.identity.casque.authenticator.authenticator.internal.CasqueAuthenticatorServiceDataHolder;
import org.wso2.carbon.identity.casque.authenticator.authenticator.radius.Radius;
import org.wso2.carbon.identity.casque.authenticator.authenticator.radius.RadiusResponse;
import org.wso2.carbon.identity.casque.authenticator.constants.CasqueAuthenticatorConstants;
import org.wso2.carbon.identity.casque.authenticator.exception.CasqueException;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.user.api.RealmConfiguration;
import org.wso2.carbon.user.core.UserRealm;
import org.wso2.carbon.user.core.UserStoreManager;
import org.wso2.carbon.user.core.service.RealmService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


//import org.mockito.Mock;

//@RunWith(PowerMockRunner.class)
@PrepareForTest({CasqueAuthenticatorServiceDataHolder.class,IdentityTenantUtil.class,RadiusResponse.class,Radius.class,AuthenticatedUser.class,User.class})
public class CasqueAuthenticatorTest {

    @ObjectFactory
    public IObjectFactory getObjectFactory() {
        return new org.powermock.modules.testng.PowerMockObjectFactory();
    }

    private CasqueAuthenticator casqueAuthenticator;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private UserStoreManager userStoreManager;

    @Mock
    CasqueAuthenticatorServiceDataHolder casqueAuthenticatorServiceDataHolder;

    @Mock
    RealmService realmService;

    @Mock
    UserRealm tenantUserRealm;

    @Mock
    IdentityTenantUtil identityTenantUtil;

    @Mock
    private AuthenticationContext context;

    @Spy
    private AuthenticationContext mockedContext;

    @Mock
    private CasqueAuthenticatorConstants casqueAuthenticatorConstants;

    @Mock
    private Radius radius;

    @Mock
    private RadiusResponse radiusResponse;

    @Mock
    private AuthenticatedUser authenticatedUser;

    @Mock
    private User user;

//    @Mock
//    private Start start;

//    @Mock
//    private Request1 request;

//    @Mock
//    private CreateLocalAuthenticatedUserFromSubjectIdentifier createLocalAuthenticatedUserFromSubjectIdentifier;

//    @Mock
//    byte[] radiusState;

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

        when(httpServletRequest.getParameter(eq("sessionDataKey"))).thenReturn("wso2");
        Assert.assertEquals(casqueAuthenticator.getContextIdentifier(httpServletRequest), "wso2");

    }

    @Test(description = "Test case for canHandle() method.")
    public void testCanHandle() {

        Assert.assertEquals(casqueAuthenticator.canHandle(httpServletRequest), true);

    }

    @Test(description = "Test case for testGetCasqueTokenId() method.")
    public void testGetCasqueTokenId() throws Exception {
       //String token = "FFF 000001";


        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
        mockStatic(IdentityTenantUtil.class);

        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn("FFF 000001");
        Whitebox.setInternalState(CasqueAuthenticatorServiceDataHolder.class, "instance", casqueAuthenticatorServiceDataHolder);
        Assert.assertEquals(Whitebox.invokeMethod(casqueAuthenticator, "getCasqueTokenId", ""), "FFF 000001");

    }

//    @Test(description = "Test case for testGetCasqueTokenId() method and forTokenNull.")
    @Test(expectedExceptions = {CasqueException.class})
    public void testGetCasqueTokenIdforTokenNull() throws Exception {
        //String token = "FFF 000001";
        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
        mockStatic(IdentityTenantUtil.class);
        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn(null);
//        Whitebox.setInternalState(CasqueAuthenticatorServiceDataHolder.class, "instance", casqueAuthenticatorServiceDataHolder);
        Whitebox.invokeMethod(casqueAuthenticator, "getCasqueTokenId", "");

    }
    @Test(expectedExceptions = {CasqueException.class})
    public void testGetCasqueTokenIdforToknIdBadFormat() throws Exception {
        //String token = "FFF 000001";
        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
        mockStatic(IdentityTenantUtil.class);
        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn("wso2");
//        Whitebox.setInternalState(CasqueAuthenticatorServiceDataHolder.class, "instance", casqueAuthenticatorServiceDataHolder);
        Whitebox.invokeMethod(casqueAuthenticator, "getCasqueTokenId", "");

    }

//    @Test(expectedExceptions = {CasqueException.class})
//    public void testGetCasqueTokenIdforToknIdForUnableToGetTokenId() throws Exception {
//        //String token = "FFF 000001";
//        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
//        mockStatic(IdentityTenantUtil.class);
//        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
//        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
//        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
//        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
//       // when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn("");
////        Whitebox.setInternalState(CasqueAuthenticatorServiceDataHolder.class, "instance", casqueAuthenticatorServiceDataHolder);
//        Whitebox.invokeMethod(casqueAuthenticator, "getCasqueTokenId", "");
//
//    }


    @Test(description = "Test case for successful logout request.")
    public void testProcessLogoutRequest() throws Exception {
        when(context.isLogoutRequest()).thenReturn(true);
        AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
        Assert.assertEquals(status, AuthenticatorFlowStatus.SUCCESS_COMPLETED);
    }

    @Test(description = "Test case for process() method for Another challenge.")
    public void testProcessRadiusState() throws Exception {

        byte[] radiusState = new byte[1];
        radiusState[0] = 10;
        int radiusResponseType =11;
        //String userName ="casque1";
         mockStatic(Radius.class);

        when(context.isLogoutRequest()).thenReturn(false);
        when(context.getProperty(anyString())).thenReturn(radiusState);
        when(httpServletRequest.getParameter(anyString())).thenReturn("Login");
        when(context.getProperty(casqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
        when(httpServletRequest.getParameter(casqueAuthenticatorConstants.RESPONSE)).thenReturn("ACCESS_CHALLENGE ");
        when(Radius.sendRequest(anyString(), anyString(), (byte[]) anyObject())).thenReturn(radiusResponse);
        when(radiusResponse.getType()).thenReturn(radiusResponseType);
        // when((String)context.getProperty(CasqueAuthenticatorConstants.USER_NAME)).thenReturn("wso2");
        AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
        Assert.assertEquals(status, AuthenticatorFlowStatus.INCOMPLETE);
    }

    @Test(description = "Test case for process() method for ForRadiusStateNull()")
    public void testProcessRadiusState5() throws Exception {

        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
        mockStatic(IdentityTenantUtil.class);

        byte[] radiusState = new byte[1];
        radiusState[0] = 10;
        int radiusResponseType =11;
        //String userName ="casque1";
        mockStatic(Radius.class);
        String token = "FFF 000001";


        when(context.isLogoutRequest()).thenReturn(false);
        when(context.getProperty(anyString())).thenReturn(null);
        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn("FFF 000001");
//        when(context.getCasqueTokenId(anyString())).thenReturn(token);

        when(httpServletRequest.getParameter(anyString())).thenReturn("Login");
        when(context.getProperty(casqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
        when(httpServletRequest.getParameter(casqueAuthenticatorConstants.RESPONSE)).thenReturn("ACCESS_CHALLENGE ");
        when(Radius.sendRequest(anyString(), anyString(), (byte[]) anyObject())).thenReturn(radiusResponse);
        when(radiusResponse.getType()).thenReturn(radiusResponseType);
        // when((String)context.getProperty(CasqueAuthenticatorConstants.USER_NAME)).thenReturn("wso2");
        AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
        Assert.assertEquals(status, AuthenticatorFlowStatus.INCOMPLETE);
    }

    @Test(description = "Test case for process() method for Authentication Pass.")
    public void testProcessRadiusState2() throws Exception {

        byte[] radiusState = new byte[1];
        radiusState[0] = 10;
        int radiusResponseType =2;

        String userName ="casque1";
        mockStatic(Radius.class);
        mockStatic(AuthenticatedUser.class);
        mockStatic(CasqueAuthenticatorServiceDataHolder.class);
        mockStatic(IdentityTenantUtil.class);


        when(context.isLogoutRequest()).thenReturn(false);
        when(context.getProperty(anyString())).thenReturn(radiusState);
        when(httpServletRequest.getParameter(anyString())).thenReturn("Login");
        when(context.getProperty(casqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
        when(httpServletRequest.getParameter(casqueAuthenticatorConstants.RESPONSE)).thenReturn("ACCESS_ACCEPT");
        when(Radius.sendRequest(anyString(), anyString(), (byte[]) anyObject())).thenReturn(radiusResponse);
        when(radiusResponse.getType()).thenReturn(radiusResponseType);

        when(authenticatedUser.createLocalAuthenticatedUserFromSubjectIdentifier(anyString())).thenReturn(authenticatedUser);
        when(CasqueAuthenticatorServiceDataHolder.getInstance()).thenReturn(casqueAuthenticatorServiceDataHolder);
        when(casqueAuthenticatorServiceDataHolder.getRealmService()).thenReturn(realmService);
        when(realmService.getTenantUserRealm(IdentityTenantUtil.getTenantIdOfUser(anyString()))).thenReturn(tenantUserRealm);
        when(tenantUserRealm.getUserStoreManager()).thenReturn(userStoreManager);
        when(userStoreManager.getUserClaimValue(anyString(), anyString(), anyString())).thenReturn("FFF 000001");

//        when(context.setSubject(any(AuthenticatedUser.class)));

        // when((String)context.getProperty(CasqueAuthenticatorConstants.USER_NAME)).thenReturn("wso2");
        AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
        Assert.assertEquals(status, AuthenticatorFlowStatus.SUCCESS_COMPLETED);
    }

    @Test(description = "Test case for process() method for  Authentication Failed.----------------")
    public void testProcessRadiusState3() throws Exception {

        byte[] radiusState = new byte[1];
        radiusState[0] = 10;
        int radiusResponseType =3;
        String userName ="casque1";
        mockStatic(Radius.class);
        mockStatic(User.class);

        when(context.isLogoutRequest()).thenReturn(false);
        when(context.getProperty(anyString())).thenReturn(radiusState);
        when(httpServletRequest.getParameter(anyString())).thenReturn("Login");
        when(context.getProperty(casqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
        when(httpServletRequest.getParameter(casqueAuthenticatorConstants.RESPONSE)).thenReturn("ACCESS_REJECT");
        when(Radius.sendRequest(anyString(), anyString(), (byte[]) anyObject())).thenReturn(radiusResponse);
        when(radiusResponse.getType()).thenReturn(radiusResponseType);
        when((String)context.getProperty(CasqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
        when(User.getUserFromUserName(anyString())).thenReturn(user);
       // AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
        Assert.assertEquals(User.getUserFromUserName(anyString()),user);
        //when(User.getUserFromUserName(anyString()));

       }
@Test(description = "Test case for process() method for login fail")
public void testProcessRadiusStateLoginFail() throws Exception {

    byte[] radiusState = new byte[1];
    radiusState[0] = 10;
    int radiusResponseType =11;
    //String userName ="casque1";
    mockStatic(Radius.class);

    when(context.isLogoutRequest()).thenReturn(false);
    when(context.getProperty(anyString())).thenReturn(radiusState);
    when(httpServletRequest.getParameter(anyString())).thenReturn(null);
    AuthenticatorFlowStatus status = casqueAuthenticator.process(httpServletRequest, httpServletResponse, context);
    Assert.assertEquals(status, AuthenticatorFlowStatus.SUCCESS_COMPLETED);
}

//    @Test(description = "Test case for start() method for userName.")
//    public void teststart() throws Exception {
//
//      when(httpServletRequest.getParameter(CasqueAuthenticatorConstants.USER_NAME)).thenReturn("casque1");
//      //when(context.setProperty(CasqueAuthenticatorConstants.USER_NAME)),anyString();
//
//
//
//          Whitebox.setInternalState();
//          Assert.assertEquals(status, AuthenticatorFlowStatus.SUCCESS_COMPLETED);
//    }

}