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
package pl.edu.amu.wmi.kino.rtc.client.reports;

import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.ActiveProjectArea;

/**
 * this class is an only backend for accessing reports. every access should be
 * done, by creatng an instance of this class and traversing through it's methods
 * - this will be the case at least until report generation/execution will be
 * supported directly by editors of some sort.
 * @author Adam Kedziora 
 */
public interface RtcReportsRetriever {

    /**
     * This method returns topmost report references factory. In orginal Eclipse
     * client those are : "My Reports", "Shared Reports", "Report Templates".
     * @return topmost references factory, not null.
     */
    public RtcReportReferenceChildren getReports();

    /**
     * This method will be called to get actions for popup menu of the main node.
     * results of this method will not be cached. do not return null, use
     * empty Array instead. Support lazy initialization if possible.
     * @return actions to use, not null.
     */
    public RtcReportActionReference[] getActions();

    public void setProjectArea(ActiveProjectArea projectArea);
    /**
     * this interface represents either a report, reportTemplate or folder within
     * reports. Nodes will be created based on those object.
     */
    public interface RtcReportReference {
        //to get localized message stored in Bundle.properties in the apropriate
        //package use :
        //NbBundle.getMessage(NameOfTheClass.class, "keyInBundleName")
        //for maven build and class xx.xxx.xxxx.NameOfTheClass, budle file will 
        //be located in {project.folder}/src/main/resources/xx/xxx/xxxx/Bundle.properties
        //which means, that bundle files are package bound, not class bound - so for
        //key names use this convention : NameOfTheClass.nameOfTheProperty

        /**
         * this method returns the display name of the reference. it may poses
         * basic html tag formatting (swing subset).
         * @return display name of the reference.
         */
        public String getDisplayName();

        /**
         * this method returns the short description of the reference. it may poses
         * basic html tag formatting (swing subset). short description will be shown
         * as a tooltip - so 128 characters is a good max length of it propably.
         * @return short description of the reference.
         */
        public String getShortDescription();
        //for icon base "xx/xxx/xxxx/icon_name.gif", files will be found in
        //{project.folder}/src/main/resources/xx/xxx/xxxx/
        //use "pl/edu/amu/wmi/kino/rtc/client/reports/icons/" for storing icons

        /**
         * this method returns icons location and naming base, with the extension
         * to use. ommit the leading '/' . if null will be passed default icon \
         * will be used.
         * @return icon base for this reference.
         */
        public String getIconBaseWithExtension();

        /**
         * This method will be called to get actions for popup menu of the node.
         * results of this method will not be cached. do not return null, use
         * empty Array instead. Support lazy initialization if possible.
         * @return actions to use, not null.
         */
        public RtcReportActionReference[] getActions();

        /**
         * This method will be called to get action invoked when doubleclicking
         * the node. (or doing similar action in systems where doubleclick is not an
         * preffered way of invoking actions). return null if you do not want any
         * action to be performed by default (for example folders).
         * Support lazy initialization if possible.
         * @return default action to run on the reference.
         */
        public RtcReportActionReference getDefaultAction();

        /**
         * this method returns ChildrenFactory for the given reference, or null
         * if the reference cannot have children. Do not pass null if the reference
         * have no children nuw, but might have some in the future !
         * @return children factory or null.
         */
        public RtcReportReferenceChildren getChildrenFactory();
    }

    /**
     * this interface is a factory for children of a reference.
     */
    public interface RtcReportReferenceChildren {

        /**
         * this method returns references that represent the children of the
         * parent reference. this method won't be called until needed, and
         * afterwards any time, when ChildrenChanged event will be fired. do not
         * return null, use empty array instead.
         * Support lazy initialization if possible.
         * @return array of children references, not null.
         */
        public RtcReportReference[] getChildren();

        /**
         * attach listener tothis object.
         * @param listener
         */
        public void addListener(RtcReportReferenceChildrenListener listener);

        /**
         * dettach listener from this object.
         * @param listener
         */
        public void removeListener(RtcReportReferenceChildrenListener listener);
    }

    /**
     * This inerface prepresents listener for the children object.
     */
    public interface RtcReportReferenceChildrenListener {

        /**
         * Inform the listener about changes in children.
         * @param eventType
         */
        public void fireEvent(Event eventType);

        public enum Event {

            /**
             * Number of children changed, or some of the children where changed
             * to other.
             */
            ChildrenChanged;
        }
    }

    public interface RtcReportActionReference {

        /**
         * This method returns a display name of the action. it may poses
         * basic html tag formatting (swing subset). short description will be shown
         * as a tooltip - so 128 characters is a good max length of it propably.
         * @return short description of the action.
         */
        public String getDisplayName();

        /**
         * This method returns icons location and naming base, with the extension
         * to use. ommit the leading '/' . if null will be passed - no icon will
         * be used
         * @return icon base for this action.
         */
        public String getIconBaseWithExtension();

        /**
         * This method will be called when action will be performed. this method
         * may be called from AWT thread so run time intensive tasks in separate
         * threads.
         */
        public void performAction();
        //code for opening url :
        //HtmlBrowser.URLDisplayer.getDefault().showURL(URL url);
        //code for progressbar :
        //ProgressHandle progress = ProgressHandleFactory.createHandle(String nameOfThetask);
        //progress.start(100);
        //progress.progress(String nameOfTheSubtask , int progressValue);
        //<do some tasks>
        //progress.progress(String nameOfTheSubtask, int progressValue);
        //<do some tasks>
        //progress.finish();
        //code for running tasks in new thread :
        //RequestProcessor.getDefault().post(Runnable task);
        //you can use named RequestProcessor as well, it will provide mutex functionality:
        //RequestProcessor rp = new RequestProcessor(String name);
        //rp.post(Runnable task);
    }
}
