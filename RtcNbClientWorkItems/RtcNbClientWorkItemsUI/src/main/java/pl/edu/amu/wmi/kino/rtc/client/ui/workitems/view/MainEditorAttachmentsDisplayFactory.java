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
package pl.edu.amu.wmi.kino.rtc.client.ui.workitems.view;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.RequestProcessor;

import pl.edu.amu.wmi.kino.netbeans.mvp.client.Display;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.DisplayFactory;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.SwingDisplay;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.InputHandler.Input;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler;
import pl.edu.amu.wmi.kino.netbeans.mvp.client.handlers.OptionChooseHandler.OptionChooser;
import pl.edu.amu.wmi.kino.netbeans.mvp.event.HandlerRegistration;
import pl.edu.amu.wmi.kino.rtc.client.RtcLogger;
import pl.edu.amu.wmi.kino.rtc.client.facade.api.common.utils.Pair;
import pl.edu.amu.wmi.kino.rtc.client.ui.api.workitems.view.AttachmentsDisplay;

/**
 * 
 * @author Paweł Doleciński
 * @author Bartosz Zaleski <b4lzak@gmail.com>
 * @author Patryk Żywica
 */
public class MainEditorAttachmentsDisplayFactory implements DisplayFactory {

    @Override
    public <D extends Display> D createDisplay(Class<D> displayType,
            Lookup lookup) {
        if (displayType.equals(AttachmentsDisplay.class)) {
            return displayType.cast(new MainEditorAttachmentsDisplay());
        }
        return null;
    }

