/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import com.comcast.cats.SnmpManager;
import com.comcast.cats.info.SetValueTypes;
import com.comcast.cats.info.SnmpServiceConstants;

/**
 * The JPanel for Snmp operations.
 * @author TATA
 */
public class SnmpForm extends JPanel
{
    /**
	 * Generated Serial version ID.
	 */
	private static final long serialVersionUID = 6916730305457224422L;
	// Version strings set in the ComboBox.
    static final String V1                 = "V1";
    static final String V2                 = "V2";
    static final String V3                 = "V3";

    // The action names for get, set and clear actions defined in SnmpFormController class.
    static final String GET_ACTION_NAME    = "getAction";
    static final String SET_ACTION_NAME    = "setAction";
    static final String CLEAR_ACTION_NAME  = "clearAction";

    // The buttons for 'Get', 'Set' and 'Clear' operations.
    JButton             getButton;
    JButton             setButton;
    JButton             clearButton;

    // The Documents for the TextFields, for adding the DocumentListener.
    Document            setValueDoc;
    Document            targetIpDoc;
    Document            oidDoc;
    Document            userNameDoc;
    Document            authPassWordDoc;
    Document            privacyPassWordDoc;

    // Layout used for showing V1, V2 or V3 specific fields.
    JPanel              optionContainer;
    CardLayout          cardLayout;

    // The textFields for entering inputs.
    private JTextField          targetIpTextFld;
    private JTextField          oidTextFld;
    private JTextField          portTextFld;
    private JTextField          userNameTextFld;
    private JTextField          setValueTextFld;
    private JTextField          wsdlLocation;
    private JTextArea           resultTextFld;
    private JPasswordField      communityTextFldV1;
    private JPasswordField      communityTextFldV2;
    private JPasswordField      authPassTextFld;
    private JPasswordField      privPassTextFld;

    // ComboBoxes for selecting the Snmp version and set value type.
    private JComboBox           versionCombo;
    private JComboBox           setValueTypeCombo;

    /**
     * The default constructor which makes and add all the required fields in to
     * the JPanel.
     */
    public SnmpForm()
    {
        // Creating the SnmpFormController instance.
        final SnmpFormController controller = new SnmpFormController(this);

        final GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.fill = GridBagConstraints.NONE;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.gridwidth = 1;
        labelConstraints.weightx  = 0.2;
        labelConstraints.weighty  = 0.0;
        labelConstraints.insets = new Insets(1, 1, 1, 1);

        final GridBagConstraints textFieldConstraints = new GridBagConstraints();
        textFieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        textFieldConstraints.gridwidth = GridBagConstraints.REMAINDER;
        textFieldConstraints.weightx  = 1.0;
        textFieldConstraints.weighty  = 0.0;
        textFieldConstraints.insets = new Insets(1, 1, 1, 1);

        final int inset = 20;
        final Dimension formPrefSize = new Dimension(462, 466);

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(inset, inset, inset, inset));
        setPreferredSize(formPrefSize);

        /*
         *  The panel for version selection.
         */
        final JPanel versionSelectPanel = new JPanel();
        final TitledBorder title = BorderFactory.createTitledBorder("Select Version");
        title.setTitleJustification(TitledBorder.CENTER);
        versionSelectPanel.setBorder(BorderFactory.createTitledBorder(title));
        final String[] versionStrings = {V1, V2, V3};
        versionCombo = new JComboBox(versionStrings);
        final int prefWidth = 50;
        final int prefHeight = 20;
        versionCombo.setPreferredSize(new Dimension(prefWidth, prefHeight));
        versionCombo.addItemListener(controller);
        versionSelectPanel.add(versionCombo);
        //**** end *******//

        /*
         * Panel holding input forms for details common to v1, v2 and v3
         */
        final JPanel commonFormPanel = new JPanel();
        commonFormPanel.setLayout(new GridBagLayout());
        final TitledBorder commonFormBorder = BorderFactory.createTitledBorder("Input Parameters");
        commonFormBorder.setTitleJustification(TitledBorder.CENTER);
        commonFormPanel.setBorder(BorderFactory.createTitledBorder(commonFormBorder));

