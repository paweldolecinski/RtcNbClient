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
package pl.edu.amu.wmi.kino.rtc.client.queries.editor;
import org.openide.util.NbBundle;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.cookies.SaveCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import org.openide.util.Task;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.CloneableTopComponent;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.EventListener;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueriesSet;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.editable.RtcEditableQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.expression.editable.RtcEditableStatement;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.exceptions.RtcSaveException;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery.RtcQueryEvent;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcStatementCreationException;
import pl.edu.amu.wmi.kino.rtc.client.queries.querylist.nodes.RtcQueryNodeFactory;

/**
 *
 * @author Patryk Żywica
 */
public class QueryEditorTopComponent extends CloneableTopComponent implements EventListener<RtcQueryEvent> {

    private static final String PREFERRED_ID_PREFIX = "QueryEditorTopComponent-";
    private static final long serialVersionUID = 515611L;
    private String PREFERRED_ID;
    private SaveCookie saveCookie;
    private RtcQuery query;
    private RtcQueriesSet set;
    private RtcEditableQuery eQuery;
    private Scene scene;
    private InstanceContent content = new InstanceContent();
    private static EditorManager editorManager = new EditorManager();
    private Widget statementWidget;
    private RtcEditableStatement actualStatement;

    public QueryEditorTopComponent(RtcQuery query, RtcQueriesSet set) {
        super();
        this.query = query;
        this.set = set;
        if (query instanceof RtcEditableQuery) {
            this.eQuery = (RtcEditableQuery) query;
        }
        PREFERRED_ID = PREFERRED_ID_PREFIX + query.getQueryIdentifier();
        query.addListener(this);
        associateLookup(new AbstractLookup(content));

        InstanceContent ic = new InstanceContent();
        ic.add(query);
        RtcQueryNodeFactory factory =
                Lookup.getDefault().lookup(RtcQueryNodeFactory.class);
        setActivatedNodes(new Node[]{factory.createNode(set, query)});

        setDisplayName(query.getEditableName());
        scene = new Scene();
        //TESTING BORDER 
//        scene.setBorder(BorderFactory.createLineBorder(9, Color.WHITE));

        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);

