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
package pl.edu.amu.wmi.kino.rtc.client.connections.services;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.openide.explorer.propertysheet.PropertyEnv;
import org.openide.explorer.propertysheet.PropertyModel;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import javax.swing.Action;
import javax.swing.JPasswordField;
import org.openide.explorer.propertysheet.ExPropertyEditor;
import org.openide.explorer.propertysheet.InplaceEditor;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.Exceptions;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcPasswordAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnectionEvent;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSSLCertificateAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSmartCardAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.connections.wizards.AuthOptions;
import pl.edu.amu.wmi.kino.rtc.client.connections.wizards.AuthOptionsListener;

//TODO: javadoc: lack of author
/**
 * Class represents a {@link RtcRepositoryConnectionImpl} object as a child of RtcRepositoryConnectionsNode.
 */
public class RtcRepositoryConnectionNode extends AbstractNode implements EventListener<RtcRepositoryConnectionEvent> {

    private InstanceContent ic;
    private Sheet.Set licenses = null;

    public RtcRepositoryConnectionNode(InstanceContent ic) {
        super(Children.LEAF, new AbstractLookup(ic));
        this.ic = ic;
        RtcRepositoryConnection repositoryConnection = getLookup().lookup(RtcRepositoryConnection.class);

        updateIcon();
        updateDisplayName();
        setShortDescription(NbBundle.getMessage(RtcRepositoryConnectionNode.class, "RtcRepositoryConnectionNode.tooltip"));
        if (repositoryConnection.isConnected()) {
            setChildren(Children.create(new RtcRepositoryConnectionChildFactory(repositoryConnection), true));
        }
        repositoryConnection.addListener(this);
    }

    /**
     * Gets <code>RtcRepositoryConnection</code> object linked to this node.
     * @return RtcRepositoryConnection
     */
    private RtcRepositoryConnection getRepositoryConnection() {
        return getLookup().lookup(RtcRepositoryConnection.class);
    }

    @Override
    public void eventFired(RtcRepositoryConnectionEvent event) {
        switch (event) {
            case STATUS_CHANGED:
                updateIcon();
                updateDisplayName();
                if (getRepositoryConnection().isConnected()) {
                    setChildren(Children.create(new RtcRepositoryConnectionChildFactory(getRepositoryConnection()), true));
                    if (licenses != null) {
                        getSheet().put(licenses);
                    }
                } else {
                    getSheet().remove("licenses");
                }
                
                break;
            case NAME_CHANGED:
            case AUTHENTICATION_INFO_CHANGED:
                updateDisplayName();
                break;
        }
    }

    private void updateDisplayName() {
        RtcRepositoryConnection repositoryConnection = getLookup().lookup(RtcRepositoryConnection.class);
        if ((RtcPasswordAuthenticationInfo) repositoryConnection.getRtcAuthenticationInfo() == null) {
            System.out.println("null");
        } else {
            setDisplayName(((RtcPasswordAuthenticationInfo) repositoryConnection.getRtcAuthenticationInfo()).getUserName() + "@" + repositoryConnection.getName());
        }
    }

    @Override
    public Action[] getActions(boolean b) {
        List<Action> actions = new LinkedList<Action>();
        actions.addAll(Utilities.actionsForPath("Rtc/Modules/CoreModule/Nodes/RepositoryConnectionActions"));
        actions.addAll(Arrays.asList(super.getActions(b)));
        return actions.toArray(new Action[]{});
    }