    /**
     *
     * @author Paweł Doleciński
     */
    private static class MainEditorAttachmentsDisplay extends JPanel implements
            SwingDisplay, AttachmentsDisplay {

        private static final long serialVersionUID = 86181915565050L;
        DefaultTableModel model;
        private final Map<Input<?>, List<InputHandler<?>>> inputHandlers = Collections.synchronizedMap(new HashMap<Input<?>, List<InputHandler<?>>>());
        private final Map<OptionChooser, List<OptionChooseHandler>> optionChooseHandlers = Collections.synchronizedMap(new HashMap<OptionChooser, List<OptionChooseHandler>>());
        ResourceBundle bundle = NbBundle.getBundle(MainEditorAttachmentsDisplay.class);
        String[] colNames = new String[]{
            bundle.getString("AttachmentColumnId.name"),
            bundle.getString("AttachmentColumnDate.name"),
            bundle.getString("AttachmentColumnCreatorName.name"),
            bundle.getString("AttachmentColumnFilesize.name"),
            bundle.getString("AttachmentColumnFiletype.name"),};

        MainEditorAttachmentsDisplay() {
            super(new BorderLayout());
            initComponents();
            setOpaque(false);

            model = new DefaultTableModel(colNames, 0);
            jTable1.setModel(model);

            jTable1.getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {

                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                            if (lsm.equals(jTable1.getSelectionModel())) {
                                if (lsm.isSelectionEmpty()) {
                                    SaveAsButton.setEnabled(false);
                                    openButton.setEnabled(false);
                                    deleteFileButton.setEnabled(false);
                                    renameFileButton.setEnabled(false);
                                } else {
                                    SaveAsButton.setEnabled(true);
                                    openButton.setEnabled(true);
                                    deleteFileButton.setEnabled(true);
                                    renameFileButton.setEnabled(true);
                                }
                            }
                        }
                    });

        }

        private void initComponents() {
            jScrollPane1 = new JScrollPane();
            jTable1 = new JTable();
            jPanel1 = new JPanel();
            jPanel1.setOpaque(false);
            SaveAsButton = new JButton();
            renameFileButton = new JButton();
            addFileButton = new JButton();
            deleteFileButton = new JButton();
            openButton = new JButton();


            jTable1.setAutoCreateRowSorter(true);
            jTable1.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{}, new String[]{}));
            jTable1.setShowHorizontalLines(false);
            jTable1.setShowVerticalLines(false);
            jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            jScrollPane1.setPreferredSize(new java.awt.Dimension(200, 300));
            jScrollPane1.setViewportView(jTable1);
            
            
            add(jScrollPane1,BorderLayout.CENTER);

            jPanel1.setMaximumSize(new java.awt.Dimension(121, 32767));
            jPanel1.setMinimumSize(new java.awt.Dimension(121, 0));

            SaveAsButton.setText(org.openide.util.NbBundle.getMessage(
                    MainEditorAttachmentsDisplay.class,
                    "saveAsButton.text")); // NOI18N
            SaveAsButton.setEnabled(false);
            SaveAsButton.setPreferredSize(new java.awt.Dimension(100, 24));
            SaveAsButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    saveAsButtonActionPerformed(evt);
                }
            });

            renameFileButton.setText(org.openide.util.NbBundle.getMessage(
                    MainEditorAttachmentsDisplay.class,
                    "renameFileButton.text")); // NOI18N
            renameFileButton.setEnabled(false);
            renameFileButton.setPreferredSize(new java.awt.Dimension(100, 24));
            renameFileButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(
                        java.awt.event.ActionEvent evt) {
                    renameFileButtonActionPerformed(evt);
                }
            });

            addFileButton.setText(org.openide.util.NbBundle.getMessage(
                    MainEditorAttachmentsDisplay.class,
                    "addFileButton.text")); // NOI18N
            addFileButton.setPreferredSize(new java.awt.Dimension(100, 24));
            addFileButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(
                        java.awt.event.ActionEvent evt) {
                    addFileButtonActionPerformed(evt);
                }
            });

            deleteFileButton.setText(org.openide.util.NbBundle.getMessage(
                    MainEditorAttachmentsDisplay.class,
                    "deleteFileButton.text")); // NOI18N
            deleteFileButton.setEnabled(false);
            deleteFileButton.setPreferredSize(new java.awt.Dimension(100, 24));
            deleteFileButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(
                        java.awt.event.ActionEvent evt) {
                    deleteFileButtonActionPerformed(evt);
                }
            });

            openButton.setText(org.openide.util.NbBundle.getMessage(
                    MainEditorAttachmentsDisplay.class,
                    "openButton.text")); // NOI18N
            openButton.setEnabled(false);
            openButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    openButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
                    jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    javax.swing.GroupLayout.Alignment.TRAILING,
                    jPanel1Layout.createSequentialGroup().addContainerGap(
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE).addGroup(
                    jPanel1Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING,
                    false).addComponent(
                    openButton,
                    javax.swing.GroupLayout.Alignment.TRAILING,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE).addComponent(
                    addFileButton,
                    0,
                    0,
                    Short.MAX_VALUE).addComponent(
                    deleteFileButton,
                    0,
                    0,
                    Short.MAX_VALUE).addComponent(
                    SaveAsButton,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    97,
                    Short.MAX_VALUE).addComponent(
                    renameFileButton,
                    0,
                    0,
                    Short.MAX_VALUE)).addContainerGap()));
            jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
                    javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                    jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(openButton).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                    addFileButton,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                    renameFileButton,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                    deleteFileButton,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    24,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(
                    SaveAsButton,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    Short.MAX_VALUE)));

            add(jPanel1,BorderLayout.EAST);
        }

        @Override
        public JComponent asComponent() {
            return this;
        }

        @Override
        public void setAttachments(String[] names, Date[] creationDate,
                String[] createdBy, long[] size, String[] type) {
            model = new DefaultTableModel(colNames, 0);
            jTable1.setModel(model);
            for (int i = 0; i < names.length; i++) {
                model.addRow(new Object[]{names[i], creationDate[i],
                            createdBy[i], size[i], type[i]});
            }
        }

        @Override
        public HandlerRegistration addOptionHandler(OptionChooser source,
                final OptionChooseHandler h) {
            if (AttachmentsDisplay.REMOVE_OPTION.equals(source)
                    || AttachmentsDisplay.OPEN_OPTION.equals(source)) {
                if (!optionChooseHandlers.containsKey(source)) {
                    optionChooseHandlers.put(source,
                            Collections.synchronizedList(new LinkedList<OptionChooseHandler>()));
                }
                final List<OptionChooseHandler> list = optionChooseHandlers.get(source);

                list.add(h);

                return new HandlerRegistration(new Runnable() {

                    @Override
                    public void run() {
                        list.remove(h);
                    }
                });
            }
            return null;
        }

        @Override
        public <T> HandlerRegistration addInputHandler(Input<T> input,
                final InputHandler<T> h) {

            if (AttachmentsDisplay.NEW_FILE_PATH.equals(input)
                    || AttachmentsDisplay.RENAME_INPUT.equals(input)
                    || AttachmentsDisplay.SAVE_INPUT.equals(input)) {
                if (!inputHandlers.containsKey(input)) {
                    inputHandlers.put(input,
                            Collections.synchronizedList(new LinkedList<InputHandler<?>>()));
                }
                final List<InputHandler<?>> list = inputHandlers.get(input);

                list.add(h);
                return new HandlerRegistration(new Runnable() {

                    @Override
                    public void run() {
                        list.remove(h);
                    }
                });
            } else {
                return null;
            }
        }

        private void deleteFileButtonActionPerformed(
                java.awt.event.ActionEvent evt) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1 && selectedRow < jTable1.getRowCount()) {
                int result = JOptionPane.showConfirmDialog(
                        this,
                        NbBundle.getMessage(
                        MainEditorAttachmentsDisplay.class,
                        "DoYouReallyWantToDeleteThatFile.name"),
                        NbBundle.getMessage(
                        MainEditorAttachmentsDisplay.class,
                        "DoYouReallyWantToDeleteThatFileTitle.name"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION) {
                    List<OptionChooseHandler> l = optionChooseHandlers.get(AttachmentsDisplay.REMOVE_OPTION);
                    if (l != null) {
                        for (OptionChooseHandler o : l) {
                            o.optionChosen(selectedRow);
                        }
                    }
                }
            }
        }

        private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
            if (jTable1.getSelectedRow() != -1) {
                List<OptionChooseHandler> l = optionChooseHandlers.get(AttachmentsDisplay.OPEN_OPTION);
                if (l != null) {
                    for (OptionChooseHandler o : l) {
                        o.optionChosen(jTable1.getSelectedRow());
                    }
                }
            }

        }

        @Override
        public void openFile(final File f) {
            RequestProcessor.getDefault().post(new Runnable() {

                @Override
                public void run() {
                    ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(
                            MainEditorAttachmentsDisplay.class,
                            "OpeningFileProgressBar.name"));
                    ph.start();
                    ph.switchToIndeterminate();
                    try {
                        Desktop.getDesktop().open(f);
                    } catch (IOException ex) {
                        RtcLogger.getLogger(MainEditorAttachmentsDisplay.class).severe(ex.getLocalizedMessage());
                    }
                    ph.finish();

                }
            });
        }

        private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {
            if (jTable1.getSelectedRow() != -1) {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setSelectedFile(new File((String) jTable1.getModel().getValueAt(jTable1.getSelectedRow(), 0)));

                int result = fileChooser.showSaveDialog(this);

                if (result == JFileChooser.APPROVE_OPTION) {

                    RequestProcessor.getDefault().post(new Runnable() {

                        @SuppressWarnings("unchecked")
                        @Override
                        public void run() {
                            ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(
                                    MainEditorAttachmentsDisplay.class,
                                    "DownloadingFileProgressBar.name"));
                            ph.start();
                            ph.switchToIndeterminate();

                            File f1 = fileChooser.getSelectedFile();
                            List<InputHandler<?>> list = inputHandlers.get(AttachmentsDisplay.SAVE_INPUT);
                            for (InputHandler<?> i : list) {
                                ((InputHandler<Pair<Integer, String>>) i).valueEntered(new Pair<Integer, String>(
                                        jTable1.getSelectedRow(), f1.getAbsolutePath()));
                            }
                            ph.finish();

                        }
                    });

                }

            }
        }

        private void addFileButtonActionPerformed(java.awt.event.ActionEvent evt) {
            final JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {

                RequestProcessor.getDefault().post(new Runnable() {

                    @SuppressWarnings("unchecked")
                    @Override
                    public void run() {
                        ProgressHandle ph = ProgressHandleFactory.createHandle(NbBundle.getMessage(
                                MainEditorAttachmentsDisplay.class,
                                "UploadingFileProgressBar.name"));
                        ph.start();
                        ph.switchToIndeterminate();
                        File f1 = fileChooser.getSelectedFile();
                        List<InputHandler<?>> list = inputHandlers.get(AttachmentsDisplay.NEW_FILE_PATH);
                        for (InputHandler<?> i : list) {
                            fireEvent((InputHandler<String>) i,
                                    f1.getAbsolutePath());
                        }
                        ph.finish();
                    }

                    private void fireEvent(InputHandler<String> i, String path) {
                        i.valueEntered(path);
                    }
                });
            }
        }

        private void renameFileButtonActionPerformed(
                java.awt.event.ActionEvent evt) {
            int selectedRow = jTable1.getSelectedRow();
            if (selectedRow != -1) {
                // String newName = JOptionPane.showInputDialog(this, "Message",
                // attachmentList.get(selectedRow).getName());
                // attachmentList.get(selectedRow).setName(newName);
                // pe.setValue(attachmentList);
            }
        }
        private javax.swing.JButton SaveAsButton;
        private javax.swing.JButton addFileButton;
        private javax.swing.JButton deleteFileButton;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTable jTable1;
        private javax.swing.JButton openButton;
        private javax.swing.JButton renameFileButton;
    }
}
