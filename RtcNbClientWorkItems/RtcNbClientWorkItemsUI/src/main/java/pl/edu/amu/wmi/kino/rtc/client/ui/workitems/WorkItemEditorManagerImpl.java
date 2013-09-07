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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.lookup.ServiceProvider;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItem;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.workitems.RtcWorkItemViewTarget;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.WorkItemEditorManager;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenter;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.presenters.WorkItemPresenterManager;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

/**
 *
 * @author Patryk Żywica
 */
@ServiceProvider(service = WorkItemEditorManager.class)
public class WorkItemEditorManagerImpl implements WorkItemEditorManager {

    private final Map<RtcWorkItem, WorkItemPresenter> register = new HashMap<RtcWorkItem, WorkItemPresenter>(4);
    private static final RequestProcessor rp = new RequestProcessor(WorkItemEditorManagerImpl.class);

    @Override
    public void openEditor(final RtcWorkItem wi) {
        WorkItemPresenter p;
        synchronized (register) {
            if (register.containsKey(wi)) {
                p = register.get(wi);
                p.revealDisplay();
            } else {
                rp.post(new CreateRunnable(wi));
            }
        }
    }

    @Override
    public void closeEditor(RtcWorkItem wi) {
        synchronized (register) {
            WorkItemPresenter p = register.get(wi);
            if (p != null) {
                p.closeDisplay();
            }
            register.remove(wi);
            assert p != null : NbBundle.getMessage(WorkItemEditorManagerImpl.class, "unopenedEditor.exp");
        }
    }

    private class CreateRunnable implements Runnable {

        private RtcWorkItem wi;

        CreateRunnable(RtcWorkItem wi) {
            this.wi = wi;
        }

        public void run() {
            ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(WorkItemEditorManagerImpl.class, "OpeningWIEditor.msg"));
            ph.start();
            final WorkItemPresenter p;
            WorkItemPresenterManager pm = wi.getManager().getProjectArea().getLookup().lookup(WorkItemPresenterManager.class);
            p = pm.createPresenter(wi, RtcWorkItemViewTarget.MAIN);
            p.addCloseHandler(new WorkItemPresenter.CloseHandler() {

                @Override
                public void close() {
                    synchronized (register) {
                        register.remove(wi);
                    }
                    p.unbind();
                }
            });
            p.bind();
            synchronized (register) {
                register.put(wi, p);
            }
            p.revealDisplay();
            ph.finish();
        }
    }
}