    @Override
    protected Sheet createSheet() {
        Sheet toReturn = new Sheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        toReturn.put(set);
        try {
            Property<String> prop = new PropertySupport.Reflection<String>(
                    getRepositoryConnection(),
                    String.class,
                    "url");
            prop.setName(
                    NbBundle.getMessage(RtcRepositoryConnectionNode.class, "RtcRepositoryConnectionNode.properties.repURL.text"));
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcRepositoryConnectionNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        try {
            Property<String> prop = new PropertySupport.Reflection<String>(
                    getRepositoryConnection(),
                    String.class,
                    "name");
            prop.setName(
                    NbBundle.getMessage(RtcRepositoryConnectionNode.class, "RtcRepositoryConnectionNode.properties.connName.text"));
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcRepositoryConnectionNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }


        set.put(new RtcAuthenticationInfoProperty(getRepositoryConnection()));
//        set.put(new RtcPasswordProperty(getRepositoryConnection()));

        try {
            Property<Boolean> prop = new PropertySupport.Reflection<Boolean>(
                    getRepositoryConnection(),
                    Boolean.class,
                    "autoconnect");
            prop.setName(
                    NbBundle.getMessage(RtcRepositoryConnectionNode.class, "RtcRepositoryConnectionNode.properties.autoconnect.text"));
            set.put(prop);
        } catch (NoSuchMethodException ex) {
            //Exceptions.printStackTrace(ex);
            RtcLogger.getLogger(RtcRepositoryConnectionNode.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        
        
        licenses = Sheet.createPropertiesSet();
        licenses.setName("licenses");
        licenses.setDisplayName("Licenses");
        licenses.setValue("tabName", "Licenses");
        
        if (getRepositoryConnection().isConnected()) {            
            toReturn.put(licenses);
        }
        
        
        return toReturn;
    }

    @Override
	public HelpCtx getHelpCtx() {
		return new HelpCtx("usingRationalTeamConcert.Repository"); //TODO monia: helpCtx
	}

	/**
     * Changes node icon depending on connection status.
     */
    private void updateIcon() {
        if (getRepositoryConnection().isConnected()) {
            this.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/repository_rep.gif");
        } else {
            this.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/connections/services/repository_rep_deactivated.gif");
        }
    }
}

class RtcPasswordProperty extends PropertySupport.ReadWrite<String> {

    private RtcRepositoryConnection connection;

    public RtcPasswordProperty(RtcRepositoryConnection connection) {
        super("password", String.class,
                NbBundle.getMessage(RtcPasswordProperty.class, "RtcPasswordProperty.password.text"),
                NbBundle.getMessage(RtcPasswordProperty.class, "RtcPasswordProperty.passwordDesc.text"));
        this.connection = connection;
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        return new RtcPasswordPropertyEditor();
    }

    @Override
    public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        ((RtcPasswordAuthenticationInfo) connection.getRtcAuthenticationInfo()).setPassword(val);
    }

    @Override
    public String getValue() throws IllegalAccessException, InvocationTargetException {
        return ((RtcPasswordAuthenticationInfo) connection.getRtcAuthenticationInfo()).getPassword();
    }
}

class RtcPasswordPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, InplaceEditor.Factory {

    private InplaceEditor ed = null;

    @Override
    public String getAsText() {
        String toReturn = "";
        String pass = super.getAsText();
        for (int i = 0; i < pass.length(); i++) {
            toReturn += "*";
        }
        return toReturn;
    }

    @Override
    public void attachEnv(PropertyEnv env) {
        env.registerInplaceEditorFactory(this);
    }

    @Override
    public InplaceEditor getInplaceEditor() {
        if (ed == null) {
            ed = new Inplace();
        }
        return ed;
    }

    static class Inplace implements InplaceEditor {

        private final JPasswordField picker = new JPasswordField();
        private PropertyEditor editor = null;
        private PropertyModel model;

        @Override
        public void connect(PropertyEditor propertyEditor, PropertyEnv env) {
            editor = propertyEditor;
            reset();
        }

        @Override
        public JComponent getComponent() {
            return picker;
        }

        @Override
        public void clear() {
            //avoid memory leaks:
            editor = null;
            model = null;
        }

        @Override
        public Object getValue() {
            return new String(picker.getPassword());
        }

        @Override
        public void setValue(Object o) {
            picker.setText((String) o);
        }

        @Override
        public boolean supportsTextEntry() {
            return true;
        }

        @Override
        public void reset() {
            String d = (String) editor.getValue();
            if (d != null) {
                picker.setText(d);
            }
        }

        @Override
        public KeyStroke[] getKeyStrokes() {
            return new KeyStroke[0];
        }

        @Override
        public PropertyEditor getPropertyEditor() {
            return editor;
        }

        @Override
        public PropertyModel getPropertyModel() {
            return model;
        }

        @Override
        public void setPropertyModel(PropertyModel propertyModel) {
            this.model = propertyModel;
        }

        @Override
        public boolean isKnownComponent(Component component) {
            return component == picker || picker.isAncestorOf(component);
        }

        @Override
        public void addActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }

        @Override
        public void removeActionListener(ActionListener actionListener) {
            //do nothing - not needed for this component
        }
    }
}

class RtcAuthenticationInfoProperty extends PropertySupport.ReadWrite<RtcAuthenticationInfo> {

