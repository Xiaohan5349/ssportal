package com.ssportal.be.ldaps;

import com.pingidentity.access.DataSourceAccessor;
import com.ssportal.be.pingid.model.Operation;
import com.ssportal.be.pingid.model.PingIdProperties;
import com.ssportal.be.pingid.service.PingIdOperationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.sourceid.saml20.domain.datasource.info.LdapInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Hashtable;

public class LdapOperationWithoutPing {

    private final LdapProperties props;
    private static final Logger LOG = LogManager.getLogger(LdapOperationWithoutPing.class);

    @Autowired
    private PingIdOperationService pingIdOperationService;

    public LdapOperationWithoutPing() throws IOException {
        this.props = new LdapProperties();
        this.props.loadProperties();
    }

    private DirContext getContext() throws NamingException {
        DirContext context = null;
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, this.props.getServerUrl ());
        env.put(Context.SECURITY_PROTOCOL, "ssl");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, this.props.getPrincipal ());
        env.put(Context.SECURITY_CREDENTIALS, this.props.getCredentials ());
      try {
          context = new InitialDirContext (env);
      } catch(Exception e){
          e.printStackTrace ();
      }

        return context;
    }

    public LdapUser searchUser(String bbbyId) throws GeneralSecurityException, IOException, NamingException {
        DirContext context = getContext();
        LdapUser user = null;
        // Configure filter
        String ldapFilter = null;
        String SearchFilter = this.props.getSearchFilter ();
        ldapFilter = SearchFilter.replaceAll("_username", bbbyId);
        // Fetch following fields from LDAP
        String[] returnAttr = new String[]{
                this.props.getFirstName(), this.props.getLastName(), this.props.getMail(), this.props.getMemberOf()
        };
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        searchControls.setReturningAttributes(returnAttr);
        NamingEnumeration<SearchResult> results = context.search(props.getSearchBaseDN (), ldapFilter, searchControls);
        SearchResult result = null;
        if (results.hasMoreElements()) {
            LOG.info(bbbyId + " found in LDAP. Fetching user attributes " + Arrays.asList(returnAttr));

            result = (SearchResult) results.next();
            if (result.getAttributes() != null) {
                user = new LdapUser();
                Attributes resultAttributes = result.getAttributes();
                // createUserInPingID(bbbyId);
                if (resultAttributes.get(this.props.getFirstName()) != null) {
                    user.setFirstName( resultAttributes.get(this.props.getFirstName()).get().toString ());
                }
                if (resultAttributes.get(this.props.getLastName()) != null) {
                    user.setLastName( resultAttributes.get(this.props.getLastName()).get().toString ());
                }
                if (resultAttributes.get(this.props.getMail()) != null) {
                    user.setEmailAddress( resultAttributes.get(this.props.getMail()).get().toString ());
                }
                user.setUsername(bbbyId);
                if (resultAttributes.get(this.props.getMemberOf()) != null) {
                    NamingEnumeration ne = resultAttributes.get(this.props.getMemberOf()).getAll ();

                    while (ne.hasMore()) {
                        String groupDN = ne.next().toString();
                        if (groupDN.equalsIgnoreCase(this.props.getSoftTokenGroup())) {
                            user.setSoftTokenUser(true);
                        }
                        if (groupDN.equalsIgnoreCase(this.props.getHardTokenGroup())) {
                            user.setHardTokenUser(true);
                        }
                        if (groupDN.equalsIgnoreCase(this.props.getDesktopTokenGroup())) {
                            user.setDesktopTokenUser(true);
                        }
                        String otpToken = this.props.getOtpTokenGroup ();
                        if (groupDN.equalsIgnoreCase ( this.props.getOtpTokenGroup () )) {
                            user.setOtpTokenUser ( true );
                        }
                    }
                }
            }
        }
        context.close();
        return user;
    }

    public void createUserInPingID(String userName) throws IOException {
        PingIdProperties pingIdProperties = new PingIdProperties ();
        pingIdProperties.setProperties ( 0 );
        Operation operation = new Operation ( pingIdProperties.getOrgAlias (), pingIdProperties.getPingid_token (), pingIdProperties.getPingid_use_base64_key (), pingIdProperties.getApi_url () );
        operation.setTargetUser ( userName );
        JSONObject userDetails = pingIdOperationService.getUserDetails ( operation );
        if (userDetails == null) {
            //pingIdOperationService.addUser ( user, false, operation );
        }



        }

//    public boolean isManagerOf(String userDN, String username) throws GeneralSecurityException, IOException, NamingException {
//        DirContext context = getContext();
//        // Configure filter
//        String ldapFilter = this.props.getSearchFilter().replaceAll("_username", username).replaceAll("_manager", userDN);
//        // Fetch following fields from LDAP
//        String[] returnAttr = new String[]{
//            this.props.getFirstName(), this.props.getLastName(), this.props.getMail()
//        };
//        SearchControls controls = new SearchControls();
//        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
//        controls.setReturningAttributes(returnAttr);
//        NamingEnumeration<SearchResult> results = context.search(props.getSearchBaseDN(), ldapFilter, controls);
//        boolean isManager = results.hasMoreElements();
//        context.close();
//        return isManager;
//    }

//    public void checkGroupSubscription(String userDN, boolean isGroupMember) throws NamingException {
//        DirContext context = getContext();
//        boolean present = false;
//        Attributes resultAttributes = context.getAttributes(userDN, new String[]{this.props.getMemberOf()});
//        if (resultAttributes.get(this.props.getMemberOf()) != null) {
//            NamingEnumeration ne = resultAttributes.get(this.props.getMemberOf()).getAll();
//            while (ne.hasMore()) {
//                String groupDN = ne.next().toString();
//                if (groupDN.equalsIgnoreCase(this.props.getMfaGroupDN())) {
//                    present = true;
//                }
//            }
//        }
//        ModificationItem[] mods = new ModificationItem[1];
//        if (isGroupMember && !present) {
//            LOG.info("User not found in group. Adding user " + userDN);
//            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, new BasicAttribute("member", userDN));
//            context.modifyAttributes(this.props.getMfaGroupDN(), mods);
//        } else if (!isGroupMember && present) {
//            LOG.info("User found in group. Removing user " + userDN);
//            mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("member", userDN));
//            context.modifyAttributes(this.props.getMfaGroupDN(), mods);
//        }
//        context.close();
//    }



}
