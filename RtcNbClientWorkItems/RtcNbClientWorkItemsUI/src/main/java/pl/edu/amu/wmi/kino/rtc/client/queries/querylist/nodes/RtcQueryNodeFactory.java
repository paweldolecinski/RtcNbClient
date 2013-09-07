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
package pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes;

import java.awt.Color;
import java.awt.Dialog;
import java.util.logging.Level;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.DeleteCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.DuplicateCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.actions.cookies.RenameCookie;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQuery;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.favorites.api.helpers.RtcAddToFavoritesFilterNode;
import pl.edu.amu.wmi.kino.rtc.client.queries.result.OpenCookieImpl;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = RtcQueryNodeFactory.class)
public class RtcQueryNodeFactory {

    public Node createNode(RtcQueriesSet set, RtcQuery key) {
        return (new RtcAddToFavoritesFilterNode(createNodeWithoutFavorites(set, key),
                key.getQueriesManager().getAvtiveProjectArea(),
                Lookup.getDefault().lookup(RtcQueriesResourceProviderImpl.class)));
    }

    protected Node createNodeWithoutFavorites(RtcQueriesSet set, RtcQuery key){
                InstanceContent ic = new InstanceContent();
        ic.add(key);
        ic.add(set);
        RtcQueryNode node = new RtcQueryNode(ic);
        node.setName(key.getEditableName());
        node.setIconBaseWithExtension("pl/edu/amu/wmi/kino/rtc/client/queries/resources/queries.gif");

        ic.add(new OpenCookieImpl(key));
        ic.add(new EditCookieImpl(key, set));
        ic.add(new DuplicateCookieImpl(key));
        //ic.add(new AddToFavoritesCookieImpl());
        if (set instanceof RtcEditableQueriesSet) {
            ic.add(new DeleteCookieImpl((RtcEditableQueriesSet) set, key));
        }
        if (key instanceof RtcEditableQuery) {
            ic.add(new RenameCookieImpl((RtcEditableQuery) key));
        }
        return node;
    }
}

//class AddToFavoritesCookieImpl implements AddToFavoritesCookie {
//
//
//    @Override
//    public Node getReference(String resourceId, RtcActiveProjectArea projectArea) throws UnableToGetTheReference {
//        RtcQueriesManager manager = Lookup.getDefault().lookup(RtcQueriesManagerFactory.class).getManager(projectArea);
//
//        return (Lookup.getDefault().lookup(RtcQueryNodeFactory.class)).createNode(manager.findQuery(resourceId).getSecond(), manager.findQuery(resourceId).getFirst());
//    }
//
//    @Override
//    public boolean enableForContext(Lookup context) {
//        if(context.lookup(new Template(IQueryDescriptor.class))!=null){
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public String getResourceId(Lookup context) {
//        IQueryDescriptor q = context.lookup(IQueryDescriptor.class);
//        return String.valueOf(q.getId());
//    }
//
//    @Override
//    public RtcActiveProjectArea getProjectArea(Lookup context) {
//        final IQueryDescriptor q = context.lookup(IQueryDescriptor.class);
//        return new RtcActiveProjectArea() {
//
//            @Override
//            public IProjectArea getProjectArea() {
//                try {
//                    return (IProjectArea) getRepositoryConnection().itemManager().fetchCompleteItem(q.getProjectArea(), ItemManager.DEFAULT, null);
//                } catch (TeamRepositoryException ex) {
//                    RtcLogger.getLogger().log(Level.SEVERE, ex.getLocalizedMessage());
//                }
//                return null;
//            }
//
//            @Override
//            public ITeamRepository getRepositoryConnection() {
//                return (ITeamRepository) q.getOrigin();
//            }
//        };
//    }
//
//
//}

class DuplicateCookieImpl implements DuplicateCookie {

    private RtcQuery query;

    public DuplicateCookieImpl(RtcQuery query) {
        this.query = query;
    }

    @Override
    public void duplicate() {
        final javax.swing.JTextField text = new javax.swing.JTextField();
        text.setText(query.getEditableName());
        javax.swing.JPanel panel = new javax.swing.JPanel();
        javax.swing.JLabel label = new javax.swing.JLabel();
        DialogDescriptor dd = new DialogDescriptor(panel, NbBundle.getMessage(RtcQueryNode.class, "RenamePanel.name"), true, null);


        label.setText(NbBundle.getMessage(RtcQueryNode.class, "NewName.name"));
        text.setColumns(20);
        text.setText(query.getEditableName());
        text.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if ((text.getBackground().equals(Color.pink)) && !(text.getText().equals(""))) {
                    text.setBackground(Color.white);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (text.getText().equals("")) {
                    text.setBackground(Color.pink);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        panel.add(label);
        panel.add(text);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);

        if (dd.getValue().equals(DialogDescriptor.OK_OPTION)) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    try {
                        query.saveAs(text.getText());
                    } catch (RtcSaveException ex) {
                        RtcLogger.getLogger(RtcQueryNodeFactory.class)
                                .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    }
                }
            });
        }
    }
}

class DeleteCookieImpl implements DeleteCookie {

    private RtcQuery query;
    private RtcEditableQueriesSet set;

    public DeleteCookieImpl(RtcEditableQueriesSet set, RtcQuery query) {
        this.query = query;
        this.set = set;
    }

    @Override
    public void delete() {
        set.removeQuery(query);
    }
}

class RenameCookieImpl implements RenameCookie {

    private RtcEditableQuery query;

    public RenameCookieImpl(RtcEditableQuery query) {
        this.query = query;
    }

    @Override
    public void rename() {
        final javax.swing.JTextField text = new javax.swing.JTextField();
        text.setText(query.getEditableName());
        javax.swing.JPanel panel = new javax.swing.JPanel();
        javax.swing.JLabel label = new javax.swing.JLabel();
        DialogDescriptor dd = new DialogDescriptor(panel, NbBundle.getMessage(RtcQueryNode.class, "RenamePanel.name"), true, null);


        label.setText(NbBundle.getMessage(RtcQueryNode.class, "NewName.name"));
        text.setColumns(20);
        text.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                if ((text.getBackground().equals(Color.pink)) && !(text.getText().equals(""))) {
                    text.setBackground(Color.white);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (text.getText().equals("")) {
                    text.setBackground(Color.pink);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        panel.add(label);
        panel.add(text);
        Dialog dialog = DialogDisplayer.getDefault().createDialog(dd);
        dialog.setVisible(true);

        if (dd.getValue().equals(DialogDescriptor.OK_OPTION)) {
            String oldName = query.getEditableName();
            query.setEditableName(text.getText());
            try {
                query.save();
            } catch (RtcSaveException ex) {
                query.setEditableName(oldName);
                RtcLogger.getLogger(RtcQueryNodeFactory.class)
                        .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }

    }
}
