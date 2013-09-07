/*
 * Copyright (C) 2009-2011 RtcNbClient Team (http://rtcnbclient.wmi.amu.edu.pl/)
 *
 * This file is part of RtcNbClient.
 *
 * RtcNbClient is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RtcNbClient is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RtcNbClient. If not, see <http://www.gnu.org/licenses/>.
 */
package pl.edu.amu.wmi.kino.rtc.client.connections.wizards;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.WizardValidationException;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
//TODO: javadoc: lack of author
public class RtcAddConnectionWizardPanel1
        implements WizardDescriptor.ValidatingPanel<WizardDescriptor>, PropertyChangeListener {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private RtcAddConnectionVisualPanel1 component;
    private WizardDescriptor wDescriptor;
    private boolean isValid = true;
    private boolean nameEdited = false;
    private ResourceBundle bundle = NbBundle.getBundle(RtcAddConnectionWizardPanel1.class);
    private final Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0
    private String errorConnection;

    /** Get the visual component for the panel. In this template, the component
     *  is kept separate. This can be more efficient: if the wizard is created
     *  but never displayed, or not all panels are displayed, it is better to
     *  create only those which really need to be visible.
     */
    public RtcAddConnectionWizardPanel1() {
        super();
        component = new RtcAddConnectionVisualPanel1();
        component.setUserID("");
        component.setRepositoryName("");
        component.setPassword("");
        component.setUrl("");
        component.setAutoconnect(false);
        component.setCertFile("");
        component.setCertificate(null);
        component.setCertPassword("");
        component.setAuthenticationMethod(AuthOptions.AUTH_PASSWORD);
    }

    @Override
    public RtcAddConnectionVisualPanel1 getComponent() {
        return component;
    }

    public void setStartingException(String message) {
        errorConnection = message;
    }

    @Override
    public boolean isValid() {
        // If it is always OK to press Next or Finish, then:
        return isValid;
        // If it depends on some condition (form filled out...), then:
        // return someCondition();
        // and when this condition changes (last form field filled in...) then:

        // and uncomment the complicated stuff below.
    }

    public void setErrorMessage(String message) {
        wDescriptor.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, message);
    }

    public void setInfoMessage(String message) {
        wDescriptor.putProperty(WizardDescriptor.PROP_INFO_MESSAGE, message);
    }

    @Override
    public final void addChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.add(l);
        }
    }

    @Override
    public final void removeChangeListener(ChangeListener l) {
        synchronized (listeners) {
            listeners.remove(l);
        }
    }

    protected final void fireChangeEvent() {
        Iterator<ChangeListener> it;
        synchronized (listeners) {
            it = new HashSet<ChangeListener>(listeners).iterator();

            ChangeEvent ev = new ChangeEvent(this);
            while (it.hasNext()) {
                it.next().stateChanged(ev);
            }
        }
    }

    public boolean setDefaultName() {
        try {
            URL url = new URL(getComponent().getUrl());
            if (!nameEdited) {
                getComponent().setRepositoryName(url.getHost());
                nameEdited = false;
            }
            return true;
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    public boolean checkValidity() {
        RtcConnectionsManager manager =
                Lookup.getDefault().lookup(RtcConnectionsManager.class);

        if (getComponent().getUrl().isEmpty()) {
            setInfoMessage(bundle.getString("UrlEmptyError.name"));
            return false;
        } else if (!setDefaultName()) {
            setErrorMessage(bundle.getString("UrlInvalidFormat.name"));
            return false;
        } else if (getComponent().getRepositoryName().isEmpty()) {
            setInfoMessage(bundle.getString("RepoNameEmptyError.name"));
            return false;
        } else if (getComponent().getAuthenticationMethod() == AuthOptions.AUTH_PASSWORD
                && getComponent().getUserID().isEmpty()) {
            setInfoMessage(bundle.getString("UserIdEmptyError.name"));
            return false;
        } else if (getComponent().getAuthenticationMethod() == AuthOptions.AUTH_SSL
                && getComponent().getCertFile().isEmpty()) {
            setInfoMessage(bundle.getString("CertFileEmptyError.name"));
            return false;
        } else if (getComponent().getAuthenticationMethod() == AuthOptions.AUTH_SMARTCARD
                && getComponent().getCertificate()==null) {
            setInfoMessage(bundle.getString("CertificateEmptyError.name"));
            return false;
        } else if (manager.verifyConnectionURL(
                getComponent().getUrl()) && !getComponent().getUrl().equals(getComponent().getOriginalUrl())) {
            setErrorMessage(bundle.getString("UrlInvalid.name"));
            return false;
        }
        setErrorMessage(errorConnection);
        return true;
    }

    public void validate() throws WizardValidationException {
        String url = component.getUrl();
        String userId = component.getUserID();
        String certFile = component.getCertFile();
        String certificate = component.getCertificate();
        int meth = component.getAuthenticationMethod();
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        if (url.isEmpty()) {
            throw new WizardValidationException(component,
                    NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "UrlEmptyError.name"),
                    null);
        }
        if (meth == AuthOptions.AUTH_PASSWORD && userId.isEmpty()) {
            throw new WizardValidationException(component,
                    NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "UserIdEmptyError.name"),
                    null);
        }

        if (manager.verifyConnectionURL(url)) {
            throw new WizardValidationException(component,
                    NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "UrlInvalid.name"),
                    null);
        }
        if (getComponent().getAuthenticationMethod() == AuthOptions.AUTH_SSL
                && certFile.isEmpty()) {
            throw new WizardValidationException(component,
                    NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "CertFileInvalid.name"),
                    null);
        }
        if (getComponent().getAuthenticationMethod() == AuthOptions.AUTH_SMARTCARD
                && certificate == null) {
            throw new WizardValidationException(component,
                    NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "CertificateInvalid.name"),
                    null);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(RtcAddConnectionVisualPanel1.PROP_NAME)) {
            this.nameEdited = true;
        }
        boolean oldVal = isValid;
        isValid = checkValidity();
        if (oldVal != isValid) {
            fireChangeEvent();
        }
    }

    @Override
    public void readSettings(WizardDescriptor wizardDescriptor) {
        wDescriptor = wizardDescriptor;
        String username = (String) wizardDescriptor.getProperty("userID");
        component.setUserID(username);
        String password = (String) wizardDescriptor.getProperty("userPassword");
        component.setPassword(password);
        String url = (String) wizardDescriptor.getProperty("repositoryUrl");
        component.setUrl(url);
        String repositoryName = (String) wizardDescriptor.getProperty("repositoryName");
        component.setName(repositoryName);
        Boolean autoconnect = (Boolean) wizardDescriptor.getProperty("autoconnect");
        component.setAutoconnect(autoconnect);
        String certPassword = (String) wizardDescriptor.getProperty("certPassword");
        component.setCertPassword(certPassword);
        String certFile = (String) wizardDescriptor.getProperty("certFile");
        component.setCertFile(certFile);
        Integer authMethod = ((Integer) wizardDescriptor.getProperty("authMethod"));
        if (authMethod != null) {
            component.setAuthenticationMethod(authMethod);
        }
        String certificate = (String) wizardDescriptor.getProperty("certificate");
        component.setCertificate(certificate);
        getComponent().addPropertyChangeListener(this);
    }

    @Override
    public void storeSettings(WizardDescriptor wizardDescriptor) {
        wizardDescriptor.putProperty("userID", component.getUserID());
        wizardDescriptor.putProperty("userPassword", component.getPassword());
        wizardDescriptor.putProperty("repositoryUrl", component.getUrl());
        wizardDescriptor.putProperty("repositoryName", component.getRepositoryName());
        wizardDescriptor.putProperty("certPassword", component.getCertPassword());
        wizardDescriptor.putProperty("certFile", component.getCertFile());
        wizardDescriptor.putProperty("authMethod", component.getAuthenticationMethod());
        wizardDescriptor.putProperty("certificate", component.getCertificate());
        wizardDescriptor.putProperty("autoconnect", component.getAutoconnect());
    }

    /**
     *
     * @return the help context
     */
    @Override
    public HelpCtx getHelp() {
        return wDescriptor.getHelpCtx();
    }
}
