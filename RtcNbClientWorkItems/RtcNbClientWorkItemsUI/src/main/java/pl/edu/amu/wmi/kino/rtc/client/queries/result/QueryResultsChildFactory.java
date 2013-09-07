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
package pl.edu.amu.wmi.kino.rtc.client.queries.result;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryResult;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.WorkItemNodeManager;

/**
 * This class is basically a Factory. It is responsible for creating children that
 * will be shown. It even cares about creating <code>LoadingNode</code> while waiting
 * for expected results. Class also implements <code>Runnable</code> interface
 * to run it in separate thread. This is used to prevent the application from
 * suspeding while waiting for results.
 *
 *
 * @author Szymon Sadlo
 */
@Deprecated
public class QueryResultsChildFactory extends ChildFactory<Node> implements Runnable {

    private RtcQuery query;
    private WorkItemNodeManager nodeManager;
    private boolean started = false;
    private List<Node> toAddList;

    public QueryResultsChildFactory(RtcQuery query) {
        this.query = query;
        nodeManager = Lookup.getDefault().lookup(WorkItemNodeManager.class);
    }

    @Override
    protected boolean createKeys(List<Node> list) {
        if (started == false) {
            started = true;
            toAddList = new ArrayList<Node>();
            RequestProcessor.getDefault().post(this);
        } else {
            list.addAll(toAddList);
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Node item) {
        return item;
    }

    @Override
    public void run() {
        assert (started);
        assert (!EventQueue.isDispatchThread());
        ProgressHandle ph;
        ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ResultsTopComponent.class, "FetchResults.msg"), new Cancellable() {
            @Override
            public boolean cancel() {
                return true;
            }
        });
        ph.start();
        ph.switchToIndeterminate();
        List<RtcQueryResult> resultList = query.getQueryResults();
        ph.finish();
        ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ResultsTopComponent.class, "CreatingResultNodes.msg"), new Cancellable() {
            @Override
            public boolean cancel() {
                return true;
            }
        });
        ph.start();
        ph.switchToIndeterminate();
        int count = 0;
        for (RtcQueryResult result : resultList) {
//            //System.out.println(System.currentTimeMillis()%1000+"+");
            Node n = nodeManager.createNode(result.getWorkItem());
            n.getPropertySets();
            toAddList.add(n);
            count++;
//            //System.out.println(System.currentTimeMillis()%1000+"++");
            if (count % 5 == 0) {
                refresh(true);
            }
//            //System.out.println(System.currentTimeMillis()%1000+"+++");
        }
        refresh(true);
        ph.finish();
        started = false;
    }
}
//This is another implementation of this factory
/*
public class QueryResultsChildFactory extends ChildFactory<Node> implements Runnable {

private RtcQuery query;
private RtcWorkItemNodeFactory wif;
private boolean started = false, finished = true;
private List<Node> toAddList;

public QueryResultsChildFactory(RtcQuery query) {
this.query = query;
this.wif = Lookup.getDefault().lookup(RtcWorkItemNodeFactory.class);
}

@Override
protected boolean createKeys(List<Node> list) {
if (started == false && finished == true) {
started = true;
finished = false;
toAddList = Collections.synchronizedList(new ArrayList<Node>());
RequestProcessor.getDefault().post(this);
} else {
if (started == false) {
finished = true;
}
synchronized (toAddList) {
list.addAll(toAddList);
toAddList.clear();
}
}
return finished;
}

@Override
protected Node createNodeForKey(Node item) {
return item;
}

@Override
public void run() {
assert (started);
assert (!EventQueue.isDispatchThread());
ProgressHandle ph;
ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ResultsTopComponent.class, "FetchResults.msg"));
ph.start();
List<RtcQueryResult> resultList = query.getQueryResults();
ph.finish();
ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(ResultsTopComponent.class, "CreatingResultNodes.msg"));
ph.start();
int count = 0;
for (RtcQueryResult result : resultList) {
//            //System.out.println(System.currentTimeMillis()%1000+"+");
Node n = wif.createNodeForWorkItem(result.getWorkItem(), result.getActiveProjectArea());
n.getPropertySets();
synchronized (toAddList) {
toAddList.add(n);
}
count++;
//            //System.out.println(System.currentTimeMillis()%1000+"++");
//            if (count % 5 == 0) {
//                refresh(true);
//            }
//            //System.out.println(System.currentTimeMillis()%1000+"+++");
}
ph.finish();
started = false;
}
}
 */
