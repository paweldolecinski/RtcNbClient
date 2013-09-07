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

import java.awt.event.ItemEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcConnectionsManager;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.connections.RtcRepositoryConnection;
//TODO: javadoc: lack of author
public final class RtcAddConnectionVisualPanel1 extends JPanel
        implements DocumentListener, AuthOptionsListener {

    private static final long serialVersionUID = 7355184019493717375L;
    public static final String PROP_URL = "url";
    public static final String PROP_NAME = "name";
    public static final String PROP_USER = "user";
    public static final String PROP_PASSWORD = "password";
    public static final String PROP_AUTH_OPTIONS = "authOptions";
    private String originalUrl;
    private Document urlDocument;
    private Document nameDocument;
    //TODO: javadoc: properties?

    /** Creates new form RtcAddConnectionVisualPanel1 */
    public RtcAddConnectionVisualPanel1() {
        initComponents();
        if (urlComboBox.getEditor().getEditorComponent() instanceof JTextField) {
            JTextComponent txt = (JTextComponent) urlComboBox.getEditor().getEditorComponent();
            urlDocument = txt.getDocument();
            urlDocument.addDocumentListener(this);
        }
        if (nameComboBox.getEditor().getEditorComponent() instanceof JTextField) {
            JTextComponent txt = (JTextComponent) nameComboBox.getEditor().getEditorComponent();
            nameDocument = txt.getDocument();
            nameDocument.addDocumentListener(this);
        }

        RtcConnectionsManager manager = Lookup.getDefault().lookup(RtcConnectionsManager.class);
        for (RtcRepositoryConnection conn : manager.getRepositoryConnections()) {
            urlComboBox.addItem(conn.getUrl());
            nameComboBox.addItem(conn.getName());
        }

        urlComboBox.setSelectedItem("");
        nameComboBox.setSelectedItem("");
        
        authOptions2.addListener(this);

//        ((JTextComponent)urlComboBox.getEditor().getEditorComponent()).getDocument().addDocumentListener(this);
//        ((JTextComponent)nameComboBox.getEditor().getEditorComponent()).getDocument().addDocumentListener(this);
//        nameComboBox.addActionListener(this);
    }

    /**
     *
     * @return name of RtcAddConnectionVisualPanel1
     */
    @Override
    public String getName() {
        return NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "RtcAddConnectionStep.name");
    }

    public Boolean getAutoconnect() {
        Boolean autoconnect = autoconnectCheckbox.isSelected();
        if (autoconnect == null) {
            autoconnect = false;
        }
        return autoconnect;
    }

    public void setRepositoryNameEditable(Boolean bln) {
        nameComboBox.setEnabled(bln);
        nameComboBox.setEditable(bln);
    }

    public void setAutoconnect(Boolean bln) {
        if (bln != null) {
            autoconnectCheckbox.setSelected(bln);
        } else {
            autoconnectCheckbox.setSelected(false);
        }
    }

    /**
     *
     * @return the name of repositiry
     */
    public String getRepositoryName() {
        String name = nameComboBox.getEditor().getItem().toString();
        if (name == null) {
            name = "";
        }
        return name;
    }

    /**
     * Set repository name.
     * @param s - the name of repository
     */
    public void setRepositoryName(String s) {
        nameComboBox.setSelectedItem(s);
    }

    public String getUrl() {
        String url = urlComboBox.getEditor().getItem().toString().trim();
        if (url == null) {
            return "";
        }
        return url;
    }

    public void setUrl(String url) {
        urlComboBox.setSelectedItem(url);
        originalUrl = url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void insertUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    public void removeUpdate(DocumentEvent e) {
        changedUpdate(e);
    }

    public void changedUpdate(DocumentEvent e) {
        if (e.getDocument() == urlDocument) {
            setDefaultName();
            firePropertyChange(PROP_URL, 0, 1);
        } else if (e.getDocument() == nameDocument) {
            firePropertyChange(PROP_NAME, 0, 1);
        }
    }

    public void setDefaultName() {
        try {
            URI uri = new URI(getUrl());
            setRepositoryName(uri.getHost());
        } catch (URISyntaxException ex) {
//            Exceptions.printStackTrace(ex);
              RtcLogger.getLogger(RtcAddConnectionVisualPanel1.class).log(Level.INFO, ex.getLocalizedMessage(), ex);
        }
    }
    public boolean getNameStatus() {
        return nameComboBox.isEnabled();
    }

    public void setUserID(String userId) {
        authOptions2.setUserID(userId);
    }

    public void setPassword(String pass) {
        authOptions2.setPassword(pass);
    }

    public void setCertificate(String selected) {
        authOptions2.setCertificate(selected);
    }

    public void setCertPassword(String pass) {
        authOptions2.setCertPassword(pass);
    }

    public void setCertFile(String filePath) {
        authOptions2.setCertFile(filePath);
    }

    public void setAuthenticationMethod(int method) {
        authOptions2.setAuthenticationMethod(method);
    }

    public void itemStateChanged(ItemEvent e) {
        authOptions2.itemStateChanged(e);
    }

    public String getUserID() {
        return authOptions2.getUserID();
    }

    public String getPassword() {
        return authOptions2.getPassword();
    }

    public String getCertificate() {
        return authOptions2.getCertificate();
    }

    public String getCertPassword() {
        return authOptions2.getCertPassword();
    }

    public String getCertFile() {
        return authOptions2.getCertFile();
    }

    public int getAuthenticationMethod() {
        return authOptions2.getAuthenticationMethod();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        autoconnectCheckbox = new javax.swing.JCheckBox();
        urlComboBox = new javax.swing.JComboBox();
        nameLabel = new javax.swing.JLabel();
        urlLabel = new javax.swing.JLabel();
        nameComboBox = new javax.swing.JComboBox();
        authOptions2 = new pl.edu.amu.wmi.kino.rtc.client.connections.wizards.AuthOptions();

        org.openide.awt.Mnemonics.setLocalizedText(autoconnectCheckbox, org.openide.util.NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "RtcAddConnectionVisualPanel1.autoconnectCheckbox.text")); // NOI18N
        autoconnectCheckbox.setMargin(null);

        urlComboBox.setEditable(true);
        urlComboBox.setFont(urlComboBox.getFont());
        urlComboBox.setName("urlTextField"); // NOI18N

        nameLabel.setFont(nameLabel.getFont());
        org.openide.awt.Mnemonics.setLocalizedText(nameLabel, org.openide.util.NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "RtcAddConnectionVisualPanel1.nameLabel.text")); // NOI18N

        urlLabel.setFont(urlLabel.getFont());
        org.openide.awt.Mnemonics.setLocalizedText(urlLabel, org.openide.util.NbBundle.getMessage(RtcAddConnectionVisualPanel1.class, "RtcAddConnectionVisualPanel1.urlLabel.text")); // NOI18N

        nameComboBox.setEditable(true);
        nameComboBox.setFont(nameComboBox.getFont());
        nameComboBox.setName("serverNameTextField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(urlLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(nameLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autoconnectCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                    .addComponent(nameComboBox, 0, 438, Short.MAX_VALUE)
                    .addComponent(urlComboBox, 0, 438, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(authOptions2, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(urlComboBox)
                    .addComponent(urlLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameComboBox)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(autoconnectCheckbox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authOptions2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private pl.edu.amu.wmi.kino.rtc.client.connections.wizards.AuthOptions authOptions2;
    private javax.swing.JCheckBox autoconnectCheckbox;
    private javax.swing.JComboBox nameComboBox;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JComboBox urlComboBox;
    private javax.swing.JLabel urlLabel;
    // End of variables declaration//GEN-END:variables

    public void panelChanged() {
        firePropertyChange(PROP_AUTH_OPTIONS, 0, 1);
    }
}