        final JLabel urlLabel = new JLabel("WSDL Location:", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        commonFormPanel.add(urlLabel, labelConstraints);
        wsdlLocation = new JTextField();
        wsdlLocation.setText(SnmpServiceConstants.SNMP_SERVICE_WSDL_LOCATION);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        commonFormPanel.add(wsdlLocation, textFieldConstraints);

        final JLabel ipLabel = new JLabel("Target IP Address:");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 1;
        commonFormPanel.add(ipLabel, labelConstraints);
        targetIpTextFld = new JTextField();
        targetIpDoc = targetIpTextFld.getDocument();
        targetIpDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 1;
        commonFormPanel.add(targetIpTextFld, textFieldConstraints);

        final JLabel oidLabel = new JLabel("OID:");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 2;
        commonFormPanel.add(oidLabel, labelConstraints);
        oidTextFld = new JTextField();
        oidDoc = oidTextFld.getDocument();
        oidDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 2;
        commonFormPanel.add(oidTextFld, textFieldConstraints);

        final JLabel portLabel = new JLabel("Port Number:");
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 3;
        commonFormPanel.add(portLabel, labelConstraints);
        portTextFld = new JTextField(Integer.toString(SnmpManager.DEFAULT_PORT_NUMBER));
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 3;
        commonFormPanel.add(portTextFld, textFieldConstraints);

        final JLabel setLabel = new JLabel("SET Value:", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 4;
        commonFormPanel.add(setLabel, labelConstraints);
        setValueTextFld = new JTextField();
        setValueDoc = setValueTextFld.getDocument();
        setValueDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 4;
        commonFormPanel.add(setValueTextFld, textFieldConstraints);

        final JLabel setTypeLabel = new JLabel("SET Value Type:", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 5;
        commonFormPanel.add(setTypeLabel, labelConstraints);
        setValueTypeCombo = new JComboBox(SetValueTypes.values());
        setValueTypeCombo.setSelectedItem(SetValueTypes.STRING);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 5;
        commonFormPanel.add(setValueTypeCombo, textFieldConstraints);
        //**** end *******//

        /*
         * Panel holding input forms for specific information of V1, V2 and V3
         * Using card layout for simplified optioning
         */
        optionContainer = new JPanel();
        cardLayout =  new CardLayout();
        optionContainer.setLayout(cardLayout);
        final TitledBorder optionBorder = BorderFactory.createTitledBorder("Authorization");
        optionBorder.setTitleJustification(TitledBorder.LEADING);
        optionContainer.setBorder(BorderFactory.createTitledBorder(optionBorder));

        final JPanel v1Card = new JPanel();
        v1Card.setLayout(new GridBagLayout());
        final JLabel communityLabelv1 = new JLabel("Community", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        v1Card.add(communityLabelv1, labelConstraints);
        communityTextFldV1 = new JPasswordField(SnmpManager.DEFAULT_COMMUNITY_NAME);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        v1Card.add(communityTextFldV1, textFieldConstraints);

        final JPanel v2Card = new JPanel();
        v2Card.setLayout(new GridBagLayout());
        final JLabel communityLabelv2 = new JLabel("Community", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        v2Card.add(communityLabelv2, labelConstraints);
        communityTextFldV2 = new JPasswordField(SnmpManager.DEFAULT_COMMUNITY_NAME);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        v2Card.add(communityTextFldV2, textFieldConstraints);

        final JPanel v3Card = new JPanel();
        v3Card.setLayout(new GridBagLayout());

        final JLabel userNameLabel = new JLabel("User Name", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        v3Card.add(userNameLabel, labelConstraints);
        userNameTextFld = new JTextField();
        userNameDoc = userNameTextFld.getDocument();
        userNameDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 0;
        v3Card.add(userNameTextFld, textFieldConstraints);

        final JLabel authPassWordLabel = new JLabel("Auth PassWord", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 1;
        v3Card.add(authPassWordLabel, labelConstraints);
        authPassTextFld = new JPasswordField();
        authPassWordDoc = authPassTextFld.getDocument();
        authPassWordDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 1;
        v3Card.add(authPassTextFld, textFieldConstraints);

        final JLabel privacyPassWordLabel = new JLabel("Privacy PassWord", SwingConstants.LEFT);
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 2;
        v3Card.add(privacyPassWordLabel, labelConstraints);
        privPassTextFld = new JPasswordField();
        privacyPassWordDoc = privPassTextFld.getDocument();
        privacyPassWordDoc.addDocumentListener(controller);
        textFieldConstraints.gridx = 1;
        textFieldConstraints.gridy = 2;
        v3Card.add(privPassTextFld, textFieldConstraints);

        optionContainer.add(v1Card , V1);
        optionContainer.add(v2Card , V2);
        optionContainer.add(v3Card , V3);
        //**** end *******//

        /*
         * Panel holding Buttons
         */
        int rows = 1;
        int cols = 3;
        int hgap = 5;
        int vgap = 5;
        final Dimension buttonPanelPrefSize = new Dimension(150, 55);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(rows, cols, hgap, vgap));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Action"));
        buttonPanel.setPreferredSize(buttonPanelPrefSize);

        getButton = new JButton();
        getButton.setAction(controller.getActionMap().get(GET_ACTION_NAME));
        getButton.setEnabled(false);
        getButton.setText("Get");

        setButton = new JButton();
        setButton.setAction(controller.getActionMap().get(SET_ACTION_NAME));
        setButton.setEnabled(false);
        setButton.setText("Set");

        clearButton = new JButton();
        clearButton.setAction(controller.getActionMap().get(CLEAR_ACTION_NAME));
        clearButton.setText("Clear");

        buttonPanel.add(getButton);
        buttonPanel.add(setButton);
        buttonPanel.add(clearButton);
        //**** end *******//

        /*
         * Shows the SNMP result from the SNMP Devices
         */
        rows = 1;
        cols = 2;
        hgap = 5;
        vgap = 5;
        final JPanel resultPanel = new JPanel(new GridLayout(rows, cols, hgap, vgap));
        resultTextFld = new JTextArea();
        resultTextFld.setEditable(false);
        resultPanel.add(resultTextFld);
        final JScrollPane scrollPane = new JScrollPane(resultTextFld,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        //**** end *******//

        final Dimension versionPanelPrefSize = new Dimension(115, 115);
        final Dimension panelMaximumSize = new Dimension(475, 100);

        final GridBagConstraints formPanelConstraints = new GridBagConstraints();
        formPanelConstraints.fill = GridBagConstraints.VERTICAL;
        formPanelConstraints.gridx = 0;
        formPanelConstraints.gridy = 0;
        formPanelConstraints.weightx = 0.0;
        versionSelectPanel.setPreferredSize(versionPanelPrefSize);
        add(versionSelectPanel, formPanelConstraints);
        formPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formPanelConstraints.weightx = 1;
        formPanelConstraints.gridx = 1;
        formPanelConstraints.gridy = 0;
        add(commonFormPanel, formPanelConstraints);
        formPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formPanelConstraints.weightx = 0.0;
        formPanelConstraints.gridwidth = 3;
        formPanelConstraints.gridx = 0;
        formPanelConstraints.gridy = 1;
        optionContainer.setMaximumSize(panelMaximumSize);
        add(optionContainer, formPanelConstraints);
        formPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        formPanelConstraints.weighty = 0.1;
        formPanelConstraints.gridwidth = 3;
        formPanelConstraints.gridx = 0;
        formPanelConstraints.gridy = 2;
        buttonPanel.setMaximumSize(panelMaximumSize);
        add(buttonPanel, formPanelConstraints);
        formPanelConstraints.fill = GridBagConstraints.BOTH;
        formPanelConstraints.weighty = 1.0;
        formPanelConstraints.gridx = 0;
        formPanelConstraints.gridwidth = 2;
        formPanelConstraints.gridy = 3;
        add(resultPanel, formPanelConstraints);
    }

    /**
     * The getter method for Target IP TextField.
     * @return TargetIP TextField
     */
    public JTextField getTargetIpTextFld()
    {
        return targetIpTextFld;
    }

    /**
     * The getter method for OID TextField.
     * @return OID TextField
     */
    public JTextField getOidTextFld()
    {
        return oidTextFld;
    }

    /**
     * The getter method for port number TextField.
     * @return PortNumber TextField
     */
    public JTextField getPortTextFld()
    {
        return portTextFld;
    }

    /**
     * The getter method for UserName TextField.
     * @return UserName TextField
     */
    public JTextField getUserNameTextFld()
    {
        return userNameTextFld;
    }

    /**
     * The getter method for set value TextField.
     * @return Set value TextField
     */
    public JTextField getSetValueTextFld()
    {
        return setValueTextFld;
    }

    /**
     * The getter method for WSDL location TextField.
     * @return WSDL Location TextField
     */
    public JTextField getWSDLLocationTextFld()
    {
        return wsdlLocation;
    }

    /**
     * The getter method for Snmp get/set result TextField.
     * @return Snmp get/set result TextField
     */
    public JTextArea getResultTextFld()
    {
        return resultTextFld;
    }

    /**
     * The getter method for V1 CommunityName TextField.
     * @return V1 CommunityName TextField
     */
    public JPasswordField getCommunityTextFldV1()
    {
        return communityTextFldV1;
    }

    /**
     * The getter method for V2 CommunityName TextField.
     * @return V2 CommunityName TextField
     */
    public JPasswordField getCommunityTextFldV2()
    {
        return communityTextFldV2;
    }

    /**
     * The getter method for Authentication Password TextField.
     * @return Authentication Password TextField
     */
    public JPasswordField getAuthPassTextFld()
    {
        return authPassTextFld;
    }

    /**
     * The getter method for Privacy Password TextField.
     * @return Privacy Password TextField
     */
    public JPasswordField getPrivPassTextFld()
    {
        return privPassTextFld;
    }

    /**
     * The getter method for Snmp Version ComboBox.
     * @return Snmp Version ComboBox
     */
    public JComboBox getVersionCombo()
    {
        return versionCombo;
    }

    /**
     * The getter method for Set value type ComboBox.
     * @return Set value type ComboBox
     */
    public JComboBox getSetValueTypeCombo()
    {
        return setValueTypeCombo;
    }
}

