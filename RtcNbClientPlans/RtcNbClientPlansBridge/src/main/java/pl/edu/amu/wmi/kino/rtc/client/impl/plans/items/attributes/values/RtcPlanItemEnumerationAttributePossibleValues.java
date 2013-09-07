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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.values;

import com.ibm.team.repository.client.ITeamRepository;
import com.ibm.team.repository.common.TeamRepositoryException;
import com.ibm.team.workitem.common.IWorkItemCommon;
import com.ibm.team.workitem.common.model.IAttribute;
import com.ibm.team.workitem.common.model.IEnumeration;
import com.ibm.team.workitem.common.model.ILiteral;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.types.impl.RtcLiteralImpl;

/**
 * RtcEnumerationPossibleValues retrieves possible enumeration values from server
 * @author Pawel Dolecinski
 */
public class RtcPlanItemEnumerationAttributePossibleValues implements RtcPlanItemAttributePossibleValues<RtcLiteral> {

    private IAttribute attr;
    private List<RtcLiteral> possibleValues;

    /**
     * Constructor get IAttribute class which cannot be null
     * @param attribute 
     */
    public RtcPlanItemEnumerationAttributePossibleValues(IAttribute attribute) {
        assert attribute != null : "RtcEnumerationPossibleValues: argument in constructor cannnot be null";
        this.attr = attribute;
    }

    /**
     * This method retrieves from rtc server possible values of enumeration type attributes
     * You will get list of <code>ILiteral</code> but you must remember that WI gives you from getValue method
     * IIdentifier object. You can get this object from ILiteral but it hasn't got a human readable name.
     * Human readable name you will get from <code>ILiteral.getName()</code> and this should be displayed in editor.
     * 
     * @return list of possible literals
     */
    @Override
    public List<RtcLiteral> getPossibleValues() {

        if (possibleValues != null) {
            return Collections.unmodifiableList(possibleValues);
        }

        List<RtcLiteral> rtcLiterals = new ArrayList<RtcLiteral>();
        try {
            ITeamRepository repo = (ITeamRepository) attr.getOrigin();
            IWorkItemCommon workItemCommon = (IWorkItemCommon) repo.getClientLibrary(IWorkItemCommon.class);
            //this should give you a list of ILiteral but
            IEnumeration<? extends ILiteral> enumerations = workItemCommon.resolveEnumeration(attr, null);
            @SuppressWarnings("unchecked")
            List<ILiteral> items = (List<ILiteral>) enumerations.getEnumerationLiterals();

            for (ILiteral iLiteral : items) {
                rtcLiterals.add(new RtcLiteralImpl(iLiteral));
            }
        } catch (TeamRepositoryException ex) {
            RtcLogger.getLogger(RtcPlanItemEnumerationAttributePossibleValues.class).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        } finally {
            possibleValues = rtcLiterals;
            return Collections.unmodifiableList(possibleValues);
        }
    }
}
