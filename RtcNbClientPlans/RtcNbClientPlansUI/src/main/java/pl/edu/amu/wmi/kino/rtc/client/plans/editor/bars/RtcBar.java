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
package pl.edu.amu.wmi.kino.rtc.client.plans.editor.bars;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Michal Wojciechowski
 */
public class RtcBar extends JComponent {

    private static final long serialVersionUID = 125465322L;
    protected double estimatePercent;
    protected double completedItemsPercent;
    protected double greenBar = 0, redBar = 0, lightBar = 0;
    protected JLabel progressLabel = new JLabel();
    protected JLabel estimateLabel = new JLabel();
    protected Layout layout = Layout.VERITICAL;

    public enum Layout {

        HORIZONTAL, VERITICAL
    };

    protected void initialize() {
        setLayout(new BorderLayout(5, 0));

        setVisible(true);
        setOpaque(true);
        setMinimumSize(new Dimension(100, 16));
        setMaximumSize(new Dimension(1000, 32));

        ProgressPanel progressPanel = new ProgressPanel();
        JPanel infoPanel = new JPanel(new BorderLayout());

        infoPanel.setMinimumSize(new Dimension(100, 16));
        infoPanel.setSize(new Dimension(100, 16));

        if (layout.equals(Layout.HORIZONTAL)) {

            add(progressPanel, BorderLayout.CENTER);
            add(progressLabel, BorderLayout.LINE_START);
            add(estimateLabel, BorderLayout.LINE_END);
            setSize(new Dimension(300, 16));

        } else if (layout.equals(Layout.VERITICAL)) {

            infoPanel.add(progressLabel, BorderLayout.LINE_START);
            infoPanel.add(estimateLabel, BorderLayout.LINE_END);

            add(progressPanel, BorderLayout.CENTER);
            add(infoPanel, BorderLayout.PAGE_END);
            setSize(new Dimension(300, 32));
        }
    }

    private class ProgressPanel extends JPanel {

		private static final long serialVersionUID = -6925809852020348127L;

		public ProgressPanel() {
            setLayout(new BorderLayout());

            setOpaque(false);
            setMinimumSize(new Dimension(100, 16));
            setMaximumSize(new Dimension(1000, 16));
            setSize(100, 16);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            Dimension size = getSize();
            int progressBarPanelHeight = 15;
            int progressBarHeight = progressBarPanelHeight - 4;
            int possibleWidth = size.width - 4;
            int top = (size.height - progressBarPanelHeight) / 2;
            int progressBarTop = top + 2;
            int progressBarLeft = 2;
            int progressBarEstimatedHeight = (int) (progressBarHeight * (1 - estimatePercent));

            g.setColor(Color.decode("#A0A0A0"));
            g.drawRect(0, top, size.width - 1, progressBarPanelHeight);
            g.setColor(Color.decode("#FFFFFF"));
            g.fillRect(1, top + 1, size.width - 3, progressBarPanelHeight - 2);
            g.drawRect(1, top + 1, size.width - 3, progressBarPanelHeight - 2);

            int greenWidth = (int) (greenBar * possibleWidth);
            int redWidth = (int) (redBar * possibleWidth);
            int lightWidth = (int) (lightBar * possibleWidth);

            if (greenWidth > 0) {
                GradientPaint gradient1 = new GradientPaint(0, 0, Color.decode("#E5E5E5"), 0, progressBarHeight, Color.decode("#D3D3D3"), false);
                Rectangle2D.Double r1 = new Rectangle2D.Double(progressBarLeft, progressBarTop, greenWidth - 2, progressBarEstimatedHeight);
                g2.setPaint(gradient1);
                g2.fill(r1);
                g2.draw(r1);

                GradientPaint gradient2 = new GradientPaint(0, 0, Color.decode("#98C398"), 0, progressBarHeight - progressBarEstimatedHeight, Color.decode("#89B489"), false);
                Rectangle2D.Double r2 = new Rectangle2D.Double(progressBarLeft, progressBarTop + progressBarEstimatedHeight, greenWidth - 2, progressBarHeight - progressBarEstimatedHeight);
                g2.setPaint(gradient2);
                g2.fill(r2);
                g2.draw(r2);
            }

            if (redWidth > 0) {
                GradientPaint gradient1 = new GradientPaint(0, 0, Color.decode("#E5E5E5"), 0, progressBarHeight, Color.decode("#D3D3D3"), false);
                Rectangle2D.Double r1 = new Rectangle2D.Double(progressBarLeft + greenWidth, progressBarTop, redWidth, progressBarEstimatedHeight);
                g2.setPaint(gradient1);
                g2.fill(r1);
                g2.draw(r1);

                GradientPaint gradient2 = new GradientPaint(0, 0, Color.decode("#ED9898"), 0, progressBarHeight, Color.decode("#DE8989"), false);
                Rectangle2D.Double r2 = new Rectangle2D.Double(progressBarLeft + greenWidth, progressBarTop + progressBarEstimatedHeight, redWidth, progressBarHeight - progressBarEstimatedHeight);
                g2.setPaint(gradient2);
                g2.fill(r2);
                g2.draw(r2);
            }

            if (lightWidth > 0) {
                GradientPaint gradient1 = new GradientPaint(0, 0, Color.decode("#E5E5E5"), 0, progressBarHeight, Color.decode("#D3D3D3"), false);
                Rectangle2D.Double r1 = new Rectangle2D.Double(progressBarLeft + greenWidth, progressBarTop, lightWidth, progressBarEstimatedHeight);
                g2.setPaint(gradient1);
                g2.fill(r1);
                g2.draw(r1);

                GradientPaint gradient2 = new GradientPaint(0, 0, Color.decode("#98EC98"), 0, progressBarHeight, Color.decode("#8ADE8A"), false);
                Rectangle2D.Double r2 = new Rectangle2D.Double(progressBarLeft + greenWidth + redWidth, progressBarTop + progressBarEstimatedHeight, lightWidth, progressBarHeight - progressBarEstimatedHeight);
                g2.setPaint(gradient2);
                g2.fill(r2);
                g2.draw(r2);
            }

            super.paint(g);
        }
    }

    protected double toHours(double unit) {
        double fraction = unit - (int) unit;
        if (fraction <= 0.125) {
            fraction = 0;
        } else if (fraction > 0.125 && fraction <= 0.25) {
            fraction = 0.25;
        } else if (fraction > 0.25 && fraction <= 0.5) {
            fraction = 0.5;
        } else if (fraction > 0.5 && fraction <= 0.75) {
            fraction = 0.75;
        } else if (fraction > 0.75) {
            fraction = 1;
        }
        return (int) unit + fraction;
    }
}
