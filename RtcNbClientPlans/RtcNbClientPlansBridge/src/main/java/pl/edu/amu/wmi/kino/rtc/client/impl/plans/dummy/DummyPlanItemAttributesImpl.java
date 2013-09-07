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
package pl.edu.amu.wmi.kino.rtc.client.impl.plans.dummy;

import java.awt.Image;
import java.util.List;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.RtcPlanItemAttribute;
import pl.edu.amu.wmi.kino.rtc.client.api.plans.items.attributes.RtcPlanItemAttributePossibleValues;
import pl.edu.amu.wmi.kino.rtc.client.impl.plans.items.attributes.checkers.RtcDurationValueCheckerImpl;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcLiteral;
import pl.edu.amu.wmi.kino.rtc.client.workitems.attributes.types.RtcWorkItemType;

/**
 *
 * @author Patryk Żywica
 * @author Michal Wojciechowski
 */
public class DummyPlanItemAttributesImpl {

    public static RtcPlanItemAttribute<RtcLiteral> PRIORITY_ATTRIBUTE =
            new RtcPlanItemAttribute<RtcLiteral>() {

                private InstanceContent ic;
                private Lookup lookup;

                @Override
                public RtcLiteral getNullValue() {
                    return new RtcLiteral() {

                        @Override
                        public Image getIcon() {
                            return null;
                        }

                        @Override
                        public String getName() {
                            return "Unassigned";
                        }

                        @Override
                        public String getId() {
                            return "0";
                        }

                        @Override
                        public String toString() {
                            return getName();
                        }
                    };
                }

                @Override
                public String getAttributeName() {
                    return "Priority";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "priority";
                }

                @Override
                public Class<RtcLiteral> getValueType() {
                    return RtcLiteral.class;
                }

                @Override
                public Lookup getLookup() {
                    if (ic == null) {
                        ic = new InstanceContent();
                        ic.add(this);
                        ic.add(new DummyPriorityEnumerationPossibleValues());
                        lookup = new AbstractLookup(ic);
                    }
                    return lookup;
                }

                @Override
                public boolean isReadOnly() {
                    return false;
                }
            };
    public static RtcPlanItemAttribute<String> DESCRIPTION_ATTRIBUTE =
            new RtcPlanItemAttribute<String>() {

                @Override
                public String getNullValue() {
                    return "";
                }

                @Override
                public String getAttributeName() {
                    return "Description";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "desc";
                }

                @Override
                public Class<String> getValueType() {
                    return String.class;
                }

                @Override
                public Lookup getLookup() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isReadOnly() {
                    return false;
                }
            };
    public static RtcPlanItemAttribute<String> SUMMPARY_ATTRIBUTE =
            new RtcPlanItemAttribute<String>() {

                @Override
                public String getNullValue() {
                    return "";
                }

                @Override
                public String getAttributeName() {
                    return "Summary";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "summary";
                }

                @Override
                public Class<String> getValueType() {
                    return String.class;
                }

                @Override
                public Lookup getLookup() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isReadOnly() {
                    return true;
                }
            };
    public static RtcPlanItemAttribute<Integer> ID_ATTRIBUTE =
            new RtcPlanItemAttribute<Integer>() {

                @Override
                public Integer getNullValue() {
                    return -1;
                }

                @Override
                public String getAttributeName() {
                    return "Identifier";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "id";
                }

                @Override
                public Class<Integer> getValueType() {
                    return Integer.class;
                }

                @Override
                public Lookup getLookup() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isReadOnly() {
                    return true;
                }
            };
    public static RtcPlanItemAttribute<Integer> SOME_ATTRIBUTE =
            new RtcPlanItemAttribute<Integer>() {

                @Override
                public Integer getNullValue() {
                    return 0;
                }

                @Override
                public String getAttributeName() {
                    return "Some attribute";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "some";
                }

                @Override
                public Class<Integer> getValueType() {
                    return Integer.class;
                }

                @Override
                public Lookup getLookup() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public boolean isReadOnly() {
                    return false;
                }
            };
    public static RtcPlanItemAttribute<RtcWorkItemType> TYPE_PROPERTY =
            new RtcPlanItemAttribute<RtcWorkItemType>() {

                private Lookup lookup;

                @Override
                public RtcWorkItemType getNullValue() {
                    return new RtcWorkItemType() {

                        @Override
                        public List<String> getAliases() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String getId() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public String getDisplayName() {
                            return "Zadanie";
                        }

                        @Override
                        public String getCategory() {
                            throw new UnsupportedOperationException("Not supported yet.");
                        }

                        @Override
                        public Image getIcon() {
                            return ImageUtilities.loadImage("pl/edu/amu/wmi/kino/rtc/client/plans/editor/items/dummyPlanItemIcon.png", false);
                        }
                    };
                }

                @Override
                public String getAttributeName() {
                    return "Typ";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "type";
                }

                @Override
                public Class<RtcWorkItemType> getValueType() {
                    return RtcWorkItemType.class;
                }

                @Override
                public Lookup getLookup() {
                    if (lookup == null) {
                        InstanceContent ic = new InstanceContent();
                        RtcPlanItemAttributePossibleValues a = new DummyTypePossibleValues();
                        ic.add(a);
                        lookup = new AbstractLookup(ic);
                    }
                    return lookup;
                }

                @Override
                public boolean isReadOnly() {
                    return false;
                }
            };

    public static RtcPlanItemAttribute<Long> DURATION_PROPERTY =
            new RtcPlanItemAttribute<Long>() {

                private Lookup lookup;

                @Override
                public Long getNullValue() {
                    return 0L;
                }

                @Override
                public String getAttributeName() {
                    return "Duration";
                }

                @Override
                public String getAttributeIdentifier() {
                    return "duration";
                }

                @Override
                public Class<Long> getValueType() {
                    return Long.class;
                }

                @Override
                public Lookup getLookup() {
                    if(lookup == null) {
                        InstanceContent ic = new InstanceContent();
                        ic.add(new RtcDurationValueCheckerImpl());
                        lookup = new AbstractLookup(ic);
                    }

                    return lookup;
                }

                @Override
                public boolean isReadOnly() {
                    return false;
                }
            };

            //
            
    private DummyPlanItemAttributesImpl() {
    }
}