    private RtcRepositoryConnection connection;

    public RtcAuthenticationInfoProperty(RtcRepositoryConnection conn) {
        super("authInfo", RtcAuthenticationInfo.class, 
                NbBundle.getMessage(RtcAuthenticationInfoProperty.class, "RtcAuthenticationInfoProperty.authInfo.text"), 
                NbBundle.getMessage(RtcAuthenticationInfoProperty.class, "RtcAuthenticationInfoProperty.authInfoDesc.text"));
        this.connection = conn;
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        return new RtcAuthenticationInfoPropertyEditor();
    }

    public void setValue(RtcAuthenticationInfo val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (connection != null) {
            connection.setRtcAuthenticationInfo(val);
        }
    }

    public RtcAuthenticationInfo getValue() throws IllegalAccessException, InvocationTargetException {
        if (connection != null) {
            return connection.getRtcAuthenticationInfo();
        } else {
            return null;
        }
    }
}

class RtcAuthenticationInfoPropertyEditor extends PropertyEditorSupport implements ExPropertyEditor, AuthOptionsListener {

    private AuthOptions authPanel;
    private PropertyEnv env;

    RtcAuthenticationInfoPropertyEditor() {
        super();
    }

    @Override
    public boolean supportsCustomEditor() {
        return true;
    }

    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        //
    }

    @Override
    public Component getCustomEditor() {
        if (authPanel == null) {
            authPanel = new AuthOptions();
            authPanel.addListener(this);
        }
        Object value = getValue();
        if (getValue() instanceof RtcPasswordAuthenticationInfo) {
            RtcPasswordAuthenticationInfo info = (RtcPasswordAuthenticationInfo) value;
            authPanel.setAuthenticationMethod(0);
            authPanel.setPassword(info.getPassword());
            authPanel.setUserID(info.getUserName());
        } else {
            if (getValue() instanceof RtcSmartCardAuthenticationInfo) {
                RtcSmartCardAuthenticationInfo info = (RtcSmartCardAuthenticationInfo) value;
                authPanel.setCertificate(info.getAlias());
            } else {
                if (getValue() instanceof RtcSSLCertificateAuthenticationInfo) {
                    RtcSSLCertificateAuthenticationInfo info = (RtcSSLCertificateAuthenticationInfo) value;
                    authPanel.setCertFile(info.getCertificateLocation());
                    authPanel.setCertPassword(info.getPassword());
                }
            }
        }
        return authPanel;
    }

    public void attachEnv(PropertyEnv env) {
        this.env = env;
    }

    public void panelChanged() {
        if (!checkValid()) {
            env.setState(PropertyEnv.STATE_INVALID);
        } else {
            RtcAuthenticationInfo rai;
            switch (authPanel.getAuthenticationMethod()) {
                case AuthOptions.AUTH_PASSWORD:
                    String username = authPanel.getUserID();
                    String password = authPanel.getPassword();
                    rai = new RtcPasswordAuthenticationInfo(username, password);
                    break;
                case AuthOptions.AUTH_SMARTCARD:
                    String cert = authPanel.getCertificate();
                    rai = new RtcSmartCardAuthenticationInfo(cert);
                    break;
                case AuthOptions.AUTH_SSL:
                    String name = authPanel.getCertFile();
                    String pass = authPanel.getCertPassword();
                    rai = new RtcSSLCertificateAuthenticationInfo(
                            name, pass);
                    break;
                default:
                    throw new IllegalStateException(
                            NbBundle.getMessage(RtcAuthenticationInfoPropertyEditor.class,
                            "RtcAuthenticationInfoProperty.unsupportedAuth.text"));
            }
            setValue(rai);
            env.setState(PropertyEnv.STATE_VALID);
        }
    }

    private boolean checkValid() {
        if (authPanel == null) {
            return false;
        }
        
        switch (authPanel.getAuthenticationMethod()) {
            case AuthOptions.AUTH_PASSWORD:
                if (authPanel.getUserID().isEmpty()) {
                    return false;
                }
                break;
            case AuthOptions.AUTH_SMARTCARD:
                if (authPanel.getCertificate() == null) {
                    return false;
                }
                break;
            case AuthOptions.AUTH_SSL:
                if (authPanel.getCertFile().isEmpty()) {
                    return false;
                }
                break;
            default:
                return false;
        }
        
        return true;
    }
}
