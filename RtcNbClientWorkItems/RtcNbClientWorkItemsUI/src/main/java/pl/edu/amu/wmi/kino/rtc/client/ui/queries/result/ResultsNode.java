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
package pl.edu.amu.wmi.kino.rtc.client.ui.queries.result;

import java.util.Iterator;
import java.util.List;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Cancellable;
import org.openide.util.Lookup;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQuery;
import pl.edu.amu.wmi.kino.rtc.client.queries.model.query.RtcQueryResult;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.WorkItemNodeManager;

/**
 *
 * @author Patryk Żywica
 */
public class ResultsNode extends AbstractNode {

    public ResultsNode(RtcQuery query) {
        super(Children.create(new ResultsChildren(query), true));
    }

    private static class ResultsChildren extends ChildFactory<RtcQueryResult> {

        private static final int GROUP_SIZE = 3;
        private RtcQuery query;
        private WorkItemNodeManager winm;
        private Iterator<RtcQueryResult> it;
        private ProgressHandle ph;
        private boolean cancelled = false;
        private int counter = 0;
        private boolean finished = false;

        ResultsChildren(RtcQuery query) {
            winm = Lookup.getDefault().lookup(WorkItemNodeManager.class);
            this.query = query;
        }

        @Override
        protected boolean createKeys(List<RtcQueryResult> toPopulate) {

            if (it == null) {
                ph = ProgressHandleFactory.createHandle("QueryResults:" + query.getName(), new Cancellable() {

                    @Override
                    public boolean cancel() {
                        cancelled = true;
                        return true;
                    }
                });
                ph.start();
                ph.progress("Fetching query results");
                it = query.getQueryResults().iterator();
                if(it.hasNext()){
                    ph.progress("Displaying results");
                }else{
                    ph.finish();
                    finished=true;
                    return true;
                }
            }
            int i = 0;
            while (i < GROUP_SIZE && it.hasNext()) {
                toPopulate.add(it.next());
                i++;
            }
            counter += i;
            if (it.hasNext() && !cancelled) {
                return false;
            } else {
                finished = true;
                return true;
            }
        }

        @Override
        protected Node createNodeForKey(RtcQueryResult key) {
            if (!cancelled) {
                Node n = winm.createNode(key.getWorkItem());
                counter--;
                if (counter == 0 && finished == true) {
                    ph.finish();
                }
                return n;
            } else {
                return null;
            }
        }
    }
}
