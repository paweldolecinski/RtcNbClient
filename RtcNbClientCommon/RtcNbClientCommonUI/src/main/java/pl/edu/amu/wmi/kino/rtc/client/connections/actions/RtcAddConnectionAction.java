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
package pl.edu.amu.wmi.kino.rtc.client.connections.actions;

import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcPasswordAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSSLCertificateAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcSmartCardAuthenticationInfo;
import pl.edu.amu.wmi.kino.rtc.client.connections.wizards.AuthOptions;
import pl.edu.amu.wmi.kino.rtc.client.connections.wizards.RtcAddConnectionWizardPanel1;

/**
 * Action creates Add New Connection Wizard.
 * @author Tomasz Adamski (tomasz.adamski@gmail.com)
 */
public final class RtcAddConnectionAction extends CallableSystemAction {

    private static final long serialVersionUID = 1337666235711131719L;
    private WizardDescriptor.Panel<WizardDescriptor>[] panels;
//TODO zmodyfikować akcję

    @Override
    public void performAction() {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        // {0} will be replaced by WizardDesriptor.Panel.getComponent().getName()
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle(NbBundle.getMessage(RtcAddConnectionAction.class, "RtcAddConnectionWizardTitle.name"));
        wizardDescriptor.setHelpCtx(new HelpCtx(RtcAddConnectionAction.class));
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            String url = (String) wizardDescriptor.getProperty("repositoryUrl");
//            String url = component.getUrl();
            String repositoryName = (String) wizardDescriptor.getProperty("repositoryName");
//            String repositoryName = component.getRepositoryName();
            boolean autoconnect = (Boolean) wizardDescriptor.getProperty("autoconnect");

            RtcAuthenticationInfo rai;
            switch (((Integer) wizardDescriptor.getProperty("authMethod")).intValue()) {
                case AuthOptions.AUTH_PASSWORD:
                    String username = (String) wizardDescriptor.getProperty("userID");
                    String password = (String) wizardDescriptor.getProperty("userPassword");
                    rai = new RtcPasswordAuthenticationInfo(username, password);
                    break;
                case AuthOptions.AUTH_SMARTCARD:
                    String cert = (String) wizardDescriptor.getProperty("certificate");
                    rai = new RtcSmartCardAuthenticationInfo(cert);
                    break;
                case AuthOptions.AUTH_SSL:
                    String name = (String) wizardDescriptor.getProperty("certFile");
                    String pass = (String) wizardDescriptor.getProperty("certPassword");
                    rai = new RtcSSLCertificateAuthenticationInfo(
                            name, pass);
                    break;
                default:
                    throw new IllegalStateException("Unsupported authentication option");
            }
            RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);

            manager.addRepositoryConnection(url, repositoryName, rai, autoconnect);
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = (WizardDescriptor.Panel<WizardDescriptor>[]) new WizardDescriptor.Panel[]{
                        new RtcAddConnectionWizardPanel1()};

            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                // Default step name to component name of panel. Mainly useful
                // for getting the name of the target chooser to appear in the
                // list of steps.
                steps[i] = c.getName();
                if (c instanceof JComponent) { // assume Swing components
                    JComponent jc = (JComponent) c;
                    // Sets step number of a component
                    // TODO if using org.openide.dialogs >= 7.8, can use WizardDescriptor.PROP_*:
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    // Sets steps names for a panel
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    // Turn on subtitle creation on each step
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    // Show steps on the left side with the image on the background
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    // Turn on numbering of all steps
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.FALSE);
                }
            }
        }
        return panels;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(RtcAddConnectionAction.class, "RtcAddConnectionAction.name");
    }

    /**
     * @return null
     */
    @Override
    public String iconResource() {
        return null;
    }

    /**
     *
     * @return the help context
     */
    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(RtcAddConnectionAction.class);
    }

    /**
     * @return always true
     */
    @Override
    protected boolean asynchronous() {
        return true;
    }

    /**
     * @return true if RtcRepositoryConnectiond is running
     */
    @Override
    public boolean isEnabled() {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        return manager.isTeamPlatformActive();
    }
}