        JPanel conditionsPanel = new JPanel();
        RtcHeaderPanel header = new RtcHeaderPanel(query);
        conditionsPanel.setLayout(new BorderLayout());
        this.add(header, BorderLayout.NORTH);
        conditionsPanel.add(new JScrollPane(scene.createView(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);

        //TODO : for future : add details and layout tabs
        tabbedPane.addTab(NbBundle.getMessage(QueryEditorTopComponent.class, "ConditionsTab.name"), conditionsPanel);
       // 
//        JPanel detailsPanel = new RtcQueryDetailsPanel(query);
//        //TODO : bikol : I18N\
//        tabbedPane.addTab("Details", detailsPanel);

        //TODO : bikol : I18N
//        tabbedPane.addTab("Result layout", new ResultLayoutPanel(query));

        add(tabbedPane);

        RequestProcessor.getDefault().post(new StatementChanger());

        //This good because this method is doing nothing if query is unmodified.
        queryModified();
        setFocusable(true);
    }

    @Override
    public int getPersistenceType() {
        return PERSISTENCE_NEVER;
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public static QueryEditorTopComponent findInstanceFor(RtcQuery query, RtcQueriesSet set) {
        return editorManager.findEditor(query, set);
    }

    @Override
    public boolean canClose() {
        if (eQuery != null && eQuery.isModified()) {
            Confirmation message = new NotifyDescriptor.Confirmation(
                    NbBundle.getMessage(QueryEditorTopComponent.class, "QueryEditor.unsaved.changed.msg", query.getEditableName()),
                    NotifyDescriptor.YES_NO_CANCEL_OPTION,
                    NotifyDescriptor.QUESTION_MESSAGE);

            Object result = DialogDisplayer.getDefault().notify(message);

            if (NotifyDescriptor.YES_OPTION.equals(result)) {
                BlockingSaveRunnable br = new BlockingSaveRunnable(eQuery);
                Task task = RequestProcessor.getDefault().create(br);
                task.run();
                task.waitFinished();
                if (br.isTrue()) {
                    return true;
                } else {
                    if (br.getException() != null) {
                        RtcLogger.getLogger().log(Level.WARNING, br.getException().getLocalizedMessage());
                    }
                    return false;
                }
            } else {
                if (NotifyDescriptor.NO_OPTION.equals(result)) {
                    RequestProcessor.getDefault().post(new Runnable() {

                        @Override
                        public void run() {
                            eQuery.discardChanges();
                        }
                    });
                    return true;
                }
            }
            return false;
        } else {
            if (eQuery == null) {
                RequestProcessor.getDefault().post(new Runnable() {

                    @Override
                    public void run() {
                        query.discardChanges();
                    }
                });
            }
            return true;
        }
    }

    @Override
    protected void componentClosed() {
        query.removeListener(this);
        editorManager.closeEditor(query, set);
    }

    @Override
    public void eventFired(RtcQueryEvent event) {
        //this method does nothing if query is unmodified
        queryModified();
        switch (event) {
            case STATEMENT_CHANGED:
                break;
            case QUERY_SAVED:
                querySaved();
                break;
            case NAME_CHANGED:
                break;
            case EDITABLE_NAME_CHANGED:
                break;

        }
    }

    /**
     * This method should be called only after setting new actualStatemtent.
     */
    private void replaceStatement() {
        assert (EventQueue.isDispatchThread());
        if (statementWidget != null) {
            scene.removeChild(statementWidget);
            statementWidget = null;
        }
        ExpressionWidgetFactory factory = Lookup.getDefault().lookup(ExpressionWidgetFactory.class);
        Widget[] e = factory.createExpressionWidget(actualStatement.getRootTerm(), query, scene);
        assert (e.length == 1);
        statementWidget = e[0];
        scene.addChild(statementWidget);
        statementWidget.revalidate();
        scene.validate();
    }

    private void querySaved() {
        content.remove(saveCookie);
        saveCookie = null;
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                setHtmlDisplayName("<html>" + query.getEditableName() + "</html>");
            }
        });
    }

    private void queryModified() {
        if (eQuery != null && eQuery.isModified() && saveCookie == null) {
            saveCookie = new SaveCookieImpl();
            content.add(saveCookie);
        }
        if (eQuery != null && eQuery.isModified()) {
            EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    setHtmlDisplayName("<html><b>" + (query.getEditableName().equals("") ? "*" : query.getEditableName()) + "</b></html>");
                }
            });
        }
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(QueryEditorTopComponent.class);
    }

    private class SaveCookieImpl implements SaveCookie, Runnable {

        @Override
        public void save() throws IOException {
            RequestProcessor.getDefault().post(this);
        }

        @Override
        public void run() {
            if (eQuery == null) {
                throw new IllegalStateException("Save cookie for non editable Query"); //poprawione
                //
            }
            try {
                eQuery.save();
            } catch (RtcSaveException ex) {
                RtcLogger.getLogger(QueryEditorTopComponent.class)
                        .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }

    private class StatementChanger implements Runnable {

        private ProgressHandle ph;

        @Override
        public void run() {
            if (!EventQueue.isDispatchThread()) {
                ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(QueryEditorTopComponent.class, "creating.model.msg"));
                ph.start();
                try {
                    actualStatement = QueryEditorTopComponent.this.query.getStatement();
                    EventQueue.invokeLater(this);
                } catch (RtcStatementCreationException ex) {
                    RtcLogger.getLogger(QueryEditorTopComponent.class)
                            .log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    ph.finish();
                }
            } else {
                replaceStatement();
                ph.finish();
            }
        }
    }

    private class BlockingSaveRunnable implements Runnable {

        private boolean isTrue = false;
        private RtcSaveException ex;
        private RtcEditableQuery query;

        public BlockingSaveRunnable(RtcEditableQuery query) {
            this.query = query;
        }

        @Override
        public void run() {
            try {
                query.save();
                isTrue = true;
            } catch (RtcSaveException ex1) {
                this.ex = ex1;
                isTrue = false;
            }
        }

        public RtcSaveException getException() {
            return ex;
        }

        public boolean isTrue() {
            return isTrue;
        }
    }
}
