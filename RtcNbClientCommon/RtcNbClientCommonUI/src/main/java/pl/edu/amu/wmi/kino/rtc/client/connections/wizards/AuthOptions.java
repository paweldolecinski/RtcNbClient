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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;

/**
 *
 * @author Patryk Żywica
 */
public class AuthOptions extends JPanel implements ItemListener,AuthOptionsListener{

    private static final long serialVersionUID = 3465125L;
    private CardLayout layout;
    private JComboBox combo;
    private JPanel panel;
    private final SSLPanel sSLPanel;
    private final SmartCardPanel smartCardPanel;
    private final UsernamePanel usernamePanel;
    

    public static final int AUTH_PASSWORD = 0;
    public static final int AUTH_SMARTCARD = 1;
    public static final int AUTH_SSL = 2;
    

    public AuthOptions() {
        layout = new CardLayout();
        setLayout(new BorderLayout());

        panel = new JPanel(layout);
        usernamePanel = new UsernamePanel();
        panel.add(usernamePanel, "pass",AUTH_PASSWORD);
        smartCardPanel = new SmartCardPanel();
        panel.add(smartCardPanel, "card",AUTH_SMARTCARD);
        sSLPanel = new SSLPanel();
        panel.add(sSLPanel, "cert",AUTH_SSL);

        combo = new JComboBox();
        combo.addItem(
                NbBundle.getMessage(AuthOptions.class, "AuthOptions.methods.password.text"));
        combo.addItem(
                NbBundle.getMessage(AuthOptions.class, "AuthOptions.methods.smartCard.text"));
        combo.addItem(
                NbBundle.getMessage(AuthOptions.class, "AuthOptions.methods.sslCertificate.text"));
        
        combo.addItemListener(this);

        
        add(combo, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        
        usernamePanel.addListener(this);
        sSLPanel.addListener(this);
        smartCardPanel.addListener(this);
        
    }
    
    public int getAuthenticationMethod() {
        return combo.getSelectedIndex();
    }

    public void setAuthenticationMethod(int method) {
        if (method >=0 && method < 3) {
            combo.setSelectedIndex(method);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (combo.getItemAt(0).equals(e.getItem())) {
            layout.show(panel, "pass");
        }
        if (combo.getItemAt(1).equals(e.getItem())) {
            layout.show(panel, "card");
        }
        if (combo.getItemAt(2).equals(e.getItem())) {
            layout.show(panel, "cert");
        }
        fireChange();
    }

    public void setCertPassword(String pass) {
        sSLPanel.setCertPassword(pass);
    }

    public void setCertFile(String filePath) {
        sSLPanel.setCertFile(filePath);
    }

    public String getCertPassword() {
        return sSLPanel.getCertPassword();
    }

    public String getCertFile() {
        return sSLPanel.getCertFile();
    }

    public void setCertificate(String selected) {
        smartCardPanel.setCertificate(getPossibleCertificates(), selected);
    }

    public String getCertificate() {
        return smartCardPanel.getCertificate();
    }

    public void setUserID(String userId) {
        usernamePanel.setUserID(userId);
    }

    public void setPassword(String pass) {
        usernamePanel.setPassword(pass);
    }

    public String getUserID() {
        return usernamePanel.getUserID();
    }

    public String getPassword() {
        return usernamePanel.getPassword();
    }
    
    private String[] getPossibleCertificates() {
        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        return manager.getSmardCardAliases();
    }
    
    private final Set<AuthOptionsListener> listeners = Collections.synchronizedSet(new HashSet<AuthOptionsListener>());
    public void addListener(AuthOptionsListener l){
        listeners.add(l);
    }
    
    public void removeListener(AuthOptionsListener l){
        listeners.remove(l);
    }
    
    protected void fireChange(){
        synchronized(listeners){
            for(AuthOptionsListener l : listeners){
                l.panelChanged();
            }
        }
    }

    public void panelChanged() {
        fireChange();
    }

}
